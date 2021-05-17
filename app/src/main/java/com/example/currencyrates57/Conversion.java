package com.example.currencyrates57;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.example.currencyrates57.Request.*;



public class Conversion extends Fragment {

    private View view;

    private String[] currs;

    private CardView curr_to1;
    private CardView curr_from1;

    private TextView curr_to_txt_1;
    private TextView curr_to_full_txt_1;
    private ImageView flag_to_1;

    private TextView curr_from_txt_1;
    private TextView curr_from_full_txt_1;
    private ImageView flag_from_1;


    private Button date_begin;
    private Button date_end;

    private EditText summ_view;
    private TextView summ1;


    private TextView date_begin_2;
    private TextView date_begin_abs_1;
    private TextView date_begin_num_1;
    private TextView date_begin_abs_num_1;

    private TextView date_end_2;
    private TextView date_end_abs_1;
    private TextView date_end_num_1;
    private TextView date_end_abs_num_1;

    private TextView difference_2;
    private TextView difference_abs_1;
    private TextView difference_num_1;
    private TextView difference_abs_num_1;
    private DecimalFormat df = new DecimalFormat("#########.#######");

    private DatePickerDialog datePickerDialog;
    private ImageButton switch_1;
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                set_summ_r(Double.parseDouble(summ_view.getText().toString()));
                summ_view.setCursorVisible(false);
                change_results();
            }
            return false;
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversion, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        change_results();
        date_begin.setClickable(true);
        date_end.setClickable(true);
        curr_to1.setClickable(true);
        curr_from1.setClickable(true);
        switch_1.setClickable(true);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        curr_from1 = view.findViewById(R.id.curr_from);
        curr_to1 = view.findViewById(R.id.curr_to);
        curr_to_txt_1 = view.findViewById(R.id.curr_to_txt);
        curr_to_full_txt_1 = view.findViewById(R.id.curr_to_full);
        flag_to_1 = view.findViewById(R.id.flag_to);
        curr_from_txt_1 = view.findViewById(R.id.curr_from_txt);
        curr_from_full_txt_1 = view.findViewById(R.id.curr_from_full);
        flag_from_1 = view.findViewById(R.id.flag_from);
        currs = getResources().getStringArray(R.array.currencies);
        date_begin = view.findViewById(R.id.date_begin);
        date_end = view.findViewById(R.id.date_end);
        summ_view = view.findViewById(R.id.summa);
        summ_view.setOnEditorActionListener(editorListener);
        summ_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summ_view.setCursorVisible(true);
            }
        });
        summ1 = view.findViewById(R.id.summ1);
        date_begin_num_1 = view.findViewById(R.id.date_begin_1_num);
        date_begin_abs_num_1 = view.findViewById(R.id.date_begin_abs_num);
        date_end_num_1 = view.findViewById(R.id.date_end_1_num);
        date_end_abs_num_1 = view.findViewById(R.id.date_end_abs_num);
        difference_2 = view.findViewById(R.id.difference);
        difference_num_1 = view.findViewById(R.id.difference_num);
        difference_abs_num_1 = view.findViewById(R.id.difference_abs_num);
        switch_1 = view.findViewById(R.id.switch_);
        switch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog ld = new LoadingDialog(getActivity());
                ld.startLoadingDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        swap();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                change_results();
                                ld.dismissDialog();
                            }
                        });
                    }
                }).start();
            }
        });

        date_begin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                set_begin_c(year, monthOfYear + 1, dayOfMonth);
                            }
                        }, get_begin_r().getYear(), get_begin_r().getMonthValue() - 1, get_begin_r().getDayOfMonth());
                datePickerDialog.getDatePicker().setMinDate((new GregorianCalendar(get_min_date_r().getYear(), get_min_date_r().getMonthValue() - 1, get_min_date_r().getDayOfMonth())).getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(new GregorianCalendar(get_end_r().getYear(), get_end_r().getMonthValue() - 1, get_end_r().getDayOfMonth()).getTimeInMillis());
                datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(view.getContext(), R.color.text_main));
                        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(view.getContext(), R.color.text_main));
                    }
                });

                datePickerDialog.show();

            }
        });

        date_end.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                set_end_c(year, monthOfYear + 1, dayOfMonth);
                            }
                        }, get_end_r().getYear(), get_end_r().getMonthValue() - 1, get_end_r().getDayOfMonth());
                datePickerDialog.getDatePicker().setMinDate(new GregorianCalendar(get_begin_r().getYear(), get_begin_r().getMonthValue() - 1, get_begin_r().getDayOfMonth()).getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(new GregorianCalendar(get_current_r().getYear(), get_current_r().getMonthValue() - 1, get_current_r().getDayOfMonth()).getTimeInMillis());
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.text_main);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.text_main);
                datePickerDialog.show();
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        date_begin.setClickable(false);
        date_end.setClickable(false);
        curr_to1.setClickable(false);
        curr_from1.setClickable(false);
        switch_1.setClickable(false);
    }

    public void set_begin_c(int year, int monthOfYear, int dayOfMonth){
        datePickerDialog.dismiss();
        LoadingDialog ld = new LoadingDialog(getActivity());
        ld.startLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                set_begin_r(year, monthOfYear, dayOfMonth);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.dismissDialog();
                        change_results();
                    }
                });

            }
        }).start();



    }

    public void set_end_c(int year, int monthOfYear, int dayOfMonth){
        datePickerDialog.dismiss();
        LoadingDialog ld = new LoadingDialog(getActivity());
        ld.startLoadingDialog();
        new Thread(new Runnable() {
            public void run() {
                try {
                    set_end_r(year, monthOfYear, dayOfMonth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ld.dismissDialog();
                    change_results();
                }
            });

            }
        }).start();



    }

    public void change_results(){
        date_begin.setText(get_begin_txt());
        date_begin_num_1.setText(get_info.get_inf("SYMBOL", get_curr_to_r()) + " " + String.valueOf(get_rates_r(get_begin_r()) * get_summ_r()));
        date_begin_abs_num_1.setText(get_info.get_inf("SYMBOL", get_curr_from_r()) + " 1 = " + get_info.get_inf("SYMBOL", get_curr_to_r()) + " " + String.valueOf(get_rates_r(get_begin_r())));
        date_end.setText(get_end_txt());
        date_end_num_1.setText(get_info.get_inf("SYMBOL", get_curr_to_r()) + " " + String.valueOf(get_rates_r(get_end_r()) * get_summ_r()));
        date_end_abs_num_1.setText(get_info.get_inf("SYMBOL", get_curr_from_r()) + " 1 = " + get_info.get_inf("SYMBOL", get_curr_to_r()) + " " + String.valueOf(get_rates_r(get_end_r())));
        difference_num_1.setText(get_info.get_inf("SYMBOL", get_curr_to_r()) + " " + String.valueOf(df.format((get_rates_r(get_end_r()) - get_rates_r(get_begin_r())) * get_summ_r())));
        difference_abs_num_1.setText(get_info.get_inf("SYMBOL", get_curr_to_r()) + " " + String.valueOf(df.format(get_rates_r(get_end_r()) -get_rates_r(get_begin_r()))));
        curr_to_txt_1.setText(currs[get_curr_to_r()]);
        curr_to_full_txt_1.setText(get_info.get_inf("NAME_EN", get_curr_to_r()));
        flag_to_1.setImageResource(flags.get_flag(get_curr_to_r()));
        curr_from_txt_1.setText(currs[get_curr_from_r()]);
        curr_from_full_txt_1.setText(get_info.get_inf("NAME_EN", get_curr_from_r()));
        flag_from_1.setImageResource(flags.get_flag(get_curr_from_r()));
        summ1.setText(get_info.get_inf("SYMBOL", get_curr_from_r()));
        summ_view.setText(String.valueOf(get_summ_r()));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}