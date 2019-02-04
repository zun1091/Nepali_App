package com.nepali.nepali_app.nepali_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
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

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SearchView search;

    ListView list;
    ListViewAdapter2 adapter;
    ArrayList<NepaliWords> arraylist2 = new ArrayList<NepaliWords>();
    TextView nav_name;
    DataBaseHelper dataBaseHelper;
    UpdateDataHelper updateDataHelper;
    String test_data = "";
    ScrollView wrapper;
    RelativeLayout quiz_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = (ListView) findViewById(R.id.listView);
        wrapper = (ScrollView) findViewById(R.id.wrapper);
        search = (SearchView) findViewById(R.id.search);
        quiz_layout = (RelativeLayout) findViewById(R.id.quiz_layout);
        search.setFocusable(false);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = nav_view.getHeaderView(0);
        nav_name = (TextView) headerLayout.findViewById(R.id.nav_name);
        dataBaseHelper = new DataBaseHelper(this);
        updateDataHelper = new UpdateDataHelper(this);
        Cursor data = dataBaseHelper.getData();
        if(data.moveToFirst()){
            nav_name.setText(data.getString(1)+"님 안녕하세요!");
        }
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
                Main2Activity.this.runOnUiThread(new Runnable() {
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

                    Main2Activity.this.runOnUiThread(new Runnable() {
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
                                adapter = new ListViewAdapter2(Main2Activity.this, arraylist2);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//       --------------------------------------------------------------------------------------------- 기본 정보 끝
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

        quiz_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMoveActivity("오늘도 즐겁게!","네팔어 고수가 되는 10단어");
            }
        });

    }// OnCreate 메소드의 끝

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(list.getVisibility()==View.VISIBLE){
            list.setVisibility(View.INVISIBLE);
            wrapper.setVisibility(View.VISIBLE);
            search.clearFocus();
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_page) {
            alert("테스트","홈페이지");

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_send3) {
            dataBaseHelper.delete_login();
            openMainActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
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
    public void openMoveActivity(String title, String small_title){
        Intent intent = new Intent(this,MoveActivity.class);
        Bundle b = new Bundle();
        b.putString("title", title); //Your id
        b.putString("small_title", small_title);
        intent.putExtras(b);
        startActivity(intent);
    }
}
