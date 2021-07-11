package com.layercontent.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.layercontent.weather_app.Retrofit.ManegarAll;
import com.layercontent.weather_app.adapter.Adapterhavadurumu;
import com.layercontent.weather_app.jsonpopjo.Condition;
import com.layercontent.weather_app.jsonpopjo.Condition__1;
import com.layercontent.weather_app.jsonpopjo.Day;
import com.layercontent.weather_app.jsonpopjo.Forecast;
import com.layercontent.weather_app.jsonpopjo.Forecastday;
import com.layercontent.weather_app.jsonpopjo.Wee;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detalist extends AppCompatActivity {
    String sehir, resimgelen;
    int resimid;
    final String api = "f4addb85b5e4492f8ed115420211606";
    List<Condition__1> conditionList2;
    List<Day> dayList;
    RecyclerView recyclerView;
    List resimidlist;
    List tarihlist;

    ImageView resimimage;
    TextView textgünsaat, textsehir, textderece, texthavadurumu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalist);
        tarihlist = new ArrayList();
        conditionList2 = new ArrayList<>();
        dayList = new ArrayList<>();
        resimidlist = new ArrayList();
        resimimage = findViewById(R.id.imagehavaresim);
        recyclerView = findViewById(R.id.recylerid);
        textgünsaat = findViewById(R.id.textgunsaat);
        textsehir = findViewById(R.id.textViewsehir);
        textderece = findViewById(R.id.textderece);
        texthavadurumu = findViewById(R.id.texthavadurumu);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setLayoutManager(new GridLayoutManager(Detalist.this, 3));
        sehir = getIntent().getStringExtra("country");
        resimgelen = getIntent().getStringExtra("resim");
        if (resimgelen.matches("png")) {
            Picasso.get().load(resimgelen).into(resimimage);
        }
        resimimage.setImageResource(Integer.parseInt(resimgelen));
        Havadurum(api, sehir, 3, "tr");
    }

    public void Havadurum(String api, String sehir, int days, String lang) {
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days, lang);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(retrofit2.Call<Wee> call, Response<Wee> response) {
                if (response.isSuccessful()) {
                    //Bugün ki hava durumu
                    final int lenght = response.body().getLocation().getLocaltime().length();
                    textgünsaat.setText("\n" + response.body().getLocation().getLocaltime().substring(10, lenght));
                    textsehir.setText(response.body().getLocation().getName());
                    texthavadurumu.setText(response.body().getCurrent().getCondition().getText());
                    String s = String.format("%.0f", response.body().getCurrent().getTempC());
                    textderece.setText(s);
                    //Diğer günler

                    for (int i = 0; i < response.body().getForecast().getForecastday().size(); i++) {
                        dayList.add(response.body().getForecast().getForecastday().get(i).getDay());
                        conditionList2.add(response.body().getForecast().getForecastday().get(i).getDay().getCondition());

                    }
                    for (int i = 0; i < response.body().getForecast().getForecastday().size(); i++) {
                        tarihlist.add(response.body().getForecast().getForecastday().get(i).getDate());
                    }
                    for (int i = 0; i < conditionList2.size(); i++) {
                        String ist = conditionList2.get(i).getText();
                        todaysweatherresim(ist);

                    }

                    Log.i("yyyyyyyy", String.valueOf(conditionList2.size()));

                    Adapterhavadurumu adapterhavadurumu = new Adapterhavadurumu(Detalist.this, conditionList2, resimidlist, tarihlist, dayList);
                    recyclerView.setAdapter(adapterhavadurumu);

                }
            }

            @Override
            public void onFailure(retrofit2.Call<Wee> call, Throwable t) {


            }
        });

    }


    public void todaysweatherresim(String durum) {
        if (durum.equals("Güneşli")) {

            resimid = R.drawable.ic_sunny;
        } else if (durum.equals("Bulutlu")
                || durum.equals("Çok Bulutlu")) {

            resimid = R.drawable.ic_clouds;
        } else if (durum.equals("Parçalı Bulutlu")
        ) {

            resimid = R.drawable.ic_pblut;
        }
        //YAĞMUR&KAR
        else if (durum.equals("Hafif yağmurlu")
                || durum.equals("Düzensiz hafif yağmurlu")
                || durum.equals("Hafif sağnak yağışlı")) {


            resimid = R.drawable.ic_hafifyamur;
        } else if (durum.equals("Bölgesel düzensiz yağmur yağışlı")
                || durum.equals("Ara ara orta kuvvetli yağmurlu")
                || durum.equals("Orta kuvvetli yağmurlu")) {

            resimid = R.drawable.ic_yamur;
        } else if (durum.equals("Şiddetli yağmurlu")
                || durum.equals("Bölgesel düzensiz gök gürültülü yağmurlu")
                || durum.equals("Orta kuvvetli veya yoğun sağnak yağışlı")
                || durum.equals("Şiddetli sağnak yağmur")
                || durum.equals("Ara ara şiddetli yağmurlu")) {

            resimid = R.drawable.ic_saganakyamur;

        } else if (durum.equals("Hafif buz taneleri şeklinde sağnak yağış")
                || durum.equals("Hafif karla karışık yağmur")
                || durum.equals("Hafif dondurucu yağmurlu")) {

            resimid = R.drawable.ic_karlayamur;

        } else if (durum.equals("Orta kuvvetli veya yoğun buz taneleri sağnak yağışlı")
                || durum.equals("Orta kuvvetli veya şiddetli karla karışık yağmur")
                || durum.equals("Orta kuvvetli veya Şiddetli dondurucu yağmurlu")) {

            resimid = R.drawable.ic_yukselikarlayamur;

        } else if (durum.equals("Hafif sağnak şeklinde kar")
                || durum.equals("Düzensiz hafif karlı")
                || durum.equals("Hafif karlı")) {

            resimid = R.drawable.ic_hafifkar;

        } else if (durum.equals("Düzensiz yoğun kar yağışlı")
                || durum.equals("Orta kuvvetli veya yoğun ve sağnak şeklinde kar")
                || durum.equals("Yoğun kar yağışlı")) {

            resimid = R.drawable.ic_yukkar;

        } else if (durum.equals("Orta kuvvetli karlı")
                || durum.equals("Bölgesel düzensiz kar yağışlı")) {

            resimid = R.drawable.ic_kar;

        } else if (durum.equals("Kar fırtınası")
        ) {

            resimid = R.drawable.ic_firtinakar;

        } else if (durum.equals("Puslu")
                || durum.equals("Dondurucu sis")
                || durum.equals("Tipi")
                || durum.equals("Sisli")) {

            resimid = R.drawable.ic_sisli;

        }

        resimidlist.add(resimid);
    }


}
