package com.example.currencyrates57;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.example.currencyrates57.Request.get_json_s;
import static com.example.currencyrates57.get_info.get_curr_inf;

public class Savings extends Fragment {

    private static ArrayList<JsonObject> savings_json = new ArrayList<JsonObject>();



    RecyclerView list_;
    SavingsAdapter adapter;

    public Savings() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(getContext().getFilesDir(), "savings_f.txt");
                String t = "";
                try {
                    FileReader fr = new FileReader(file);
                    int i;
                    while((i = fr.read()) != -1){
                        t += String.valueOf((char) i);
                    }
                    fr.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!t.isEmpty()) {
                    JsonElement jp = JsonParser.parseString(t);
                    JsonObject savings_json1 = jp.getAsJsonObject();
                    savings_json = new ArrayList<JsonObject>();
                    for(int i = 0; i < savings_json1.keySet().size(); i++) {
                        savings_json.add(savings_json1.get(String.valueOf(i)).getAsJsonObject());
                    }
                    SavingsData.set_main_s(savings_json);
                }


            }

        }).start();

    }

    public void renew_savings(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list_.setLayoutManager(linearLayoutManager);
        adapter = new SavingsAdapter(getContext());
        ItemTouchHelper.Callback callback = new SavingsItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(list_);
        list_.setAdapter(adapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_savings, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton plus_1 = view.findViewById(R.id.plus);
        plus_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check9", String.valueOf(get_json_s()));

                SavingsData.add(get_json_s());
                adapter.notifyDataSetChanged();
                SavingsData.save(getContext());
            }
        });

        list_ = view.findViewById(R.id.list_s);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        list_.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        LoadingDialog ld = new LoadingDialog(getActivity());
        ld.startLoadingDialog();
        renew_savings();
        ld.dismissDialog();

    }

    @Override
    public void onPause() {
        super.onPause();
        SavingsData.save(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        SavingsData.save(getContext());
    }
}