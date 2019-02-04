package com.nepali.nepali_app.nepali_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    Button congregation;
    Button double_check;
    Button register_button;
    String[] cong_names;
    EditText edit_login;
    EditText edit_passwd;
    EditText pass_check;
    EditText login_name;
    int id_checked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        congregation = (Button) findViewById(R.id.congregation);
        double_check = (Button) findViewById(R.id.double_check);
        register_button = (Button) findViewById(R.id.register_button);
        edit_login = (EditText) findViewById(R.id.edit_login);
        edit_passwd = (EditText) findViewById(R.id.edit_passwd);
        pass_check = (EditText) findViewById(R.id.pass_check);
        login_name = (EditText) findViewById(R.id.login_name);


        congregation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cong_names = new String[]{"경기남양주오남남부네팔어(집단)","경기안산선부네팔어(집단)","경기양주옥정네팔어(집단)","경기화성발안동부네팔어(예비)","경남거제옥포서부네팔어(집단)","경남김해서부네팔어(집단)","경남칠원네팔어(집단)","대구지산네팔어(집단)","부산하단네팔어(집단)","서울신설네팔어(집단)","인천주안북부네팔어(집단)","전남목포동부네팔어(집단))","전남광주송정중앙네팔어(예비)","충남천안입장네팔어(예비)","충북청주수곡네팔어(예비)"};
                AlertDialog.Builder cong_dialog = new AlertDialog.Builder(RegisterActivity.this);
                cong_dialog.setTitle("회중을 선택하세요")
                            .setIcon(R.drawable.list_icon)
                            .setSingleChoiceItems(cong_names, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    congregation.setText(cong_names[which]);
                                    dialog.dismiss();
                                }
                            });
                AlertDialog dialog = cong_dialog.create();
                dialog.show();
            }
        });

        edit_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                id_checked = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        double_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit_login.getText().toString().equals("")) {
                    alert("확인 실패","아이디를 입력해주세요!");
                }else{
                    OkHttpClient client = new OkHttpClient();
                    MediaType login_data = MediaType.parse("application/jason;charset=utf-8");
                    JSONObject actualData = new JSONObject();

                    try {
                        actualData.put("id",edit_login.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://zun1091.synology.me/words_app/id_check.php";
                    RequestBody body = RequestBody.create(login_data,actualData.toString());
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            final String message = e.getMessage();
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alert("접속 에러","인터넷에 연결해주세요~!!");
                                }
                            });
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //Log.d("OKHTTP3","receive success");
                            //Log.d("OK",response.body().string().toString());
                            if(response.isSuccessful()){
                                final String myresponse = response.body().string();

                                RegisterActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //text.setText(myresponse);
                                        if(myresponse.trim().equals("0")){
                                            alert("사용 가능","사용 가능한 아이디입니다!");
                                            id_checked = 1;
                                        }else{
                                            alert("사용 불가","이미 있는 아이디입니다!");
                                            edit_login.setText("");
                                        }

                                    }
                                });
                            }
                        }
                    });


                }





            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_login.getText().toString().trim().equals("")){
                    alert("등록 실패","아이디를 입력해주세요");
                }else if(edit_passwd.getText().toString().trim().equals("")){
                    alert("등록 실패","패스워드를 입력해주세요");
                }else if(!pass_check.getText().toString().trim().equals(edit_passwd.getText().toString().trim())){
                    alert("등록 실패","비밀번호와 비밀번호 확인이 다릅니다. 확인해주세요");
                }else if(login_name.getText().toString().trim().equals("")){
                    alert("등록 실패","이름을 입력해주세요");
                }else if(congregation.getText().toString().trim().equals("선택하세요")){
                    alert("등록 실패","회중을 선택하세요");
                }else if(id_checked == 0){
                    alert("등록 실패","아이디 중복 확인을 해주세요");
                }else{

                    OkHttpClient client = new OkHttpClient();
                    MediaType login_data = MediaType.parse("application/jason;charset=utf-8");
                    JSONObject actualData = new JSONObject();

                    try {

                        actualData.put("id",edit_login.getText().toString());
                        actualData.put("passwd",edit_passwd.getText().toString());
                        actualData.put("name",login_name.getText().toString());
                        actualData.put("congregation",congregation.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://zun1091.synology.me/words_app/member/id_insert.php";
                    RequestBody body = RequestBody.create(login_data,actualData.toString());
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            final String message = e.getMessage();
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alert("접속 에러","인터넷에 연결해주세요~!!");
                                }
                            });
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //Log.d("OKHTTP3","receive success");
                            //Log.d("OK",response.body().string().toString());
                            if(response.isSuccessful()){
                                final String myresponse = response.body().string();

                                RegisterActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            alert_redirect("회원 등록",myresponse);
                                    }
                                });
                            }
                        }
                    });




                }
            }
        });

    }   // onCreate문의 끝

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
    public void alert_redirect(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
