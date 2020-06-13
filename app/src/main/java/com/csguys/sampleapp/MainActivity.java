package com.csguys.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.csguys.viewmoretextview.ViewMoreTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewMoreTextView textView1 = findViewById(R.id.tv1);
        ViewMoreTextView textView2 = findViewById(R.id.tv2);
        ViewMoreTextView textView3 = findViewById(R.id.tv3);

        /*
          only set simple text
         */
        textView1.setCharText(R.string.sample_text);

        /*
         set text with span
         */
        SpannableStringBuilder builder = new SpannableStringBuilder(getString(R.string.sample_text));
        builder.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView2.setCharText(builder);

        /*
          set custom text
         */

        textView3.setCharText("Hello text");

    }
}