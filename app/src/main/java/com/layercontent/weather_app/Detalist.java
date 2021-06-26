package com.layercontent.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.layercontent.weather_app.Retrofit.ManegarAll;
import com.layercontent.weather_app.adapter.Adapterhavadurumu;
import com.layercontent.weather_app.jsonpopjo.Condition;
import com.layercontent.weather_app.jsonpopjo.Condition__1;
import com.layercontent.weather_app.jsonpopjo.Day;
import com.layercontent.weather_app.jsonpopjo.Forecast;
import com.layercontent.weather_app.jsonpopjo.Forecastday;
import com.layercontent.weather_app.jsonpopjo.Wee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detalist extends AppCompatActivity {
    String sehir;
    final String api = "f4addb85b5e4492f8ed115420211606";
    //List<Current> list;
List<Condition>bugunhavadurumu;
    List<Condition__1>conditionList2;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalist);
        bugunhavadurumu=new ArrayList<>();
        conditionList2=new ArrayList<>();
        recyclerView = findViewById(R.id.recylerid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(Detalist.this, 3));
        sehir = getIntent().getStringExtra("country");
        Havadurum(api, sehir, 3, "tr");
    }

    public void Havadurum(String api, String sehir, int days, String lang) {
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days, lang);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(retrofit2.Call<Wee> call, Response<Wee> response) {
                if (response.isSuccessful()) {
                   Log.i("xxx", response.body().getCurrent().getCondition().getText());
                   bugunhavadurumu.add(response.body().getCurrent().getCondition());
                    Log.i("Bugün ki hava durumu", bugunhavadurumu.get(0).getText());
                    for (int i=0;i<response.body().getForecast().getForecastday().size();i++){
                        conditionList2.add(response.body().getForecast().getForecastday().get(i).getDay().getCondition());
                    }
                    Log.i("yyy", conditionList2.get(0).getText());
                    Log.i("yyy", conditionList2.get(1).getText());
                    Log.i("yyy", conditionList2.get(2).getText());


                   // currentClass = response.body().getListweedecondi();


                    //Log.i("yyy", conditionList.get(2).getText());
                 //  Adapterhavadurumu adapterhavadurumu = new Adapterhavadurumu(Detalist.this, conditionList);
                    //recyclerView.setAdapter(adapterhavadurumu);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Wee> call, Throwable t) {


            }
        });

    }

}