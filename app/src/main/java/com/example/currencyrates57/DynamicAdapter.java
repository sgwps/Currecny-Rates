package com.example.currencyrates57;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import static com.example.currencyrates57.Request.*;

public class DynamicAdapter  extends RecyclerView.Adapter<DynamicAdapter.Holder> {

    Context context;
    DecimalFormat df = new DecimalFormat("#########.#######");


    public DynamicAdapter(Context ct) {

        context = ct;

    }

    @NonNull
    @Override
    public DynamicAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dynamic_item_layout, parent, false);
        return new DynamicAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicAdapter.Holder holder, int position) {
        holder.date_d1.setText(String.valueOf(get_current_r().minusDays(position)));
        holder.rate_d1.setText(String.valueOf(get_rates_r(get_current_r().minusDays(position))));
        holder.difference_d1.setText(String.valueOf(df.format(get_rates_r(get_current_r().minusDays(position)) - get_rates_r(get_current_r().minusDays(position + 1)))));
        if (get_rates_r(get_current_r().minusDays(position)) - get_rates_r(get_current_r().minusDays(position + 1)) < 0){
            holder.arrow_d1.setImageResource(R.drawable.ic_green__1_);
        }
        else if (get_rates_r(get_current_r().minusDays(position)) - get_rates_r(get_current_r().minusDays(position + 1)) == 0){
            holder.arrow_d1.setImageResource(R.drawable.ic_line);

        }
        else{
            holder.arrow_d1.setImageResource(R.drawable.ic_red);
        }
    }

    @Override
    public int getItemCount() {
        return 14;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ConstraintLayout main_layout;
        TextView date_d1;
        TextView rate_d1;
        TextView difference_d1;
        ImageView arrow_d1;
        public Holder(@NonNull View itemView) {
            super(itemView);
            Log.d("check", String.valueOf(Request.get_rates_r()));
            main_layout = itemView.findViewById(R.id.card_d);
            date_d1 = itemView.findViewById(R.id.date_d);
            rate_d1 = itemView.findViewById(R.id.rate_d);
            difference_d1 = itemView.findViewById(R.id.difference_d);
            arrow_d1 = itemView.findViewById(R.id.arrow_d);
        }
    }
}
