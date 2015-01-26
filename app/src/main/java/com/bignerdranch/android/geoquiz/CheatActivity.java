package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Lucas Chigami on 26/01/2015.
 */
public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_cheat);
    }
}
