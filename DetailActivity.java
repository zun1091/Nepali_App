package com.nepali.nepali_app.nepali_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String id;
    private String origin;
    private String sound;
    private String meaning;
    private String role;

    ListView list;
    ListViewAdapter2 adapter;
    ArrayList<NepaliWords> arraylist2 = new ArrayList<NepaliWords>();
    SearchView search;
    TextView detail_id;
    TextView detail_origin;
    TextView detail_sound;
    TextView detail_meaning;
    Spinner role_spinner;
    ScrollView wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wrapper = (ScrollView) findViewById(R.id.wrapper);
        search = (SearchView) findViewById(R.id.search);
        search.setFocusable(false);
        list = (ListView) findViewById(R.id.listView);
        detail_id = (TextView) findViewById(R.id.detail_id);
        detail_origin= (TextView) findViewById(R.id.detail_origin);
        detail_sound= (TextView) findViewById(R.id.detail_sound);
        detail_meaning= (TextView) findViewById(R.id.detail_meaning);
        role_spinner= (Spinner)  findViewById(R.id.role_spinner);
        ArrayAdapter<CharSequence> adt = ArrayAdapter.createFromResource(this, R.array.roles,android.R.layout.simple_spinner_item);
        adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spinner.setAdapter(adt);
        role_spinner.setOnItemSelectedListener(this);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        origin = b.getString("origin");
        sound = b.getString("sound");
        meaning = b.getString("meaning");
        role = b.getString("role");

        detail_id.setText(id);
        detail_origin.setText(origin);
        detail_sound.setText(sound);
        detail_meaning.setText(meaning);
        setSpinText(role_spinner,role);

        //       --------------------------------------------------------------------------------------------- okHTTP start
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
                DetailActivity.this.runOnUiThread(new Runnable() {
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

                    DetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //origin,M,S
                                JSONArray jsonArray = new JSONArray(myresponse);
                                for(int i = 0 ; i < jsonArray.length(); i ++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    NepaliWords nepaliWords = new NepaliWords(Integer.parseInt(jsonObject.getString("id")),jsonObject.getString("origin"),jsonObject.getString("S"),jsonObject.getString("M"),jsonObject.getString("role"));
                                    arraylist2.add(nepaliWords);
                                }
                                // Pass results to ListViewAdapter Class
                                adapter = new ListViewAdapter2(DetailActivity.this, arraylist2);
                                // Binds the Adapter to the ListView
                                list.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
//       --------------------------------------------------------------------------------------------- okHTTP end
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wrapper.setVisibility(View.INVISIBLE);
                list.setVisibility(View.VISIBLE);

                adapter.filter(newText);
                return  false;
            }
        });
        // Catch event on [x] button inside search view
        int searchCloseButtonId = search.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.search.findViewById(searchCloseButtonId);

        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setQuery("",false);
                list.setVisibility(View.INVISIBLE);
                wrapper.setVisibility(View.VISIBLE);
                search.clearFocus();
            }
        });
//       --------------------------------------------------------------------------------------------- 서치 이벤트 끝
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tx_id  = (TextView) view.findViewById(R.id.words_id);
                TextView tx_origin  = (TextView) view.findViewById(R.id.origin);
                TextView tx_sound  = (TextView) view.findViewById(R.id.sound);
                TextView tx_meaning  = (TextView) view.findViewById(R.id.meaning);
                TextView tx_role  = (TextView) view.findViewById(R.id.role);
                openDetailActivity(tx_id.getText().toString(),tx_origin.getText().toString(),tx_sound.getText().toString(),tx_meaning.getText().toString(),tx_role.getText().toString());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

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
    public void openDetailActivity(String id,String origin, String sound, String meaning, String role){
        Intent intent = new Intent(this,DetailActivity.class);
        Bundle b = new Bundle();
        b.putString("id", id); //Your id
        b.putString("origin", origin);
        b.putString("sound", sound);
        b.putString("meaning", meaning);
        b.putString("role", role);
        intent.putExtras(b);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (wrapper.getVisibility() == View.VISIBLE) {

            Intent intent = new Intent(this,Main2Activity.class);
            startActivity(intent);

        }else if(list.getVisibility()==View.VISIBLE){
            list.setVisibility(View.INVISIBLE);
            wrapper.setVisibility(View.VISIBLE);
            search.clearFocus();
        }else {
            super.onBackPressed();
        }
    }
}
