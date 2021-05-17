package com.example.currencyrates57;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.w3c.dom.Text;

import static com.example.currencyrates57.Request.*;
import static com.example.currencyrates57.get_info.get_curr_inf;
import static java.time.temporal.ChronoUnit.DAYS;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Graph extends Fragment {
    private LineChart chart;
    float minn;
    float maxx;
    LocalDate minn_;
    LocalDate maxx_;
    float avg;
    private ImageView curr_from_g1;
    private ImageView curr_to_g1;
    private TextView curr_from_g2;
    private TextView curr_to_g2;
    private TextView date_min1;
    private TextView min1;
    private TextView date_max1;
    private TextView max1;
    private TextView avg1;
    private TextView date1;
    private TextView date1__;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.mm.yyyy");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
        chart = view.findViewById(R.id.chart);
        curr_from_g1 = view.findViewById(R.id.curr_from_gr);
        curr_to_g1 = view.findViewById(R.id.curr_to_gr);
        curr_from_g2 = view.findViewById(R.id.curr_from_gr1);
        curr_to_g2 = view.findViewById(R.id.curr_to_gr1);
        date_min1 = view.findViewById(R.id.date_min);
        date_max1 = view.findViewById(R.id.date_max);
        min1 = view.findViewById(R.id.min_gr);
        max1 = view.findViewById(R.id.max_gr);
        date1 = view.findViewById(R.id.date_gr_date);
        date1__ = view.findViewById(R.id.date_gr);
        avg1 = view.findViewById(R.id.avg_gr);

    }

    @Override
    public void onResume() {
        super.onResume();
        List<Entry> entries = new ArrayList<Entry>();
        minn = get_rates_r().get(get_begin_r()).floatValue();
        maxx = get_rates_r().get(get_begin_r()).floatValue();
        minn_ = get_begin_r();
        maxx_ = get_begin_r();
        avg = 0;
        for (LocalDate date = get_begin_r(); date.isBefore(get_end_r().plusDays(1)); date = date.plusDays(1))
        {
            entries.add(new Entry(DAYS.between(get_begin_r(), date), get_rates_r().get(date).floatValue()));
            if (minn > get_rates_r().get(date).floatValue()) {minn = get_rates_r().get(date).floatValue(); minn_ = date;}
            if (maxx < get_rates_r().get(date).floatValue()) {maxx = get_rates_r().get(date).floatValue(); maxx_ = date;}

            avg += get_rates_r().get(date).floatValue();
        }
        avg /= DAYS.between(get_begin_r(), get_end_r()) + 1;
        LineDataSet lds = new LineDataSet(entries, "");
        lds.setColors(getResources().getColor(R.color.text_main));
        lds.setHighLightColor(getResources().getColor(R.color.blue_light));
        lds.setHighlightLineWidth(2);
        lds.setDrawCircles(false);
        lds.setDrawValues(false);
        lds.setLineWidth((float) 2.5);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lds);
        LineData data = new LineData(dataSets);
        Log.d("ch", String.valueOf(entries));
        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setAxisMaximum(DAYS.between(get_begin_r(), get_end_r()));
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setLabelCount(4);
        chart.getAxisLeft().setLabelCount(5);
        chart.getAxisLeft().setAxisMaximum((float) (maxx + (maxx - minn) * 0.015));
        chart.getAxisLeft().setAxisMinimum((float) (minn - (maxx - minn) * 0.015));
        chart.getAxisRight().setAxisMaximum((float) (maxx + (maxx - minn) * 0.015));
        chart.getAxisRight().setAxisMinimum((float) (minn - (maxx - minn) * 0.015));
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawZeroLine(false);
        chart.getAxisLeft().setDrawZeroLine(true);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setDrawLabels(true);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setGridLineWidth((float) 1.5);
        chart.getAxisLeft().setGridColor(getResources().getColor(R.color.grey));
        chart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.text_main));
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.grey));
        chart.getXAxis().setGridColor(getResources().getColor(R.color.grey));
        chart.getXAxis().setAxisLineColor(getResources().getColor(R.color.text_main));
        chart.getXAxis().setTextColor(getResources().getColor(R.color.grey));

        chart.getXAxis().setAxisLineWidth((float) 1.5);
        chart.getXAxis().setDrawGridLines(true);
        chart.getAxisLeft().setDrawTopYLabelEntry(true);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                date1.setText(String.valueOf(get_begin_r().plusDays((int) e.getX())));
                date1__.setText(String.valueOf(e.getY()));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(get_begin_r().plusDays( (long)Math.round(value)));
            }
        });

        chart.invalidate();

        curr_from_g1.setImageResource(flags.get_flag(get_curr_from_r()));
        curr_to_g1.setImageResource(flags.get_flag(get_curr_to_r()));
        curr_from_g2.setText(get_info.get_curr_inf(get_curr_from_r()));
        curr_to_g2.setText(get_info.get_curr_inf(get_curr_to_r()));
        date_min1.setText(String.valueOf(minn_));
        date_max1.setText(String.valueOf(maxx_));
        min1.setText(String.valueOf(minn));
        max1.setText(String.valueOf(maxx));
        avg1.setText(String.valueOf(avg));

    }
}