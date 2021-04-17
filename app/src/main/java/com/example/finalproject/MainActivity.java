package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button logInPage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RequestQueue queue;
    MainActivity context;
    public static JSONObject savedResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.my_toolbar);


        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        logInPage = findViewById(R.id.loginPage);


        logInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_stock:
                if (queue == null) {
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
                                Intent intent = new Intent(getApplicationContext(), StockActivity.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(" We have a Problem");
                            }

                        });
                queue.add(jsonObjectRequest);

                break;
            case R.id.nav_login:
                Intent intent2 = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent2);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}