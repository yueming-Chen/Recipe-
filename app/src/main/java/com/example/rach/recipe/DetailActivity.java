package com.example.rach.recipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

public class DetailActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private Item item;
    private TextView title_text,tv;
    private ImageView iv;
    private LinearLayout layout ;
    private int change=0;
    final Handler handler = new Handler();
    private Handler mHandler;
    private String url,result,id;
    private String[] imgTok,descriptionTok;
    private Drawable imgDrawble[];
    private int[] location=new int[2];
    protected static final int REFRESH_DATA = 0x00000001,SET_VIEW=0x00000002;
    private LinearLayout.LayoutParams paramsHalfWidth1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        processView();
        processControllers();
        Intent intent = getIntent();
        paramsHalfWidth1  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        paramsHalfWidth1.setMargins(0,30,0,30);
        String action = intent.getAction();
        if(action.equals("com.example.rach.recipe.rand")){
            //選擇random Api
            url = "https://recipevoicebe.herokuapp.com/Api/getRandom";
        }else {
            //一班狀況下
            Log.d("work","work");
            item = (Item) intent.getExtras().getSerializable("com.example.rach.recipe.ITEM");
            id = item.getId();
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeByUUID/" + id;
        }


//        handler.post(mutiThread);
        Thread thread = new Thread(mutiThread);
        thread.start();

        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                Log.d("handleMessage","chane =" + change);
//                if(change == 0){
                    switch (msg.what)
                    {
                        case REFRESH_DATA:
                            Log.d("REFRESH_DATA","work");
                            if(imgTok != null){
//                                handler.post(imageThread);
                                Thread thread2 = new Thread(imageThread);
                                thread2.start();
                            }
                            break;
                        case SET_VIEW:
                            //設定畫面上元件

                            for(int index = 0;index<descriptionTok.length;index++){
                                iv.setImageDrawable(imgDrawble[index]);
                                iv.setAdjustViewBounds(true);
                                iv.setLayoutParams(paramsHalfWidth1);
                                layout.addView(iv);
                                Clear("iv");
                                String des_data = descriptionTok[index];
                                des_data = des_data.substring(des_data.indexOf('"')+1);
                                if(index == (descriptionTok.length-1)) des_data = des_data.substring(0,des_data.lastIndexOf('"'));
                                tv.setText(des_data.replace("\\n", "\n"));
                                tv.setTextSize(20.0f);
                                layout.addView(tv);
                                Clear("tv");
                            }
                            iv.setImageDrawable(imgDrawble[imgTok.length-1]);
                            layout.addView(iv);
                            Clear("iv");
                            break;
                    }
//                }
                change++;
            }
        };

        layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int count = layout.getChildCount();
                        for(int i=0; i<count; i++) {
                            View v = layout.getChildAt(i);
                            if(v instanceof ImageView) {
                                v.getLocationInWindow(location);
                                Log.d("location","location is " + location[0] + "  " + location[1]);
                            }
                        }
                        //移動
                        //focusOnView(location[1]);
                    }
                }
        );
    }

    private void processView(){
        title_text = (TextView)findViewById(R.id.title_text);
        layout = (LinearLayout)findViewById(R.id.main);
        scrollView =(ScrollView) findViewById(R.id.scroll_detail);
        tv = new TextView(this);
        iv = new ImageView(this);
    }

    private void Clear(String key){
        if(key == "tv") tv = new TextView(this);
        else iv = new ImageView(this);
    }

    private void  processControllers(){

    }

    final Runnable mutiThread = new Runnable(){
        public void run(){
            // 運行網路連線的程式
            result = getHtmlByGet(url);
            mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }
    };

    final Runnable imageThread = new Runnable(){
        public void run(){
            // 運行網路連線的程式
            imgDrawble = new Drawable[imgTok.length];
            int count = 0;
            for(String token:imgTok){
                imgDrawble[count] = loadImageFromURL(token);
                count++;
                Log.d("loadImageFromURL","work");
            }
            mHandler.obtainMessage(SET_VIEW, result).sendToTarget();
        }
    };

    private String getHtmlByGet(String _url){
        //get API的資料
        String result = "";
        HttpClient client = new DefaultHttpClient();
        try {

            HttpGet get = new HttpGet(_url);
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);

                JSONObject results = new JSONObject(new JSONObject(result).getString("results"));
                Log.d("RESULTS",(new JSONObject(result).getString("results")));
                String img = results.getString("photo");
                String des = results.getString("description");
                img = img.substring(img.indexOf('[')+1,img.indexOf(']'));
                des = des.substring(des.indexOf('[')+1,des.indexOf(']'));
                imgTok = img.split(",");
                descriptionTok = des.split("\",");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;

    }

    private Drawable loadImageFromURL(String url){
        //解析URL成DRAWBL並且回傳
        url = url.substring(url.indexOf('"')+1,url.lastIndexOf('"'));
        Log.d("url",url);
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src");
            return draw;
        }catch (Exception e) {
            //TODO handle error
            Log.i("loadingImg", e.toString());
            return null;
        }
    }

    private final void focusOnView(final int y){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                final int val = y;
                scrollView.scrollTo(0, val);
            }
        });
    }

}
