package com.example.currencyrates57;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static com.example.currencyrates57.get_info.get_curr_inf;
import static com.example.currencyrates57.get_info.get_inf;

public class Request {
    private static int curr_from;
    private static int curr_to;
    private static LocalDate begin;
    private static LocalDate end;
    private static LocalDate begin_min;
    private static LocalDate end_max;
    private static LocalDate min_date;
    private static LocalDate current;
    private static double summ = 1;
    private static Map<LocalDate,Double> rates;
    private static JsonObject rates1;

    public static void start_request(){
        curr_from = 0;
        curr_to = 0;
        Calendar tmp = Calendar.getInstance();
        tmp.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (tmp.get(Calendar.HOUR_OF_DAY) == 0 && tmp.get(Calendar.MINUTE) < 5){
            tmp.set(Calendar.DAY_OF_YEAR, tmp.get(Calendar.DAY_OF_YEAR) - 1);
        }
        current = LocalDate.of(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH) + 1, tmp.get(Calendar.DAY_OF_MONTH));
        begin = LocalDate.of(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH) + 1, tmp.get(Calendar.DAY_OF_MONTH));
        end = LocalDate.of(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH) + 1, tmp.get(Calendar.DAY_OF_MONTH));
        begin_min = LocalDate.of(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH) + 1, tmp.get(Calendar.DAY_OF_MONTH));
        end_max = LocalDate.of(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH) + 1, tmp.get(Calendar.DAY_OF_MONTH));
        summ = 1;
        if (LocalDate.parse(get_info.get_inf("MIN_DATE", curr_from)).compareTo(LocalDate.parse(get_inf("MIN_DATE", curr_to))) < 0){
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_to));
        }
        else{
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_from));
        }
        start();
        if (rates.get(current.minusDays(1)) == null){
            for (LocalDate date = get_current_r(); date.isAfter(get_current_r().minusDays(16)); date = date.minusDays(1))
            {
                    rates.put(date, 1.0);
            }
        }
        Log.d("check80", String.valueOf(rates));
    }

    public static void get_request(int position){
        JsonObject savings1 = SavingsData.get_main_s(position);
        curr_from = savings1.get("curr_from").getAsInt();
        curr_to = savings1.get("curr_to").getAsInt();
        begin = LocalDate.parse(savings1.get("begin").getAsString());
        begin_min = LocalDate.parse(savings1.get("begin_min").getAsString());
        rates = new HashMap<LocalDate, Double>();
        for (String s: savings1.get("rates").getAsJsonObject().keySet()){
            rates.put(LocalDate.parse(s), savings1.get("rates").getAsJsonObject().get(s).getAsDouble());
        }
        if (savings1.get("end").getAsString().equals("current")){
            end = LocalDate.of(current.getYear(), current.getMonth(), current.getDayOfMonth());
            end_max = LocalDate.of(current.getYear(), current.getMonth(), current.getDayOfMonth());
            Log.d("check36", savings1.get("end_of_rates").getAsString());

            if ((LocalDate.parse(savings1.get("end_of_rates").getAsString())).compareTo(current) != 0) start(LocalDate.parse(savings1.get("end_of_rates").getAsString()).plusDays(1), current);
            for (LocalDate d = LocalDate.parse(savings1.get("end_of_rates").getAsString()).plusDays(1); d.isBefore(current.plusDays(1)); d = d.plusDays(1)){
                savings1.get("rates").getAsJsonObject().addProperty(String.valueOf(d), get_rates_r(d));
            }
            savings1.remove("end_of_rates");
            savings1.addProperty("end_of_rates", String.valueOf(current));
            SavingsData.change(position, savings1);
        }
        else
        {
            end = LocalDate.parse(savings1.get("end").getAsString());
            end_max = LocalDate.parse(savings1.get("end_max").getAsString());
            start(current.minusDays(15), current);
        }
        if (LocalDate.parse(get_info.get_inf("MIN_DATE", curr_from)).compareTo(LocalDate.parse(get_inf("MIN_DATE", curr_to))) < 0){
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_to));
        }
        else{
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_from));
        }
        summ = savings1.get("summ").getAsDouble();
    }

    public static void set_curr_from_r(int t){
        curr_from = t;
        if (LocalDate.parse(get_info.get_inf("MIN_DATE", curr_from)).compareTo(LocalDate.parse(get_inf("MIN_DATE", curr_to))) < 0){
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_to));
        }
        else{
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_from));
        }
        if (begin.compareTo(min_date) < 0){
            begin = LocalDate.of(min_date.getYear(), min_date.getMonth(), min_date.getDayOfMonth());
        }
        if (begin.compareTo(end) > 0){
            end = LocalDate.of(min_date.getYear(), min_date.getMonth(), min_date.getDayOfMonth());
        }
        begin_min = LocalDate.of(begin.getYear(), begin.getMonth(), begin.getDayOfMonth());
        end_max = LocalDate.of(end.getYear(), end.getMonth(), end.getDayOfMonth());
        start();
    }

    public static void set_curr_to_r(int t){
        curr_to = t;
        if (LocalDate.parse(get_info.get_inf("MIN_DATE", curr_from)).compareTo(LocalDate.parse(get_inf("MIN_DATE", curr_to))) < 0){
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_to));
        }
        else{
            min_date = LocalDate.parse(get_inf("MIN_DATE", curr_from));
        }
        if (begin.compareTo(min_date) < 0){
            begin = LocalDate.of(min_date.getYear(), min_date.getMonth(), min_date.getDayOfMonth());
        }
        if (begin.compareTo(end) > 0){
            end = LocalDate.of(min_date.getYear(), min_date.getMonth(), min_date.getDayOfMonth());
        }
        begin_min = LocalDate.of(begin.getYear(), begin.getMonth(), begin.getDayOfMonth());
        end_max = LocalDate.of(end.getYear(), end.getMonth(), end.getDayOfMonth());
        start();

    }

    public static int get_curr_from_r(){
        return curr_from;
    }

    public static int get_curr_to_r(){
        return curr_to;
    }

    public static void set_begin_r(int year, int monthOfYear, int dayOfMonth){
        if (LocalDate.of(year, monthOfYear, dayOfMonth).compareTo(begin_min) < 0) {
            start(LocalDate.of(year, monthOfYear, dayOfMonth), begin);
            begin_min = LocalDate.of(year, monthOfYear, dayOfMonth);
        }
        begin = LocalDate.of(year, monthOfYear, dayOfMonth);

    }

    public static void set_end_r(int year, int monthOfYear, int dayOfMonth){
        if (LocalDate.of(year, monthOfYear, dayOfMonth).compareTo(end_max) > 0) {
            start(end, LocalDate.of(year, monthOfYear, dayOfMonth));
            end_max = LocalDate.of(year, monthOfYear, dayOfMonth);

        }
        end = LocalDate.of(year, monthOfYear, dayOfMonth);
    }

    public static LocalDate get_current_r(){
        return current;
    }

    public static LocalDate get_begin_r(){
        return begin;
    }

    public static LocalDate get_end_r(){
        return end;
    }

    public static LocalDate get_min_date_r(){
        return min_date;
    }

    public static void set_summ_r(double d){
        summ = d;
    }

    public static double get_summ_r(){
        return summ;
    }

    public static void swap(){
        int tmp = curr_to;
        curr_to = curr_from;
        curr_from = tmp;
        begin_min = LocalDate.of(begin.getYear(), begin.getMonth(), begin.getDayOfMonth());
        end_max = LocalDate.of(end.getYear(), end.getMonth(), end.getDayOfMonth());
        start();
    }

    public static String get_begin_txt(){
        int day = begin.getDayOfMonth();
        String month = begin.getMonth().toString();
        int year = begin.getYear();
        return String.valueOf(day) + " " + month + " " + String.valueOf(year);
    }

    public static String get_end_txt(){
        int day = end.getDayOfMonth();
        String month = end.getMonth().toString();
        int year = end.getYear();
        return String.valueOf(day) + " " + month + " " + String.valueOf(year);
    }

    public static Double get_rates_r(LocalDate ld){
        return rates.get(ld);
    }

    public static Map<LocalDate, Double> get_rates_r(){
        return rates;
    }

    public static void set_json(JsonObject jo){
        rates1 = jo.getAsJsonObject("rates");
    }
    public static void set_json1(JsonObject jo){
        rates1 = jo;
    }


    public static JsonObject get_json(){
        return rates1;
    }

    public static JsonObject get_json_s(){

        JsonObject savings_json = new JsonObject();
        savings_json.addProperty("curr_from", curr_from);
        savings_json.addProperty("curr_to", curr_to);
        savings_json.addProperty("begin", String.valueOf(begin));
        if (end.compareTo(current) != 0) savings_json.addProperty("end", String.valueOf(end));
        else {
            savings_json.addProperty("end", "current");
            savings_json.addProperty("end_of_rates", String.valueOf(end));
        }
        savings_json.addProperty("begin_min", String.valueOf(begin_min));
        savings_json.addProperty("end_max", String.valueOf(end_max));
        savings_json.addProperty("summ", summ);
        JsonObject rates_1 = new JsonObject();
        for (LocalDate date : rates.keySet()){
            rates_1.addProperty(String.valueOf(date), get_rates_r(date));
        }
        savings_json.add("rates", rates_1);
        Log.d("json", String.valueOf(savings_json));
        return savings_json;
    }

    public static String getURL(LocalDate date) {
        String root = "https://api.exchangerate.host/timeseries?";
        String start_at = "start_date=" + date.toString();
        String end_at = null;
        if (date.plusDays(364).isBefore(get_end_r()))  {
            end_at = "end_date=" + date.plusDays(364).toString();
        }
        else{
            end_at = "end_date=" + get_end_r().toString();
        }
        String base = "base=" + get_curr_inf(get_curr_from_r());
        String symbols = "symbols=" + get_curr_inf(get_curr_to_r());
        String URL_ = root + start_at + '&' + end_at + '&' + symbols + '&' + base;
        Log.d("g", URL_);
        return URL_;
    }

    public static String getURL(LocalDate date, LocalDate ld2) {
        String root = "https://api.exchangerate.host/timeseries?";
        String start_at = "start_date=" + date.toString();
        String end_at = null;
        if (date.plusDays(364).isBefore(ld2))  {
            end_at = "end_date=" + date.plusDays(364).toString();
        }
        else{
            end_at = "end_date=" + ld2.toString();
        }
        String base = "base=" + get_curr_inf(get_curr_from_r());
        String symbols = "symbols=" + get_curr_inf(get_curr_to_r());
        String URL_ = root + start_at + '&' + end_at + '&' + symbols + '&' + base;
        Log.d("g", URL_);
        return URL_;
    }

    public static String getURL2() {
        String root = "https://api.exchangerate.host/timeseries?";
        String start_at = "start_date=" + get_current_r().minusDays(15).toString();
        String end_at = "end_date=" + get_current_r().toString();
        String base = "base=" + get_curr_inf(get_curr_from_r());
        String symbols = "symbols=" + get_curr_inf(get_curr_to_r());
        String URL_ = root + start_at + '&' + end_at + '&' + symbols + '&' + base;
        Log.d("g", URL_);
        return URL_;
    }

    public static void start() {
        String res;
        rates = new HashMap<LocalDate, Double>();

        for (LocalDate date = get_begin_r(); date.isBefore(get_end_r().plusDays(1)); date = date.plusDays(0)) {
            try {
                res = new Parsing().execute(getURL(date)).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;
            if (get_json() == null){
                return;
            }
            while (date.isBefore(get_end_r().plusDays(1)) && i < 363) {
                if (get_json().keySet().contains(date.toString())) {
                    String rate = String.valueOf(get_json().getAsJsonObject(date.toString()).get(get_curr_inf(get_curr_to_r())));
                    Double rate_ = Double.parseDouble(rate);
                    rates.put(date, rate_);
                }
                i++;
                date = date.plusDays(1);
            }
        }

        try {
            res = new Parsing().execute(getURL2()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (get_json() == null){
            return;
        }

        for (LocalDate date = get_current_r(); date.isAfter(get_current_r().minusDays(16)); date = date.minusDays(1))
        {
            if (get_json().keySet().contains(date.toString())) {
                String rate = String.valueOf(get_json().getAsJsonObject(date.toString()).get(get_curr_inf(get_curr_to_r())));
                Double rate_ = Double.parseDouble(rate);
                rates.put(date, rate_);
            }
        }
    }

    public static void start(LocalDate ld1, LocalDate ld2)  {
        String res;
        for (LocalDate date = ld1; date.isBefore(ld2.plusDays(1)); date = date.plusDays(0))
        {
            try {
                res = new Parsing().execute(getURL(date, ld2)).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int i = 0;
            while (date.isBefore(ld2.plusDays(1)) && i < 363) {
                if (get_json().keySet().contains(date.toString())) {
                    String rate = String.valueOf(get_json().getAsJsonObject(date.toString()).get(get_curr_inf(get_curr_to_r())));
                    Double rate_ = Double.parseDouble(rate);
                    rates.put(date, rate_);



                }
                i++;
                date = date.plusDays(1);
            }
        }
    }

}
