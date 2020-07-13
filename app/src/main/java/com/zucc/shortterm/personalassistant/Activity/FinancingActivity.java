package com.zucc.shortterm.personalassistant.Activity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.zucc.shortterm.personalassistant.Bean.Product;
import com.zucc.shortterm.personalassistant.R;
import com.zucc.shortterm.personalassistant.Tools.ProductItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FinancingActivity extends AppCompatActivity {

    private ListView productlistview;
    private ProductItemAdapter productItemAdapter;
    private ImageButton back;
    private List<Product> productList;

    private Handler handler = new Handler(){
      public void handleMeg(android.os.Message msg){
          super.handleMessage(msg);
          if(msg.what == 1){
              System.out.println("new-----------");
              productItemAdapter = new ProductItemAdapter(getApplicationContext(),productList);
              productlistview.setAdapter(productItemAdapter);

          }

      }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financing);

        back = findViewById(R.id.finance_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinancingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        productlistview = findViewById(R.id.product_list);
        productList = new ArrayList<>();

        mythread();

    }

    public void mythread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10,TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .build();
                    final Request request = new Request.Builder()
                            .removeHeader("encryptValue")
                            .addHeader("Accept-Encoding","identify")
                            .url("http://172.20.10.3:8181/product")
                            .get()
                            .addHeader("Connection","close")
                            .build();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    Log.d("msg",response.body().toString());
                    String res = response.body().string();
                    try{
                        JSONArray jsonArray = new JSONArray(res);
                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product product = new Product(jsonObject.getInt("id"),jsonObject.getString("companyName"),
                                    jsonObject.getString("rate"),jsonObject.getInt("term"),jsonObject.getString("des"),
                                    jsonObject.getInt("startPoint"),jsonObject.getString("risk"));
                            productList.add(product);
                        }

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productItemAdapter = new ProductItemAdapter(getApplicationContext(),productList);
                            productlistview.setAdapter(productItemAdapter);
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }



            }
        }).start();

    }
}
