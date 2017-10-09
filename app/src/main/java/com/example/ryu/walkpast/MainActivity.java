package com.example.ryu.walkpast;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryu.walkpast.controller.StepCounter;
import com.example.ryu.walkpast.database.DatabaseAdapter;
import com.example.ryu.walkpast.database.DatabaseHelper;
import com.example.ryu.walkpast.fragments.ImageFragment;
import com.example.ryu.walkpast.fragments.UserInputFragment;
import com.example.ryu.walkpast.model.Choice;
import com.example.ryu.walkpast.model.Page;
import com.example.ryu.walkpast.model.Player;
import com.example.ryu.walkpast.model.Story;
import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;

import java.util.Objects;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends Activity implements StepCounter.Listener, UserInputFragment.OnFragmentInteractionListener, ServiceConnection {
    public static Player mPlayer;
    //TODO: GPS to recognize speed or something
    private BtleService.LocalBinder serviceBinder;
    private MetaWearBoard mwBoard;
    private ImageView backgroundView;
    private TextView storyTextView, counterTextView, totalTextView;
    private Button choice1, choice2;
    private Page mCurrentPage;
    private StepCounter mStepCounter;
    private ProgressBar mProgressBar;
    private LinearLayout storyLayout;
    private int nextPage;
    private Accelerometer accelerometer;
    private ImageFragment imageFragment;
    private Boolean metaWear = false;
    private UserInputFragment userInputFragment;
    private DatabaseAdapter dbAdapter;
    private Context context;

    /*
     SETUP ACTIVITY
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        //setup layouts
        setContentView(R.layout.activity_main);
        storyLayout = findViewById(R.id.storylayout);

        //bind the BLE service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);

        //setup database, insert story in it and create adapter
        this.deleteDatabase("StoriesDatabase");
        new DatabaseHelper(this);
        dbAdapter = new DatabaseAdapter(this);
        new Story(this);
        for (Page page : Story.getPages()) {
            Log.i("page", "added");
            dbAdapter.savePage(page);
        }
        for (Choice choice : Story.getChoices()) {
            Log.i("choice", "added");
            dbAdapter.saveChoice(choice);
        }
        dbAdapter.pages = dbAdapter.retrievePages();
        dbAdapter.choices = dbAdapter.retrieveChoices();

        //get views, buttons and progressbar
        backgroundView = findViewById(R.id.background);
        storyTextView = findViewById(R.id.storyTextView);
        counterTextView = findViewById(R.id.counterTextView);
        totalTextView = findViewById(R.id.totalTextView);
        choice1 = findViewById(R.id.choiceButton1);
        choice2 = findViewById(R.id.choiceButton2);
        mProgressBar = findViewById(R.id.stepProgressBar);

        //create player to save steps counted
        mPlayer = new Player(this);

        //get fragments and update input texts to player prefs
        imageFragment = (ImageFragment) getFragmentManager().findFragmentById(R.id.fragmentimg);
        userInputFragment = (UserInputFragment) getFragmentManager().findFragmentById(R.id.fragmentinput);
        userInputFragment.editUserName.setText(mPlayer.getName());
        userInputFragment.editMetaWear.setText(mPlayer.getAddress());
    }

    /*
     LOAD A NEW PAGE
     */
    private void loadPage(int choice) {
        mStepCounter = new StepCounter(this); //new counter
        mCurrentPage = dbAdapter.getPage(choice); //update current page

        //get page background from resources and insert into view
        int resID = getResources().getIdentifier(mCurrentPage.getBackground(), "drawable", getPackageName());
        backgroundView.setImageResource(resID);

        mProgressBar.setMax(mCurrentPage.getSteps()); //setting required steps for progress bar

        //get page's story and place into view
        String pageText = mCurrentPage.getText();
        storyTextView.setText(pageText);

        //get choices, check whether there are two or one for the page and show them in buttons
        Choice c = dbAdapter.getChoice(mCurrentPage.getChoice1());
        if (mCurrentPage.getChoice2() != 0) {
            buttonsVisible();
            choice1.setText(c.getText());
            c = dbAdapter.getChoice(mCurrentPage.getChoice2());
            choice2.setText(c.getText());
            mProgressBar.setProgress(0);
        } else {
            choice1.setText(c.getText());
            choice1.setVisibility(View.VISIBLE);
            choice2.setVisibility(View.GONE);
            totalTextView.setVisibility(View.VISIBLE);
        }

        //listener for choices, when clicked get next page's index, start listening to counter
        //start accelerator, hide and show desired views
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(choice1.getText(), "Start over")) {
                    recreate();
                } else {
                    Log.i("choice", choice1.getText().toString());
                    Choice c = dbAdapter.getChoice(mCurrentPage.getChoice1());
                    nextPage = c.getNextPage();//getting which next story
                    mStepCounter.startListening(MainActivity.this);
                    startAccelerator();
                    buttonsInVisible();
                    totalTextView.setVisibility(View.GONE);
                }
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice c = dbAdapter.getChoice(mCurrentPage.getChoice2());
                nextPage = c.getNextPage();//getting which next story
                mStepCounter.startListening(MainActivity.this);
                startAccelerator();
                buttonsInVisible();
            }
        });
    }

    /*
     SHOW CHOICE BUTTONS
     */
    private void buttonsVisible() {
        choice1.setVisibility(View.VISIBLE);
        choice2.setVisibility(View.VISIBLE);
    }

    /*
     HIDE CHOICE BUTTONS AND SHOW STEP VIEW AND PROGRESS BAR
     */
    private void buttonsInVisible() {
        choice1.setVisibility(View.GONE);
        choice2.setVisibility(View.GONE);

        counterTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /*
     MOVE PROGRESSBAR WHEN WALKING AND LOAD NEW PAGE WHEN DONE
     */
    @Override
    public void onStepChanged(int steps) {
        //update player's, views' and progressbar's steps
        mPlayer.setSteps(steps);
        totalTextView.setText(getString(R.string.total_steps) + String.valueOf(mPlayer.getTotalSteps() + steps));
        mProgressBar.setProgress(steps);

        //when progressbar is full stop listening, reset player's current steps and load next page
        //hide counter and progressbar and stop accelerator
        if (steps >= mCurrentPage.getSteps()) {
            mStepCounter.stopListening();
            mPlayer.setTotalSteps(mPlayer.getSteps());
            mPlayer.setSteps(0);
            loadPage(nextPage);
            mProgressBar.setVisibility(View.INVISIBLE);
            counterTextView.setVisibility(View.GONE);
            stopAccelerator();
        }
    }

    /*
     NOTICE CHANGE IN USER INPUT FRAGMENT AND GIVE THE CONTENT TO IMAGE FRAGMENT
     */
    @Override
    public void onFragmentInteraction(String userContent, String metaAddress, Boolean story) {
        hideKeyboard(this);
        mPlayer.setName(userContent);
        userInputFragment.editUserName.setText(mPlayer.getName());
        imageFragment.updateImageView(userContent, this);
        mPlayer.setAddress(metaAddress);

        //if story is set to true in user input fragment, the story can be shown on main layout
        if (story) {
            storyLayout.setVisibility(View.VISIBLE);
            //insert MetaWear MAC Address here
            retrieveBoard(mPlayer.getAddress());
            //load first page of array onto view
            loadPage(0);
        }
    }

    /*
     WHEN ACTIVITY IS DESTROYED
     UNBIND SERVICE, TEAR DOWN CONNECTION TO ACCELERATOR, STOP LISTENING TO COUNTER
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unbindService(this);
        if (metaWear) {
            mwBoard.tearDown();
        }
        mStepCounter.stopListening();
    }

    /*
     CONNECTING TO SERVICE
     */
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        //typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) service;

        Log.i("accelerator", "Service connected");
    }

    /*
     SERVICE DISCONNECTING
     */
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.i("accelerator", "Service disconnected");
    }

    /*
     CONNECT TO BLUETOOTH DEVICE AND CONFIGURE ACCELERATOR
     */
    private void retrieveBoard(final String macAddress) {
        final BluetoothManager btManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice =
                btManager.getAdapter().getRemoteDevice(macAddress);

        //create a MetaWear board object for the Bluetooth Device
        mwBoard = serviceBinder.getMetaWearBoard(remoteDevice);
        mwBoard.connectAsync().onSuccessTask(new Continuation<Void, Task<Route>>() {
            @Override
            public Task<Route> then(Task<Void> task) throws Exception {
                Log.i("accelerator", "Connected to board");
                accelerometer = mwBoard.getModule(Accelerometer.class);
                accelerometer.configure()
                        .odr(25f)
                        .range(4f)
                        .commit();
                //configuration the algorithm to run as a detector
                return accelerometer.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                Float y = data.value(Acceleration.class).y();
                                if (y > 0) {
                                    imageFragment.updateImage.setRotation(3);
                                } else if (y < 0) {
                                    imageFragment.updateImage.setRotation(-5);
                                }
                            }
                        });
                    }
                });

            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                if (task.isFaulted()) {
                    //TODO: show toast on main UI thread whether board is configured
                    Log.w("accelerator", "Failed to configure", task.getError());
                } else {
                    Log.i("accelerator", "Configured");
                    metaWear = true; //confirm that board exists so that it can be referred to later
                }

                return null;
            }
        });
    }

    /*
     START ACCELERATOR
     */
    private void startAccelerator() {
        if (metaWear) {
            accelerometer.acceleration().start();
            accelerometer.start();
        }
    }

    /*
     STOP ACCELERATOR
     */
    private void stopAccelerator() {
        if (metaWear) {
            accelerometer.acceleration().stop();
            accelerometer.stop();
        }
    }

    /*
     HIDE KEYBOARD
     */
    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showToast() {
        if (!metaWear) {
            Toast.makeText(context, "Failed to connect to MetaWear", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Connected to MetaWear", Toast.LENGTH_LONG).show();
        }
    }



}