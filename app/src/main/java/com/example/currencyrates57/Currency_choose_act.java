package com.example.currencyrates57;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

import static java.lang.Integer.parseInt;
import static com.example.currencyrates57.Request.*;


public class Currency_choose_act extends AppCompatActivity {

    RecyclerView list;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);



        bundle = getIntent().getExtras();
        int current = Integer.parseInt(bundle.getString("curr"));
        setContentView(R.layout.currency_choose_act);

        /*TextView t1 = findViewById(R.id.curr_now);
        TextView t2 = findViewById(R.id.curr_full_now);
        ImageView i1 = findViewById(R.id.flag_current);

        t1.setText(get_info.get_curr_inf(current));
        t2.setText(get_info.get_inf("NAME_EN", current));
        i1.setImageResource(flags.get_flag(current));*/

        list = findViewById(R.id.currency_list1);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        list.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        CurrencyAdapter curr_list = new CurrencyAdapter(this);
        list.setAdapter(curr_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        TextView tool;
        tool = findViewById(R.id.tool);
        tool.setText("Choose Currency");


    }

    public void goback(int position){
        LoadingDialog ld = new LoadingDialog(this);
        ld.startLoadingDialog();
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (bundle.getString("button").equals("from")){
                        set_curr_from_r(position);
                    }
                    else{
                        set_curr_to_r(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.dismissDialog();
                        finish();
                    }
                });

            }
        }).start();

    }
}