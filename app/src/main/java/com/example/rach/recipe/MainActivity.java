package com.example.rach.recipe;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    private SpeechRecognizer recognizer;
    private Button btnDialog;
    private Button btnStart;
    private TextView textView;
    private TextView textView2;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout Top,Pasta,Desert,Rice,Steak,Salad;
    private Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processViews();
        processControllers();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String all = "";
                for (String r : result) {
                    all = all + r + "\n";
                }
                textView.setText(all);

            }
        }
    }

    private void processViews() {
//        btnDialog = (Button) this.findViewById(R.id.btn1);
//        textView = (TextView) this.findViewById(R.id.tv1);
//        btnStart = (Button) this.findViewById(R.id.btnStart);
//        textView2 = (TextView) this.findViewById(R.id.textView2);
          toolbar = (Toolbar) findViewById(R.id.toolbar);
          fab = (FloatingActionButton) findViewById(R.id.fab);
          drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          navigationView = (NavigationView) findViewById(R.id.nav_view);
          Top = (LinearLayout) findViewById(R.id.top);
          Pasta = (LinearLayout) findViewById(R.id.pasta);
          Desert = (LinearLayout) findViewById(R.id.desert);
          Rice = (LinearLayout) findViewById(R.id.rice);
         Steak = (LinearLayout) findViewById(R.id.steak);
        Salad = (LinearLayout) findViewById(R.id.salad);
    }

    private void processControllers(){
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        setSupportActionBar(toolbar);
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new MyRecognizerListener());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Top.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("main","work for rand");
                Intent detail_intent = new Intent("com.example.rach.recipe.rand");
                startActivityForResult(detail_intent, 0);
            }
        });

        Pasta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("main","work for pasta");
                Intent list_intent = new Intent();
                list_intent.setAction("com.example.rach.recipe.pasta");
                startActivity(list_intent);
            }
        });

        Desert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("main","work for pasta");
                Intent list_intent = new Intent();
                list_intent.setAction("com.example.rach.recipe.desert");
                startActivity(list_intent);
            }
        });

        Rice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("main","work for pasta");
                Intent list_intent = new Intent();
                list_intent.setAction("com.example.rach.recipe.rice");
                startActivity(list_intent);
            }
        });

        Steak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("main","work for pasta");
                Intent list_intent = new Intent();
                list_intent.setAction("com.example.rach.recipe.steak");
                startActivity(list_intent);
            }
        });

        Salad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("main","work for pasta");
                Intent list_intent = new Intent();
                list_intent.setAction("com.example.rach.recipe.salad");
                startActivity(list_intent);
            }
        });

//        recognizer.startListening(intent);
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recognizer.startListening(intent);
//            }
//        });

//        btnDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //透過 Intent 的方式開啟內建的語音辨識 Activity...
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字
//
//                startActivityForResult(intent, 1);
//            }
//        });
    }


    private class MyRecognizerListener implements RecognitionListener {

        @Override
        public void onResults(Bundle results) {
            List<String> resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            StringBuffer sb = new StringBuffer();
            for(String res: resList) {
                sb.append(res);
                break;
            }
            Log.d("RECOGNIZER", sb.toString());
            if(sb.toString().equals("下一步")){
                Log.d("RECOGNIZER", "那我就下一步");
            }
            textView2.setText(sb.toString());
            Log.d("RECOGNIZER", "onResults: " + sb.toString());
        }

        @Override
        public void onError(int error) {
            Log.d("RECOGNIZER", "Error Code: " + error);
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d("RECOGNIZER", "ready");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("RECOGNIZER", "beginning");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("RECOGNIZER", "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {

            Log.d("RECOGNIZER", "onEndOfSpeech");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Log.d("RECOGNIZER","done");
                    recognizer.startListening(intent);
                }
            }, 1000);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d("RECOGNIZER", "onPartialResults" + partialResults.toString());
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d("RECOGNIZER", "onPartialResults" + params.toString());
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuSearchItem = menu.findItem(R.id.my_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.my_search).getActionView();
//
//        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        // 這邊讓icon可以還原到搜尋的icon
        searchView.setIconifiedByDefault(true);
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

        if (id == R.id.nav_search) {
            Log.d("search","work");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
