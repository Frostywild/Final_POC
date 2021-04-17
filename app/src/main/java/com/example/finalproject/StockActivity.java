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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

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
                System.out.println(stockInfo.getString("01. symbol"));
                list.add(stockInfo.getString("02. open"));
                System.out.println(stockInfo.getString("02. open"));
            } catch (JSONException e) {
                System.out.println(" We have a problem with Weather Data");
                e.printStackTrace();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

            listView.setAdapter(arrayAdapter);


        }




    }
}