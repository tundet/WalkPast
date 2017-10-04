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

import com.example.ryu.walkpast.Controller.StepCounter;
import com.example.ryu.walkpast.Database.DatabaseAdapter;
import com.example.ryu.walkpast.Database.DatabaseHelper;
import com.example.ryu.walkpast.Fragments.ImageFragment;
import com.example.ryu.walkpast.Fragments.UserInputFragment;
import com.example.ryu.walkpast.Model.Choice;
import com.example.ryu.walkpast.Model.Page;
import com.example.ryu.walkpast.Model.Player;
import com.example.ryu.walkpast.Model.Story;
import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends Activity implements StepCounter.Listener, UserInputFragment.OnFragmentInteractionListener, ServiceConnection {

    private BtleService.LocalBinder serviceBinder;
    private MetaWearBoard mwBoard;
    private ImageView backgroundView;
    private TextView storyTextView, counterTextView, totalTextView;
    private Player mPlayer;
    private Button choice1, choice2;
    private Page mCurrentPage;
    private StepCounter mStepCounter;
    private ProgressBar mProgressBar;
    private LinearLayout storyLayout;
    private int nextPage;
    private Accelerometer accelerometer;
    private DatabaseAdapter dbAdapter;
    private UserInputFragment userInputFragment;
    private ImageFragment imageFragment;
    private Boolean metaWear = false;

    /*
     SETUP ACTIVITY
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup layouts
        setContentView(R.layout.activity_main);
        storyLayout = findViewById(R.id.storylayout);

        //bind the BLE service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);

        //setup database, insert story in it and create adapter
        new DatabaseHelper(this);
        new Story(this);
        dbAdapter = new DatabaseAdapter(this);

        //get views, buttons and progressbar
        backgroundView = findViewById(R.id.background);
        storyTextView = findViewById(R.id.storyTextView);
        counterTextView = findViewById(R.id.counterTextView);
        totalTextView = findViewById(R.id.totalTextView);
        choice1 = findViewById(R.id.choiceButton1);
        choice2 = findViewById(R.id.choiceButton2);
        mProgressBar = findViewById(R.id.stepProgressBar);

        //create player to save steps counted
        mPlayer = new Player();

        //get fragments
        userInputFragment = (UserInputFragment) getFragmentManager().findFragmentById(R.id.fragmentinput);
        imageFragment = (ImageFragment) getFragmentManager().findFragmentById(R.id.fragmentimg);

        //load first page of array onto view
        loadPage(0);
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
            //TODO: show total steps at end of game
            choice1.setText(c.getText());
            choice1.setVisibility(View.VISIBLE);
            choice2.setVisibility(View.GONE);
        }

        //listener for choices, when clicked get next page's index, start listening to counter
        //start accelerator, hide and show desired views
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice c = dbAdapter.getChoice(mCurrentPage.getChoice1());
                nextPage = c.getNextPage();//getting which next story
                mStepCounter.startListening(MainActivity.this);
                startAccelerator();
                buttonsInVisible();
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
        counterTextView.setText(getString(R.string.needed_steps) + String.valueOf(mCurrentPage.getSteps() - steps));
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
    public void onFragmentInteraction(String userContent, Boolean story) {
        hideKeyboard(this);
        userInputFragment.userInput.setVisibility(View.GONE);
        userInputFragment.update.setVisibility(View.GONE);
        //TODO: put strings in resource
        userInputFragment.welcomemsg.setText("Hello " + userContent + ". This is an interactive story that requires " +
                "actual walking around in real life. Are you ready to take a few steps to reach your destination?");
        imageFragment.updateImageView(userContent, this);

        //if story is set to true in user input fragment, the story can be shown on main layout
        if (story) {
            storyLayout.setVisibility(View.VISIBLE);
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

        //insert MetaWear MAC Address here
        retrieveBoard("CA:CF:B2:0E:44:36");
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
    public void retrieveBoard(final String macAddress) {
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
                                Float x = data.value(Acceleration.class).x();
                                Log.i("accelerator", y.toString());
                                if (x > 0) {
                                    imageFragment.updateImage.setRotation(3);
                                } else if (x < 0) {
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
    public void startAccelerator() {
        if (metaWear) {
            accelerometer.acceleration().start();
            accelerometer.start();
        }
    }

    /*
     STOP ACCELERATOR
     */
    public void stopAccelerator() {
        if (metaWear) {
            accelerometer.acceleration().stop();
            accelerometer.stop();
        }
    }

    /*
     HIDE KEYBOARD
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}