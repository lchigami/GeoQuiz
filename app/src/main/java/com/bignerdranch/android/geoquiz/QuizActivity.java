package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lucas Chigami on 26/01/2015.
 */
public class QuizActivity extends Activity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String CHEAT_LIST = "cheat_list";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    private boolean mIsCheater;

    private ArrayList<Integer> mCheatList;

    //Function that verifies is the user is a cheater in that question
    private void cheatFromList() {
        if (mCheatList.contains(mCurrentIndex)) {
            mIsCheater = true;
        } else {
            mIsCheater = false;
        }
    }

    private void updateQuestion () {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer (boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mCheatList = savedInstanceState.getIntegerArrayList(CHEAT_LIST);
        } else {
            mCheatList = new ArrayList<Integer>();
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                mIsCheater = false;
                cheatFromList();
                updateQuestion();
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                mIsCheater = false;
                cheatFromList();
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton)findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex >= 0) {
                    mCurrentIndex = mCurrentIndex % mQuestionBank.length;
                } else {
                    mCurrentIndex = Math.abs(mCurrentIndex + mQuestionBank.length) % mQuestionBank.length;
                }
//                mIsCheater = false;
                cheatFromList();
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });

        cheatFromList();
        updateQuestion();
    }

    //Método que serve para guardar a instancia na rotacao de tela
    @Override
    public void onSaveInstanceState (Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        Log.i(TAG, "onSaveInstance() called.");
        savedInstance.putInt(KEY_INDEX, mCurrentIndex);
        savedInstance.putIntegerArrayList(CHEAT_LIST, mCheatList);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called.");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called.");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);

        if (!mCheatList.contains(mCurrentIndex) && mIsCheater) {
            mCheatList.add(mCurrentIndex);
        } else if (!mIsCheater && mCheatList.contains(mCurrentIndex)) {
            mIsCheater = true;
        }
    }
}
