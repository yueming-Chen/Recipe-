package com.example.rach.recipe;

import android.app.Activity;
import android.graphics.Color;
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
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    private SpeechRecognizer recognizer;
    private Button btnDialog;
    private Button btnStart;
    private TextView textView;
    private TextView textView2;
    private Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        btnDialog = (Button) this.findViewById(R.id.btn1);
        textView = (TextView) this.findViewById(R.id.tv1);
        btnStart = (Button) this.findViewById(R.id.btnStart);
        textView2 = (TextView) this.findViewById(R.id.textView2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new MyRecognizerListener());
        recognizer.startListening(intent);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizer.startListening(intent);
            }
        });


        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //透過 Intent 的方式開啟內建的語音辨識 Activity...
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

                startActivityForResult(intent, 1);
            }
        });

//        Intent intent = new Intent(MainActivity.this, NickyService.class);
//        startService(intent);
//        Intent intent2 = new Intent(MainActivity.this, MyRecognizerListener.class);
//        startService(intent2);


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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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


    private class MyRecognizerListener implements RecognitionListener {

        @Override
        public void onResults(Bundle results) {
            List<String> resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            StringBuffer sb = new StringBuffer();
            for(String res: resList) {
                sb.append(res + "\n");
                break;
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
