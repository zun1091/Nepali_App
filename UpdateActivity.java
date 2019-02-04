package com.nepali.nepali_app.nepali_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class UpdateActivity extends AppCompatActivity {

    UpdateDataHelper updateDataHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateDataHelper = new UpdateDataHelper(this);

        OkHttpClient client = new OkHttpClient();
        MediaType search_data = MediaType.parse("application/jason;charset=utf-8");
        JSONObject actualData = new JSONObject();

        try {

            actualData.put("all","all");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://zun1091.synology.me/words_app/search/search_data.php";
        RequestBody body = RequestBody.create(search_data,actualData.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String message = e.getMessage();
                UpdateActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert("접속 에러","인터넷에 연결해주세요~!!");
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(response.isSuccessful()){
                    final String myresponse = response.body().string();

                    UpdateActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //origin,M,S
                                JSONArray jsonArray = new JSONArray(myresponse);
                                for(int i = 0 ; i < jsonArray.length(); i ++){
                                   JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    boolean ins = updateDataHelper.addWord(Integer.parseInt(jsonObject.getString("id")),jsonObject.getString("origin"),jsonObject.getString("M"),jsonObject.getString("S"),jsonObject.getString("role"),Integer.parseInt(jsonObject.getString("counts")));
                                    if(i == jsonArray.length()-1){
                                        openMain2Activity();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


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
    public void openMain2Activity(){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
}
