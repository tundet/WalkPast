package com.example.ryu.walkpast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ryu.walkpast.Controller.StepCounter;
import com.example.ryu.walkpast.Model.Page;
import com.example.ryu.walkpast.Model.Player;
import com.example.ryu.walkpast.Model.Story;

public class MainActivity extends Activity implements StepCounter.Listener {

    SharedPreferences mSharedPreferences;

    private Story mStory = new Story();
    private TextView storyTextView;
    private TextView counterTextView;
    private Player mPlayer;
    private Button choice1;
    private Button choice2;
    private Page mCurrentPage;
    private StepCounter mStepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mImageView = (ImageView)findViewById(R.id.storyImageView);
        storyTextView = findViewById(R.id.storyTextView);
        counterTextView = findViewById(R.id.counterTextView);
        choice1 = findViewById(R.id.choiceButton1);
        choice2 = findViewById(R.id.choiceButton2);
        mSharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mStepCounter = new StepCounter(this);
        mPlayer = new Player();

        loadPage(0); //first page of array
    }

    private void loadPage(int choice) {
        mCurrentPage = mStory.getPage(choice);

        String pageText = mCurrentPage.getText();
        storyTextView.setText(pageText);
        if (mCurrentPage.getChoice2() != null) {
            choice2.setVisibility(View.VISIBLE);
            choice1.setText(mCurrentPage.getChoice1().getText());
            choice2.setText(mCurrentPage.getChoice2().getText());
        }else{
            choice1.setText(mCurrentPage.getChoice1().getText());
            choice2.setVisibility(View.INVISIBLE);
        }

        //onClickListener for each button and replace the story with new story

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = mCurrentPage.getChoice1().getNextPage();//getting which next story
                loadPage(nextPage);// and replace with old story
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = mCurrentPage.getChoice2().getNextPage();
                loadPage(nextPage);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStepCounter.startListening(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStepCounter.stopListening();
    }

    @Override
    public void onStepChanged(int steps) {
        mPlayer.setSteps(steps);
        counterTextView.setText(getString(R.string.total_steps) + String.valueOf(steps));
    }
}