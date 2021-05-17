package com.example.currencyrates57;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.io.IOException;

import static com.example.currencyrates57.Request.*;
import static com.example.currencyrates57.get_info.startJson;

public class MainActivity extends AppCompatActivity {

    Conversion conversion_1 = null;
    Graph graph_1 = null;
    Dynamic dynamic_1 = null;
    Savings savings_1 = null;
    Fragment active = null;
    TextView tool;
    FragmentManager fm = getSupportFragmentManager();
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_conversion:
                            fm.beginTransaction().hide(active).show(conversion_1).commit();
                            active.onPause();
                            active = conversion_1;
                            active.onResume();
                            tool.setText("Convert");
                            break;
                        case R.id.navigation_graph:
                            fm.beginTransaction().hide(active).show(graph_1).commit();
                            active.onPause();
                            active = graph_1;
                            active.onResume();
                            tool.setText("Charts");

                            break;
                        case R.id.navigation_dynamic:
                            fm.beginTransaction().hide(active).show(dynamic_1).commit();
                            active.onPause();
                            active = dynamic_1;
                            tool.setText("Dynamic");

                            active.onResume();

                            break;
                        case R.id.navigation_savings:
                            fm.beginTransaction().hide(active).show(savings_1).commit();
                            active.onPause();
                            active = savings_1;
                            tool.setText("Requests");

                            active.onResume();

                            break;
                    }
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        try {
            startJson(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                start_request();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("check26", "start");
                        conversion_1 = new Conversion();
                        graph_1 = new Graph();
                        dynamic_1 = new Dynamic();
                        savings_1 = new Savings();

                        setContentView(R.layout.activity_main);

                        BottomNavigationView navView = findViewById(R.id.bottom);
                        tool = findViewById(R.id.tool);
                        navView.setOnNavigationItemSelectedListener(navListener);
                        fm.beginTransaction().add(R.id.fragment, graph_1, "g").hide(graph_1).commit();
                        fm.beginTransaction().add(R.id.fragment, dynamic_1, "d").hide(dynamic_1).commit();
                        fm.beginTransaction().add(R.id.fragment, savings_1, "s").hide(savings_1).commit();
                        fm.beginTransaction().add(R.id.fragment, conversion_1, "c").commit();
                        fm.beginTransaction().show(conversion_1);
                        active = conversion_1;

                        tool.setText("Convert");
                    }
                });
            }
        }).start();
    }


    public void choose_currency(View v){
        Intent i = new Intent(this, Currency_choose_act.class);
        Bundle b = new Bundle();
        b.putString("button", String.valueOf(v.getContentDescription()));
        i.putExtra("button", String.valueOf(v.getContentDescription()));
        if (String.valueOf(v.getContentDescription()).equals("from")) i.putExtra("curr", String.valueOf(get_curr_from_r()));
        else i.putExtra("curr", String.valueOf(get_curr_to_r()));
        startActivity(i);
    }

    public void get_savings(int position) {
        LoadingDialog ld = new LoadingDialog(this);
        ld.startLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_request(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.dismissDialog();
                    }
                });
            }
        }).start();
    }
}