package com.example.currencyrates57;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;



public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.Holder>{

    Context context_c;

    public CurrencyAdapter(Context ct) {
        context_c = ct;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context_c);
        View view = inflater.inflate(R.layout.currency_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.c1.setText(get_info.get_curr_inf(position));
        holder.c2.setText(get_info.get_inf("NAME_EN", position));
        holder.fl.setImageResource(flags.get_flag(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context_c instanceof Currency_choose_act) {
                    ((Currency_choose_act)context_c).goback(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 33;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView c1, c2;
        ImageView fl;
        ConstraintLayout mainLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            c1 = itemView.findViewById(R.id.currency_txt);
            c2 = itemView.findViewById(R.id.currency_full_txt);
            fl = itemView.findViewById(R.id.flag);

            mainLayout = itemView.findViewById(R.id.card_currency);

        }
    }
}
