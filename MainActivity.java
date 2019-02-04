package com.nepali.nepali_app.nepali_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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

public class MainActivity extends AppCompatActivity {
    //GifImageView gifImageView;
    private Button login_button;
    private Button register_button;
    private Button passwd_find_button;
    private EditText edit_login;
    private EditText edit_passwd;
    private TextView text;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login_button = (Button) findViewById(R.id.login_button);
        register_button = (Button) findViewById(R.id.register_button);
        passwd_find_button = (Button) findViewById(R.id.passwd_find_button);
        edit_login = (EditText) findViewById(R.id.edit_login);
        edit_passwd = (EditText) findViewById(R.id.edit_passwd);
        text = (TextView) findViewById(R.id.test);
        dataBaseHelper = new DataBaseHelper(this);

        Cursor data = dataBaseHelper.getData();
        if(data.moveToFirst()){
            openUpdateActivity();
        }
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_check();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        passwd_find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("비밀번호 찾기","관리자에게 문의하세요");
            }
        });

    }
    public void addData(String id,String name, int manager ){
        boolean insertData = dataBaseHelper.addData(id,name,manager);
        if(insertData){
            //alert("sql결과","성공"+id+name+manager);
        }else{
            alert("sql결과","실패");
        }
    }

    public void login_check(){
        OkHttpClient client = new OkHttpClient();
        MediaType login_data = MediaType.parse("application/jason;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {
            actualData.put("id",edit_login.getText().toString());
            actualData.put("passwd",edit_passwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://zun1091.synology.me/words_app/index.php";
        RequestBody body = RequestBody.create(login_data,actualData.toString());
        Request request = new Request.Builder()
                                        .url(url)
                                        .post(body)
                                        .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String message = e.getMessage();
                MainActivity.this.runOnUiThread(new Runnable() {
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

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //text.setText(myresponse);
                            if(myresponse.trim().equals("0")){
                                alert("로그인 실패","없는 아이디이거나 비밀번호가 틀렸습니다");
                            }else if(myresponse.trim().equals("-1")){
                                alert("로그인 실패","없는 아이디이거나 비밀번호가 틀렸습니다");
                            }else{
                                try {
                                    JSONArray jsonArray = new JSONArray(myresponse);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    addData(jsonObject.getString("id"),jsonObject.getString("name"),Integer.parseInt(jsonObject.getString("manager")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                openUpdateActivity();
                            }

                        }
                    });
                }
            }
        });
    }
    public  void openDialog(String diaMessage){
        LoginDialog dialog1 = new LoginDialog();
        dialog1.show(getSupportFragmentManager(),"예시");
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

    public void openHomeActivity(){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
    public void openRegisterActivity(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void openMain2Activity(){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
    public void openUpdateActivity(){
        Intent intent = new Intent(this,UpdateActivity.class);
        startActivity(intent);
    }
}
