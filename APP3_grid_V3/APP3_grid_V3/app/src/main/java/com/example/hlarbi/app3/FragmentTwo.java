package com.example.hlarbi.app3;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.hlarbi.app3.linechart_animation_pollu.ChartView;
import com.example.hlarbi.app3.linechart_animation_pollu.InputData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.Line;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {


    MainActivity m = new MainActivity();
    private ViewFlipper viewFlipper;




    class CustomAdapter_Pollu_1 extends BaseAdapter {

        @Override
        public int getCount() {
            return NAMES.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_sample_gridview_pollu, null);
            TextView g_subject  = (TextView)view.findViewById(R.id.textView_subject_item_grid_pollu);
            TextView g_num = (TextView)view.findViewById(R.id.textView_number_item_pollu);
            TextView g_full = (TextView)view.findViewById(R.id.textView_fullname_pollu);

            g_subject.setText(NAMES[i]);
            g_num.setText(NUMBERS[i]);
            g_full.setText(FULLNAMES[i]);
            //   g_bgs.setBackgroundResource(BGS[i]);

            return view;
        }
    }


    String[] FULLNAMES = {"steps", "colories", "distance", "floors", "heigt", "duration"};
    String[] NAMES = {"CO","NO²","O3","PM10","PM25","SO²"};
    String[] NUMBERS = {"150.5", "10", "7.15", "5.5", "3.2", "17"};

    public GridView getGrid(GridView ls) {
        ls = getActivity().findViewById(R.id.grid_view_data_pollu);

        return ls;
    }





    public FragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pollu_view = inflater.inflate(R.layout.activity_data_pollution, container, false);
          initViews(pollu_view);
        GridView ls = (GridView) pollu_view.findViewById(R.id.grid_view_data_pollu);
        ls=ls;
        CustomAdapter_Pollu_1 customAdapter_pollu = new CustomAdapter_Pollu_1();
        ls.setAdapter(customAdapter_pollu);


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
