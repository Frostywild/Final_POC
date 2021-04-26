package com.example.Z23354937;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    RequestQueue queue;
    EditText searchText;
    public static JSONObject savedResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        searchText = findViewById(R.id.searchText);
    }

    public void searchButton(View view){

        String searchTerm = searchText.getText().toString();


        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="+searchTerm+"&apikey="+R.string.api_key;

        System.out.print("Using url: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        savedResponse = response;
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(" We have a Problem");
                    }

                });
        queue.add(jsonObjectRequest);
    }

    public void returnToMainMenu(View view){
        Intent intent;

        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}