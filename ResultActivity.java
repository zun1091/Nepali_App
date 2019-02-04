package com.nepali.nepali_app.nepali_app;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    String result;
    String color;
    TextView result_label;
    TextView ok_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ok_button = (TextView) findViewById(R.id.ok_button);
        result_label = (TextView) findViewById(R.id.result_label);
        Bundle b = getIntent().getExtras();
        result = b.getString("result");
        color = b.getString("color");

        result_label.setText(result);
        if(color.equals("red")){
            result_label.setTextColor(getResources().getColor(R.color.red, null));
        }if(color.equals("blue")){
            result_label.setTextColor(getResources().getColor(R.color.blue, null));
        }

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_quiz_clicked));
                ok_button.setTextColor(getResources().getColor(R.color.white, null));
                float value1 = 110 * getResources().getDisplayMetrics().density;
                float value2 = 10 * getResources().getDisplayMetrics().density;
                ok_button.setPadding((int) value1,(int) value2,(int) value1,(int) value2);
                ok_button.setTextSize(20);
                openQuizActivity();
            }
        });
    }
    public void openQuizActivity(){
        Intent intent = new Intent(this,QuizActivity.class);
        startActivity(intent);
    }
    public void openMain2Activity(){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openMain2Activity();
    }
}
