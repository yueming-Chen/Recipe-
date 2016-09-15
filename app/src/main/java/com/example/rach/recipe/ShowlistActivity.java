package com.example.rach.recipe;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.util.ArrayList;
import java.util.List;

public class ShowlistActivity extends AppCompatActivity {
    private ListView listView;
    private TextView textView;
    private ItemAdapter itemAdapter;
    private List<Item> items;
    private String url,result;
    final Handler handler = new Handler();
    private Handler mHandler;
    protected static final int REFRESH_DATA = 0x00000001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist);

        Intent intent = getIntent();
        String action = intent.getAction();
        if(action.equals("com.example.rach.recipe.pasta")){
            //選擇義大利麵
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeListByName/義大利麵";
        }else if(action.equals("com.example.rach.recipe.desert")){
            //選擇點心
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeListByName/點心";
        }else if(action.equals("com.example.rach.recipe.rice")){
            //選擇米飯
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeListByName/飯";
        }else if(action.equals("com.example.rach.recipe.steak")){
            //選擇牛排料理
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeListByName/牛排";
        }else if(action.equals("com.example.rach.recipe.salad")){
            //選擇沙拉料理
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeListByName/沙拉";
        }else {
            String query = intent.getStringExtra(SearchManager.QUERY);
            url = "https://recipevoicebe.herokuapp.com/Api/getRecipeListByName/" + query;
        }
        processView();
        processControllers();

        items = new ArrayList<Item>();
        itemAdapter = new ItemAdapter(this, R.layout.singleitem, items);
        listView.setAdapter(itemAdapter);
        handler.post(mutiThread);
        Thread thread = new Thread(mutiThread);
        thread.start();

        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case REFRESH_DATA:
                        itemAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
    }



    private void processView(){
        listView = (ListView) findViewById(R.id.list_item);
        textView = (TextView) findViewById(R.id.test);
    }

    private void processControllers(){
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 讀取選擇的記事物件
                Item item = itemAdapter.getItem(position);

                Intent intent = new Intent();
                intent.setClass(ShowlistActivity.this,DetailActivity.class);

                // 設定記事編號與記事物件
                intent.putExtra("position", position);
                intent.putExtra("com.example.rach.recipe.ITEM", item);
                intent.setAction("com.example.rach.recipe.ITEM");
                startActivityForResult(intent, 1);
            }
        };

        listView.setOnItemClickListener(itemListener);
    }

    final Runnable mutiThread = new Runnable(){
        public void run(){
            // 運行網路連線的程式

            result = getHtmlByGet(url);
            mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }
    };

    private String getHtmlByGet(String _url){

        String result = "";
        HttpClient client = new DefaultHttpClient();
        try {

            HttpGet get = new HttpGet(_url);
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
                JSONArray json =new JSONArray(new JSONObject(result).getString("results"));
                int length  = json.length();
                for (int i = 0;i<length;i++) {
                    String stringId = new JSONArray(new JSONObject(result).getString("results")).getJSONObject(i).getString("UUID");
                    String stringTitle = new JSONArray(new JSONObject(result).getString("results")).getJSONObject(i).getString("name");
                    String stringAuthor = new JSONArray(new JSONObject(result).getString("results")).getJSONObject(i).getString("author");
                    items.add(new Item(stringId, stringAuthor, stringTitle));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;

    }


}
