package com.example.ryu.walkpast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ryu.walkpast.Controller.StepCounter;
import com.example.ryu.walkpast.Model.Page;
import com.example.ryu.walkpast.Model.Player;
import com.example.ryu.walkpast.Model.Story;

public class MainActivity extends Activity implements StepCounter.Listener {

    private Story mStory = new Story();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mImageView = (ImageView)findViewById(R.id.storyImageView);
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
        mProgressBar.setMax(mCurrentPage.getSteps()); //setting required steps for progress bar
        String pageText = mCurrentPage.getText();
        storyTextView.setText(pageText);

        if (mCurrentPage.getChoice2() != null) {
            mStepCounter.startListening(this);
            buttonsVisible();
            choice1.setText(mCurrentPage.getChoice1().getText());
            choice2.setText(mCurrentPage.getChoice2().getText());
            mProgressBar.setProgress(0);
        }else{
            mStepCounter.startListening(this);
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
                mProgressBar.setVisibility(View.VISIBLE);
                buttonsInVisible();
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage = mCurrentPage.getChoice2().getNextPage();
                mStepCounter.startListening(MainActivity.this);
                mProgressBar.setVisibility(View.VISIBLE);
                buttonsInVisible();
            }
        });
    }

    private void buttonsVisible(){
        choice1.setVisibility(View.VISIBLE);
        choice2.setVisibility(View.VISIBLE);
    }

    private void buttonsInVisible(){
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
}