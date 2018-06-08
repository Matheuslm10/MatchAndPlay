package com.example.mathe.matchandplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;


public class Faq extends AppCompatActivity {

    private TextView textView3;
    private TextView textView5;
    private TextView textView7;
    private TextView textView2;
    private TextView textView4;
    private TextView textView6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        textView3 = findViewById(R.id.textView3);
        textView5 = findViewById(R.id.textView5);
        textView7 = findViewById(R.id.textView7);
        textView2 = findViewById(R.id.textView2);
        textView4 = findViewById(R.id.textView4);
        textView6 = findViewById(R.id.textView6);

        textView3.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        textView5.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        textView7.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

    }
}
