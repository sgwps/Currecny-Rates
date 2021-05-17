package com.example.currencyrates57;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SavingsData {
    private static ArrayList<JsonObject> main = new ArrayList<JsonObject>();

    public static ArrayList<JsonObject> get_main_s(){return main;}
    public static JsonObject get_main_s(int p){return main.get(p);}


    public static void set_main_s(ArrayList<JsonObject> m) {main = m;}

    public static void change(int p, JsonObject h){
        main.set(p, h);
    }

    public static void change1(int p, double f){
        Log.d("check70", String.valueOf(f));
        main.get(p).get("rates").getAsJsonObject().addProperty(String.valueOf(Request.get_current_r()), f);
    }
    public static void add(JsonObject h){
        main.add(h);
    }


    public static void onItemMove_(int fromPosition, int toPosition){
        Log.d("check60", String.valueOf(main.get(fromPosition)));
        JsonObject tmp = main.get(fromPosition);
        main.remove(fromPosition);
        main.add(toPosition, tmp);
        Log.d("check61", String.valueOf(main.get(toPosition)));

    }

    public static void onItemSwiped_(int position){
        main.remove(position);
    }

    public static void save(Context ct) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(ct.getFilesDir(), "savings_f.txt");
                try {
                    FileWriter fw = new FileWriter(file);
                    JsonObject savings_json1 = new JsonObject();
                    for(int i = 0; i < SavingsData.get_main_s().size(); i++) {
                        savings_json1.add(String.valueOf(i), main.get(i));
                    }
                    fw.write(savings_json1.toString());
                    Log.d("check65", savings_json1.toString());
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
