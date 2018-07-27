package com.example.hlarbi.app3;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.hlarbi.app3.API.GetResquest;
import com.example.hlarbi.app3.API.ServiceGenerator;
import com.example.hlarbi.app3.API.objects.FitBitApi.Activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment implements View.OnClickListener{
    private ViewFlipper viewFlipper;
    GridView gridView;
    ArrayList<Running> list;
    RunningListAdapter adapter = null;


    ///UPDATE BUTTON///
    private Button mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public TextView chosenDate;
    public String dateFirst = "today";

    public ViewFlipper getViewFlipper(ViewFlipper vf) {
        vf = getActivity().findViewById(R.id.view_flipper);

        return vf;
    }



    public FragmentOne() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_data_resume, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.grid_view_data_resume);
        String tokenFull = getArguments().getString("KEY");
        String[] tokenH1 = tokenFull.split("²");
        final String headertoken = tokenH1[0];
        final String userID = tokenH1[1];


        mDisplayDate =(Button) v.findViewById(R.id.buttondate);


        ////////////////////FlipperView////////////////////////
        Button bL = (Button) v.findViewById(R.id.bvflipperL);
        Button bR = (Button) v.findViewById(R.id.bvflipperR);
        bL.setOnClickListener(this);
        bR.setOnClickListener(this);
        ////////////////////////FlipperView////////////////////////////////


        list = new ArrayList<>();
        adapter = new RunningListAdapter(getContext(), R.layout.item_sample_gridview_run, list);
        gridView.setAdapter(adapter);

        Cursor cursor = FirstActivity.sqLiteHelper.getData("SELECT * FROM RUNNING");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String number = cursor.getString(2);
            list.add(new Running(name, number, id));
        }
        adapter.notifyDataSetChanged();


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;


                String date = year + "-" + month + "-" + day;
                //      chosenDate.setText(date);

                final GetResquest clientg = ServiceGenerator.createService(GetResquest.class);

                final Map<String, String> map = new HashMap<>();

                map.put("Authorization", headertoken);
                final Call<Activities> calld = clientg.getActivitiesData(map, userID, String.valueOf(date));
                calld.enqueue(new Callback<Activities>() {
                    @Override
                    public void onResponse(Call<Activities> call, Response<Activities> response) {

                        // textView.setText(String.valueOf(activities.getSummary().getSteps()));
                        Activities actvi = response.body();
                        SQLiteDatabase db = FirstActivity.sqLiteHelper.getWritableDatabase();

                        String steps = String.valueOf(actvi.getSummary().getSteps());
                        String calo= String.valueOf(actvi.getSummary().getCaloriesOut());
                        String distance= String.valueOf(actvi.getSummary().getDistances().get(1).getDistance());
                        String dura= String.valueOf(actvi.getSummary().getSedentaryMinutes());
                        String floors= String.valueOf(actvi.getSummary().getFloors());
                        String height= String.valueOf(actvi.getSummary().getElevation());

                        ContentValues cvSteps = new ContentValues();
                        ContentValues cvCalo = new ContentValues();
                        ContentValues cvDis = new ContentValues();
                        ContentValues cvDura = new ContentValues();
                        ContentValues cvFloors = new ContentValues();
                        ContentValues cvHeight = new ContentValues();

                        cvSteps.put("number",steps);
                        cvCalo.put("number",calo);
                        cvDis.put("number",distance);
                        cvDura.put("number",dura);
                        cvFloors.put("number",floors);
                        cvHeight.put("number",height);

                        db.update("RUNNING", cvSteps, "id = 1", null);
                        db.update("RUNNING", cvCalo, "id = 2", null);
                        db.update("RUNNING", cvDis, "id = 3", null);
                        db.update("RUNNING", cvDura, "id = 4", null);
                        db.update("RUNNING", cvFloors, "id = 5", null);
                        db.update("RUNNING", cvHeight, "id = 6", null);

                        updateRunningList();
                    }

                    @Override
                    public void onFailure(Call<Activities> call, Throwable t) {

                    }
                });

            }

        };

        return v;
    }



    private void updateRunningList(){
        // get all data from sqlite
        Cursor cursor = FirstActivity.sqLiteHelper.getData("SELECT * FROM RUNNING");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String number = cursor.getString(2);


            list.add(new Running(name, number,id));
        }
        adapter.notifyDataSetChanged();




    }

    public void previousView(View v) {
        getViewFlipper(viewFlipper).setInAnimation(getContext(), R.anim.slide_in_right);

        getViewFlipper(viewFlipper).showPrevious();
    }
    public void nextView(View v) {
        getViewFlipper(viewFlipper).setInAnimation(getContext(), R.anim.slide_out_right);

        getViewFlipper(viewFlipper).setOutAnimation(getContext(), R.anim.slide_in_left);

        getViewFlipper(viewFlipper).showNext();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bvflipperL:
                previousView(v);
                break;
            case R.id.bvflipperR:
                nextView(v);
                break;



        }

            }


}













































/*       //Calendar

        mDisplayDate = (Button) v.findViewById(R.id.buttondate);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;


                String date = year + "-" + month + "-" + day;

                final GetResquest clientg = ServiceGenerator.createService(GetResquest.class);

                final Map<String, String> map = new HashMap<>();

                map.put("Authorization", headertoken);
                final Call<Activities> calld = clientg.getActivitiesData(map,token,String.valueOf(date));
                calld.enqueue(new Callback<Activities>() {
                    @Override
                    public void onResponse(Call<Activities> call, Response<Activities> response) {
                        Activities activities1 =response.body();


                    }

                    @Override
                    public void onFailure(Call<Activities> call, Throwable t) {

                    }
                });

            }

        };*/