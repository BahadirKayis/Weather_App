package com.layercontent.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.layercontent.weather_app.Retrofit.ManegarAll;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detalist extends AppCompatActivity {
    String sehir;
    String sss;
    final String api = "f4addb85b5e4492f8ed115420211606";
    List<Wee> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalist);
        sehir = getIntent().getStringExtra("country");
        Havadurum(api, sehir, 3);
    }

    public void Havadurum(String api, String sehir, int days) {
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(retrofit2.Call<Wee> call, Response<Wee> response) {
                Log.i("xxx", response.body().getCurrent().getCondition().getText());
            }

            @Override
            public void onFailure(retrofit2.Call<Wee> call, Throwable t) {

            }
        });

    }
  /*  public void bismillah(String location){
       havadurumuadoInter.konum().enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(Call<Wee> call, Response<Wee> response) {

                Location location1=response.body().getLocation();



                Log.e("SEHİR",location1.getName() );

            }

            @Override
            public void onFailure(Call<Wee> call, Throwable t) {

            }
        });
    }*/
   /* public void deneme(String key,String loc){
       Call<List<Wee>>call=ManegarAll.getInstance().getirbilgier(key,loc);
       call.enqueue(new Callback<List<Wee>>() {
           @Override
           public void onResponse(Call<List<Wee>> call, Response<List<Wee>> response) {
               Log.i("xxx", response.body().get(0).getCurrent().getCondition().getText());
           }

           @Override
           public void onFailure(Call<List<Wee>> call, Throwable t) {

           }
       });
    }*/
}