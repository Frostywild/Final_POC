package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StockActivity extends AppCompatActivity {

    RequestQueue queue;

    ListView listView;
    List list;
    public static JSONObject savedResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        /*if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=GME&apikey=1JLPD9J3SUI1HMX9";

        System.out.print("Using url: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        savedResponse = response;
                        //Intent intent = new Intent(context, StockActivity.class);
                        //startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(" We have a Problem");
                    }

                });
        queue.add(jsonObjectRequest);*/

        JSONObject tempInfo = MainActivity.savedResponse;

        JSONObject stockInfo = null;
        try {
            System.out.println(tempInfo.toString());
            stockInfo = tempInfo.getJSONObject("Global Quote");
        }catch (JSONException e){
            System.out.println(" We have a problem with JSON");
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.list_view);

        list = new ArrayList<String>();

        if(stockInfo!=null){
            try {
                list.add(stockInfo.getString("01. symbol"));
                list.add(stockInfo.getString("02. open"));
            } catch (JSONException e) {
                System.out.println(" We have a problem with Weather Data");
                e.printStackTrace();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

            listView.setAdapter(arrayAdapter);


        }




    }
}