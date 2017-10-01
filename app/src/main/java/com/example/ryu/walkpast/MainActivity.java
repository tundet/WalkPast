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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ryu.walkpast.Controller.StepCounter;
import com.example.ryu.walkpast.Database.DatabaseHelper;
import com.example.ryu.walkpast.Fragments.ImageFragment;
import com.example.ryu.walkpast.Fragments.UserInputFragment;
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
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.AccelerometerBmi160;
import com.mbientlab.metawear.module.AccelerometerBmi160.StepDetectorMode;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends Activity implements StepCounter.Listener, UserInputFragment.OnFragmentInteractionListener, ServiceConnection {

    DatabaseHelper storyDatabase;
    private BtleService.LocalBinder serviceBinder;
    private MetaWearBoard mwBoard;
    private Story mStory = new Story();
    private ImageView backgroundView;
    private TextView storyTextView;
    private TextView counterTextView;
    private TextView totalTextView;
    private Player mPlayer;
    private Button choice1;
    private Button choice2;
    private Page mCurrentPage;
    private StepCounter mStepCounter;
    private ProgressBar mProgressBar;
    private int nextPage;
    private Accelerometer accelerometer;
    private AccelerometerBmi160 accBmi160;
    private AccelerometerBmi160.StepDetectorDataProducer stepDetector;

    //hide keyboard after pressing button
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);
        storyDatabase = new DatabaseHelper(this);
        backgroundView = findViewById(R.id.background);
        storyTextView = findViewById(R.id.storyTextView);
        counterTextView = findViewById(R.id.counterTextView);
        totalTextView = findViewById(R.id.totalTextView);
        choice1 = findViewById(R.id.choiceButton1);
        choice2 = findViewById(R.id.choiceButton2);
        mProgressBar = findViewById(R.id.stepProgressBar);
        mPlayer = new Player();

        loadPage(0); //first page of array
    }

    private void loadPage(int choice) {
        mStepCounter = new StepCounter(this);
        mCurrentPage = mStory.getPage(choice);
        int resID = getResources().getIdentifier(mCurrentPage.getBackground(), "drawable", getPackageName());
        backgroundView.setImageResource(resID);
        mProgressBar.setMax(mCurrentPage.getSteps()); //setting required steps for progress bar
        String pageText = mCurrentPage.getText();
        storyTextView.setText(pageText);

        if (mCurrentPage.getChoice2() != null) {
            buttonsVisible();
            choice1.setText(mCurrentPage.getChoice1().getText());
            choice2.setText(mCurrentPage.getChoice2().getText());
            mProgressBar.setProgress(0);
        } else {
            choice1.setText(mCurrentPage.getChoice1().getText());
            choice1.setVisibility(View.VISIBLE);
            choice2.setVisibility(View.INVISIBLE);
        }

        //onClickListener for each button and replace the story with new story

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage = mCurrentPage.getChoice1().getNextPage();//getting which next story
                mStepCounter.startListening(MainActivity.this);
                //accelerometer.acceleration().start();
                //accelerometer.start();
                mProgressBar.setVisibility(View.VISIBLE);
                buttonsInVisible();
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage = mCurrentPage.getChoice2().getNextPage();
                mStepCounter.startListening(MainActivity.this);
                //accelerometer.acceleration().start();
                //accelerometer.start();
                mProgressBar.setVisibility(View.VISIBLE);
                buttonsInVisible();
            }
        });
    }

    private void buttonsVisible() {
        choice1.setVisibility(View.VISIBLE);
        choice2.setVisibility(View.VISIBLE);
    }

    private void buttonsInVisible() {
        choice1.setVisibility(View.INVISIBLE);
        choice2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mStepCounter.startListening(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStepCounter.stopListening();
    }

    @Override
    public void onStepChanged(int steps) {
        mPlayer.setSteps(steps);
        counterTextView.setText(getString(R.string.needed_steps) + String.valueOf(mCurrentPage.getSteps()));
        totalTextView.setText(getString(R.string.total_steps) + String.valueOf(mPlayer.getTotalSteps()));
        mProgressBar.setProgress(steps);

        if (steps >= mCurrentPage.getSteps()) {
            mStepCounter.stopListening();
            mPlayer.setTotalSteps(mPlayer.getSteps());
            mPlayer.setSteps(0);
            loadPage(nextPage);// and replace with old story
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    //notice change in userinputfragment and update imagefragment
    @Override
    public void onFragmentInteraction(String userContent) {
        UserInputFragment userInputFragment = (UserInputFragment) getFragmentManager().findFragmentById(R.id.fragmentinput);
        ImageFragment imageFragment = (ImageFragment) getFragmentManager().findFragmentById(R.id.fragmentimg);
        hideKeyboard(this);
        userInputFragment.userInput.setVisibility(View.GONE);
        userInputFragment.update.setVisibility(View.GONE);
        userInputFragment.welcomemsg.setText("Hello " + userContent + ". Insert welcome message and instructions here.");
        imageFragment.updateImageView(userContent, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        // Typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) service;

        Log.i("accelerator", "Service connected");

        //Insert MetaWear MAC Address here
        retrieveBoard("CA:CF:B2:0E:44:36");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public void retrieveBoard(final String macAddress) {
        final BluetoothManager btManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice =
                btManager.getAdapter().getRemoteDevice(macAddress);

        // Create a MetaWear board object for the Bluetooth Device
        mwBoard = serviceBinder.getMetaWearBoard(remoteDevice);
        mwBoard.connectAsync().onSuccessTask(new Continuation<Void, Task<Route>>() {
            @Override
            public Task<Route> then(Task<Void> task) throws Exception {

                Log.i("accelerator", "Connected to board");

                accBmi160 = mwBoard.getModule(AccelerometerBmi160.class);
                stepDetector = accBmi160.stepDetector();
                accelerometer = mwBoard.getModule(Accelerometer.class);
                accelerometer.configure()
                        .odr(25f)       // Set sampling frequency to 25Hz, or closest valid ODR
                        .range(4f)      // Set data range to +/-4g, or closet valid range
                        .commit();
                // Configuration the algorithm to run as a detector
                // using normal detection mode
                stepDetector.configure()
                        .mode(StepDetectorMode.NORMAL)
                        .commit();

                //Log.i("accelerator", "Actual Odr = " + accelerometer.getOdr());
                //Log.i("accelerator", "Actual Range = " + accelerometer.getRange());


                return accelerometer.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                //Log.i("accelerator", data.value(Acceleration.class).toString());
                            }
                        });
                    }
                });

                /*return stepDetector.addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                Log.i("accelerator", data.value(Acceleration.class).toString());
                                Log.i("accelerator", data.value(AccelerometerBmi160.class).toString());
                                Log.i("MainActivity", "Took a step");
                            }
                        });
                    }
                });*/
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                if (task.isFaulted()) {
                    Log.w("accelerator", "Failed to configure", task.getError());
                } else {
                    Log.i("accelerator", "Configured");
                    //stepDetector.start();
                    //accBmi160.start();
                    accelerometer.acceleration().start();
                    accelerometer.start();
                }

                return null;
            }
        });
    }

}