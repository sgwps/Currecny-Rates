package com.example.currencyrates57;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.example.currencyrates57.Request.*;


class Parsing extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = (new URI(
                    params[0])).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            JsonParser jp = new JsonParser();
            InputStreamReader isr = new InputStreamReader((InputStream) connection.getContent());
            JsonElement root = jp.parse(isr);
            isr.close();
            set_json(root.getAsJsonObject());
            Log.d("check46", String.valueOf(root));
            connection.disconnect();
            return "";

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
    }
}

