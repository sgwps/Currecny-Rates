package com.example.currencyrates57;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;


public class get_info {

    private static JsonObject data_curr = new JsonObject();

    private  static String[] currs = {"AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP",
    "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP",
    "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"};


    public static void startJson(Context ct) throws IOException {
        String json = null;
        InputStream is = ct.getAssets().open("info.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer);
        JsonElement jp = JsonParser.parseString(json);
        data_curr = jp.getAsJsonObject();

    }

    public static String get_curr_inf(int a){
        return currs[a];
    }

    public static String get_inf(String func, int curr){
        JsonObject j = (JsonObject) data_curr.getAsJsonObject(currs[curr]);
        return j.get(func).getAsString();
    }


}

