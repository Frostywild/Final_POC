package com.cop4655.Z23354937;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {


    ListView listView;
    List<String> list;
    public  String passedAddition;
    public static class Stock {

        public boolean favorite;
        public String stockCode;


        public Stock(boolean favorite,String stockCode) {

            this.favorite = favorite;
            this.stockCode = stockCode;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Gets the JSON information from Search Activity
        JSONObject tempInfo = SearchActivity.savedResponse;

        //Gets the JSON array from the
        JSONArray searchInfo = null;
        try {
            System.out.println(tempInfo.toString());
            searchInfo = tempInfo.getJSONArray("bestMatches");
        }catch (JSONException e){
            System.out.println(" We have a problem with Search JSON");
            e.printStackTrace();
        }

        listView = (ListView)findViewById(R.id.search_list_view);

        String tempSymbol=null;
        list = new ArrayList<String>();

        //Adds all of the results from the search Query to the Array list that will be added to the list view
        if(searchInfo!=null){
            for(int i=0;i<searchInfo.length();i++) {
                try {
                    JSONObject searchItem = searchInfo.getJSONObject(i);
                    System.out.println(searchItem);
                    tempSymbol = searchItem.getString("1. symbol")+ "   "+searchItem.get("2. name");

                    list.add(tempSymbol);
                    Log.d("list", list.toString());

                } catch (JSONException e) {
                    System.out.println(" We have a problem with Search Stock Data");
                    e.printStackTrace();
                }
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,list);

        listView.setAdapter(arrayAdapter);

        //Sets the on item click listener for the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if(signInAccount!=null){
                    String firstWord = list.get(position);
                    if(firstWord.contains(" ")){
                        firstWord= firstWord.substring(0, firstWord.indexOf(" "));
                        System.out.println("The Code is "+firstWord);
                        String stockSymbol = firstWord;

                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference myRef = database.getReference("stocks");
                        DatabaseReference pushRef = myRef.push();
                        pushRef.setValue(new Stock(false, stockSymbol));
                        Intent intent = new Intent(getApplicationContext(), StockActivity.class);
                        startActivity(intent);

                    }
                }
                System.out.println(list.get(position));



                /*...*/
            }
        });
    }

    public void returnToMainMenu(View view){
        Intent intent;

        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}