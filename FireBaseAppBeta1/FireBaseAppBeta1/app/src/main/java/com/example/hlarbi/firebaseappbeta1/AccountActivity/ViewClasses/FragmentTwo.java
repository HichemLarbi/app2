package com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses.linechart_animation_pollu.ChartView;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses.linechart_animation_pollu.InputData;
import com.example.hlarbi.firebaseappbeta1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {
    private RecyclerView.LayoutManager mLayoutManager;


    public FragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pollu_view = inflater.inflate(R.layout.activity_data_pollu, container, false);
        RecyclerView pollu_recyclerView = (RecyclerView) pollu_view.findViewById(R.id.recycle_pollu);
        MyRecycleAdapter listAdapter = new MyRecycleAdapter() {
        };
        pollu_recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
pollu_recyclerView.setLayoutManager(layoutManager);


        initViews(pollu_view);



        // Inflate the layout for this fragment

        return pollu_view;

    }
    private void initViews(View v) {
        ChartView chartView = v.findViewById(R.id.charView);
        List<InputData> dataList = createChartData();
        chartView.setData(dataList);
    }

    @NonNull
    private List<InputData> createChartData() {
        List<InputData> dataList = new ArrayList<>();
        dataList.add(new InputData(18));
        dataList.add(new InputData(18));
        dataList.add(new InputData(18));
        dataList.add(new InputData(18));
        dataList.add(new InputData(18));
        dataList.add(new InputData(18));
        dataList.add(new InputData(18));




//X axe => add date number
        long currMillis = System.currentTimeMillis();
        currMillis -= currMillis % TimeUnit.DAYS.toMillis(1);

        for (int i = 0; i < dataList.size(); i++) {
            long position = dataList.size() - 1 - i;
            long offsetMillis = TimeUnit.DAYS.toMillis(position);

            long millis = currMillis - offsetMillis;
            dataList.get(i).setMillis(millis);
        }

        return dataList;
    }

}
