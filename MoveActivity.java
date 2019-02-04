package com.nepali.nepali_app.nepali_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MoveActivity extends AppCompatActivity {

    String title;
    String small_title;
    TextView title_label;
    TextView small_title_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        title_label = (TextView) findViewById(R.id.title_label);
        small_title_label = (TextView) findViewById(R.id.small_title_label);

        Bundle b = getIntent().getExtras();
        title = b.getString("title");
        small_title = b.getString("small_title");

        title_label.setText(title);
        small_title_label.setText(small_title);

        // 5초 뒤에 실행됩니다!!
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        openQuizActivity();
                    }
                }, 5000);
    }
    public void openQuizActivity(){
        Intent intent = new Intent(this,QuizActivity.class);
        startActivity(intent);
    }
}
