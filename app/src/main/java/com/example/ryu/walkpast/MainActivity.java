package com.example.ryu.walkpast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ryu.walkpast.Model.Page;
import com.example.ryu.walkpast.Model.Story;

public class MainActivity extends Activity {

    private Story mStory = new Story();
    private TextView mTextView;
    private Button mChoice1;
    private Button mChoice2;
    private Page mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mImageView = (ImageView)findViewById(R.id.storyImageView);
        mTextView = findViewById(R.id.storyTextView);
        mChoice1 = findViewById(R.id.choiceButton1);
        mChoice2 = findViewById(R.id.choiceButton2);

        loadPage(0); //first page of array
    }

    private void loadPage(int choice) {
        mCurrentPage = mStory.getPage(choice);

        String pageText = mCurrentPage.getText();
        mTextView.setText(pageText);
        if (mCurrentPage.getChoice2() != null) {
            mChoice2.setVisibility(View.VISIBLE);
            mChoice1.setText(mCurrentPage.getChoice1().getText());
            mChoice2.setText(mCurrentPage.getChoice2().getText());
        }else{
            mChoice1.setText(mCurrentPage.getChoice1().getText());
            mChoice2.setVisibility(View.INVISIBLE);
        }

        //onClickListener for each button and replace the story with new story

        mChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = mCurrentPage.getChoice1().getNextPage();//getting which next story
                loadPage(nextPage);// and replace with old story
            }
        });

        mChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = mCurrentPage.getChoice2().getNextPage();
                loadPage(nextPage);
            }
        });
    }

}