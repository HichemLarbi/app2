package com.example.hlarbi.aqiapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.TextViewAQI);

        //calling the method to display the heroes
        getHeroes();
    }

    public void getHeroes() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);

        Call<AQI> call = api.getAqui();

        call.enqueue(new Callback<AQI>() {
            @Override
            public void onResponse(Call<AQI> call, Response<AQI> response) {
                AQI aqiList = response.body();

                //Creating an String array for the ListView
                String aqis = new String();

                //looping through all the aqis and inserting the names inside the string array

                    aqis = aqiList.getBreezometer_aqi();

                Toast.makeText(getApplicationContext(), String.valueOf(aqis), Toast.LENGTH_SHORT).show();
                }

            @Override
            public void onFailure(Call<AQI> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Non", Toast.LENGTH_SHORT).show();

            }


            //displaying the string array into listview
        });

    }
}
