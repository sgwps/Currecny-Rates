package com.example.currencyrates57;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.*;

import java.util.concurrent.ExecutionException;

import static com.example.currencyrates57.Request.get_curr_to_r;
import static com.example.currencyrates57.Request.get_current_r;
import static com.example.currencyrates57.Request.get_json;
import static com.example.currencyrates57.SavingsData.*;


import static com.example.currencyrates57.get_info.get_curr_inf;


public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.Holder> implements ItemTouchHelperAdapter {

    ItemTouchHelper ith;
    Context ct;
    Double res__;


    public void current_rate(int fr, int to, int pos) {
        String URL_ = "https://api.exchangerate.host/convert?from=" + get_curr_inf(fr) + "&to=" + get_curr_inf(to);
        String res = null;
        try {
            res = new Parsing2().execute(URL_).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String res_ = get_json().getAsJsonObject("info").get("rate").getAsString();
        change1(pos, Double.parseDouble(res_));


    }

    public SavingsAdapter(Context ct_){
        ct = ct_;
    }



    @NonNull
    @Override
    public SavingsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.savings_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingsAdapter.Holder holder, int position) {
        JsonObject main_ = get_main_s().get(position).getAsJsonObject();
        Log.d("check71", String.valueOf(main_));

        if (main_.get("end").getAsString().equals("current") && !main_.get("rates").getAsJsonObject().keySet().contains(String.valueOf(Request.get_current_r()))){
            current_rate(main_.get("curr_from").getAsInt(), main_.get("curr_to").getAsInt(), position);

        }
        main_ = get_main_s().get(position).getAsJsonObject();
        Log.d("check11", String.valueOf(main_));
        holder.date_begin_2.setText((CharSequence) main_.get("begin").getAsString());
        Double sbs1 = main_.get("rates").getAsJsonObject().get(main_.get("begin").getAsString()).getAsDouble() * Double.parseDouble(main_.get("summ").getAsString());
        String s1 = get_info.get_inf("SYMBOL", main_.get("curr_to").getAsInt());
        String s2 = get_info.get_inf("SYMBOL", main_.get("curr_from").getAsInt());

        holder.date_begin_num_1.setText(s1 + " " + sbs1.toString());
        holder.date_begin_abs_1.setText(s2 + " 1 = " + main_.get("rates").getAsJsonObject().get(main_.get("begin").getAsString()).getAsString());
        String end = "";
        if (main_.get("end").getAsString().equals("current")) end = get_current_r().toString();
        else end = main_.get("end").getAsString();

        holder.date_end_2.setText(end);
        holder.date_end_num_1.setText(s1 + " " + String.valueOf(main_.get("rates").getAsJsonObject().get(end).getAsDouble() * main_.get("summ").getAsDouble()));
        holder.date_end_abs_1.setText(s2 + " 1 = " + main_.get("rates").getAsJsonObject().get(end).getAsString());

        holder.curr_from_g1.setImageResource(flags.get_flag(main_.get("curr_from").getAsInt()));
        holder.curr_to_g1.setImageResource(flags.get_flag(main_.get("curr_to").getAsInt()));
        holder.curr_from_g2.setText(get_info.get_curr_inf(main_.get("curr_from").getAsInt()) + " " + main_.get("summ").getAsString());
        holder.curr_to_g2.setText(get_info.get_curr_inf(main_.get("curr_to").getAsInt()));
        JsonObject finalMain_ = main_;
        holder.constr_s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ct instanceof MainActivity) {
                    ((MainActivity)ct).get_savings(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return get_main_s().size();
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Log.d("check51", "gone");
        SavingsData.onItemMove_(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        onItemSwiped_(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        ith = touchHelper;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnTouchListener,
            GestureDetector.OnGestureListener {
        GestureDetector gd;
        ConstraintLayout constr_s1;
        private ImageView curr_from_g1;
        private ImageView curr_to_g1;
        private TextView curr_from_g2;
        private TextView curr_to_g2;
        private TextView date_begin_2;
        private TextView date_begin_abs_1;
        private TextView date_begin_num_1;
        private TextView date_end_2;
        private TextView date_end_abs_1;
        private TextView date_end_num_1;

        public Holder(@NonNull View itemView) {
            super(itemView);
            constr_s1 = itemView.findViewById(R.id.constr_s);
            curr_from_g1 = itemView.findViewById(R.id.curr_from_s);
            curr_to_g1 = itemView.findViewById(R.id.curr_to_s);
            curr_from_g2 = itemView.findViewById(R.id.curr_from_s1);
            curr_to_g2 = itemView.findViewById(R.id.curr_to_s1);
            date_begin_2 = itemView.findViewById(R.id.date_begin_s);
            date_begin_abs_1 = itemView.findViewById(R.id.abd_begin_s);
            date_begin_num_1 = itemView.findViewById(R.id.rate_begin_s);
            date_end_2 = itemView.findViewById(R.id.date_end_s);
            date_end_abs_1 = itemView.findViewById(R.id.abd_end_s);
            date_end_num_1 = itemView.findViewById(R.id.rate_end_s);
            gd = new GestureDetector(itemView.getContext(), this);

            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("check50", "start");
            ith.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gd.onTouchEvent(event);
            return true;
        }
    }
}
