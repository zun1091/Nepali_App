package com.nepali.nepali_app.nepali_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    CountDownTimer ctd;
    long total_time = 10000;
    ProgressBar determinateBar;
    TextView time_passed;
    TextView origin;
    TextView sound;
    TextView answer1;
    TextView answer2;
    TextView answer3;
    TextView answer4;
    TextView b1;
    TextView b2;
    TextView b3;
    TextView b4;

    int finished = 0;
    UpdateDataHelper updateDataHelper;
    String real_word;
    String real_sound;
    String real_meaning;
    int real_id;
    String real_role;
    String answer[] = new String[]{"1","2","3"};
    int answer_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        time_passed = (TextView) findViewById(R.id.time_passed);
        determinateBar  =(ProgressBar)findViewById(R.id.determinateBar);
        origin = (TextView) findViewById(R.id.origin);
        sound = (TextView) findViewById(R.id.sound);
        answer1 = (TextView) findViewById(R.id.answer1);
        answer2 = (TextView) findViewById(R.id.answer2);
        answer3 = (TextView) findViewById(R.id.answer3);
        answer4 = (TextView) findViewById(R.id.answer4);
        b1 = (TextView) findViewById(R.id.b1);
        b2 = (TextView) findViewById(R.id.b2);
        b3 = (TextView) findViewById(R.id.b3);
        b4 = (TextView) findViewById(R.id.b4);

        updateDataHelper = new UpdateDataHelper(this);
        updateDataHelper.getData();
        Cursor dt = updateDataHelper.getQuizWord();
        if(dt.moveToFirst()){
            real_id = dt.getInt(0);
            real_word = dt.getString(1);
            real_meaning = dt.getString(2);
            real_sound = dt.getString(3);
            real_role = dt.getString(4);
        }

        Cursor data = updateDataHelper.getQuizAnswers(real_id,real_role);
        while (data.moveToNext()){
            answer[data.getPosition()] = data.getString(0);
        }
        int an_num = (int) (Math.random()*4+1); // 0<num<5 결국 1,2,3,4 중 하나를 출력하게 된다.

        if(an_num==1){
            answer1.setText(real_meaning);
            answer_number = 1;
            answer2.setText(answer[0]);
            answer3.setText(answer[1]);
            answer4.setText(answer[2]);
        }else if(an_num==2){
            answer2.setText(real_meaning);
            answer_number = 2;
            answer1.setText(answer[0]);
            answer3.setText(answer[1]);
            answer4.setText(answer[2]);
        }else if(an_num==3){
            answer3.setText(real_meaning);
            answer_number = 3;
            answer2.setText(answer[0]);
            answer1.setText(answer[1]);
            answer4.setText(answer[2]);
        }
        else if(an_num==4){
            answer4.setText(real_meaning);
            answer_number = 4;
            answer2.setText(answer[0]);
            answer3.setText(answer[1]);
            answer1.setText(answer[2]);
        }
        sound.setText(real_sound);
        origin.setText(real_word);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

         // initiate the progress bar
        determinateBar.setMax(10); // 100 maximum value for the progress value
        determinateBar.setProgress(10); // 50 default progress value for the progress bar
        countDown();
    }
    public void openResultActivity(String result,String color){
        Intent intent = new Intent(this,ResultActivity.class);
        Bundle b = new Bundle();
        b.putString("result", result); //Your id
        b.putString("color", color);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void countDown(){
        ctd = new CountDownTimer(total_time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                total_time = millisUntilFinished;
                int left_seconds = (int) total_time /1000;
                time_passed.setText(Integer.toString(left_seconds)+"초");
                determinateBar.setProgress(left_seconds);
            }

            @Override
            public void onFinish() {
                if(finished==0){
                    time_passed.setText(Integer.toString(0)+"초");
                    determinateBar.setProgress(0);
                    openResultActivity("시간을 초과했습니다.","red");
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
            finished = 1;
            super.onBackPressed();

    }
    public void alert(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
