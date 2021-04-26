package com.cop4655.Z23354937;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StockActivity extends AppCompatActivity {

    ListView listView;
    List<String> list;
    List<String> stockListArray;
    List<String> favoriteListArray;
    RequestQueue queue;
    JSONObject savedResponse;
    String stockSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        getStockDatabase();


        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        listView = (ListView) findViewById(R.id.stock_list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("DOES this work");

                String firstWord = list.get(position);
                if(firstWord.contains(" ")){
                    firstWord= firstWord.substring(0, firstWord.indexOf(" "));
                    System.out.println("The Code is "+firstWord);
                    stockSymbol = firstWord;

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(StockActivity.this);
                builder.setTitle("Options");
                builder.setMessage("Favorite/Delete/Nothing!");
                builder.setPositiveButton("Favorite Toggle",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        favoriteSelection();
                    } });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //...
                        deleteSelection();

                    }});

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //...
                        System.out.println("Nothing");
                    }});

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    //Sends the JSON request and adds it to the request Queue
    public void getStockInfo(String Symbol, boolean favorites) {

        String url= "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+Symbol+"&apikey=1JLPD9J3SUI1HMX9";
        System.out.print("Using url: ");


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        savedResponse = response;
                        setStockList(favorites);
                        System.out.println(savedResponse.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(" We have a Problem");
                    }

                });
        queue.add(jsonObjectRequest);
    }


    //Sets the information from the JSON Object into the list view
    public void setStockList(boolean favorites) {
        JSONObject stockInfo = null;
        try {
            System.out.println(savedResponse.toString());
            stockInfo = savedResponse.getJSONObject("Global Quote");
        } catch (JSONException e) {
            System.out.println(" We have a problem with JSON");
            e.printStackTrace();
        }

        //listView = (ListView) findViewById(R.id.stock_list_view);

        //list = new ArrayList<String>();
        String Favorite="";
        if (favorites){
            Favorite = "Favorite";
        }


        //adds the stock to the list
        if (stockInfo != null) {
            try {
                list.add(stockInfo.getString("01. symbol") +"   "+ stockInfo.getString("02. open")+" "+Favorite);
                Log.d("list", list.toString());
            } catch (JSONException e) {
                System.out.println(" We have a problem with Stock Data");
                e.printStackTrace();
            }


        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);

        listView.setAdapter(arrayAdapter);
    }

    public void getStockDatabase() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference stockRef = rootRef.child("stocks");
        System.out.println("Can we get here");

        //stockListArray = new ArrayList<String>();
        //favoriteListArray = new ArrayList<String>();

        // Read from the database
        stockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stockListArray = new ArrayList<String>();
                favoriteListArray = new ArrayList<String>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //System.out.println("Value is: " + value);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String tempValue = postSnapshot.child("stockCode").getValue(String.class);

                    System.out.println("The Value is "+tempValue);

                    if(postSnapshot.child("favorite").getValue(boolean.class) == true){
                        favoriteListArray.add(tempValue);
                        System.out.println("Is a favorite");
                    }else {
                        stockListArray.add(tempValue);
                        System.out.println("Is not favorite");
                    }

                }
                setDatabaseInformation();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value.");
            }
        });
    }


    public void returnToMainMenu(View view){
        Intent intent;

        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void setDatabaseInformation(){
        list = new ArrayList<String>();
        for(int i=0;i<stockListArray.size();i++){
            getStockInfo(stockListArray.get(i),false);
        }
        for(int i=0; i<favoriteListArray.size();i++) {
            getStockInfo(favoriteListArray.get(i),true);
        }
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);

        //listView.setAdapter(arrayAdapter);
    }

    //deletes the chosen stock from the database
    public void deleteSelection(){
        System.out.println("Deleted");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        System.out.println(stockSymbol);
        Query deleteQuery = ref.child("stocks").orderByChild("stockCode").equalTo(stockSymbol);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot deleteSnapshot: dataSnapshot.getChildren()) {
                    deleteSnapshot.getRef().removeValue();
                    System.out.println(stockSymbol);
                    System.out.println("Did we get here");

                }
                getStockDatabase();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
                System.out.println("We have a deletion error");
            }
        });
    }

    //Toggles the Favorite value of the chosen stock
    public void favoriteSelection(){
        System.out.println("Favorited");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        System.out.println(stockSymbol);
        Query favoriteQuery = ref.child("stocks").orderByChild("stockCode").equalTo(stockSymbol);

        favoriteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favoriteSnapshot: dataSnapshot.getChildren()) {
                    favoriteSnapshot.getRef().child("favorite").setValue(!favoriteSnapshot.child("favorite").getValue(boolean.class));
                }
                getStockDatabase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}