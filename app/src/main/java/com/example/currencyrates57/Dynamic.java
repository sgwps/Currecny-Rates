package com.example.currencyrates57;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.currencyrates57.Request.get_curr_from_r;
import static com.example.currencyrates57.Request.get_curr_to_r;


public class Dynamic extends Fragment {
    RecyclerView list_;
    private ImageView curr_from_g1;
    private ImageView curr_to_g1;
    private TextView curr_from_g2;
    private TextView curr_to_g2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        list_ = view.findViewById(R.id.list);
        curr_from_g1 = view.findViewById(R.id.curr_from_d);
        curr_to_g1 = view.findViewById(R.id.curr_to_d);
        curr_from_g2 = view.findViewById(R.id.curr_from_d1);
        curr_to_g2 = view.findViewById(R.id.curr_to_d1);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        list_.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onResume() {
        super.onResume();
        DynamicAdapter adapter = new DynamicAdapter(getContext());
        list_.setAdapter(adapter);
        list_.setLayoutManager(new LinearLayoutManager(getContext()));
        curr_from_g1.setImageResource(flags.get_flag(get_curr_from_r()));
        curr_to_g1.setImageResource(flags.get_flag(get_curr_to_r()));
        curr_from_g2.setText(get_info.get_curr_inf(get_curr_from_r()));
        curr_to_g2.setText(get_info.get_curr_inf(get_curr_to_r()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }
}