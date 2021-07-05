package com.layercontent.weather_app.Retrofit;


import com.layercontent.weather_app.jsonpopjo.SehirCevap;
import com.layercontent.weather_app.jsonpopjo.Wee;

import java.util.List;

import retrofit2.Call;

public class ManegarAll extends BaseManager {
    private static ManegarAll ourınstance=new ManegarAll();
    public static synchronized ManegarAll getInstance(){
        return ourınstance;
    }
    public Call<Wee> getirbilgier(String api, String sehir, int days, String lang){
        Call<Wee>call= getresthava().havadurumu(api,sehir,days,lang);

        //apiutilsa gidiyor
        //oradan getuserinter clasını çağırıyor oradan da interface den bilgiler clasını çağırıyor,
        //ondan sonra getirbilgiler sınıfını bilgilerin görüneceği yerde çağırıyoruz
        return call;
    }
    public Call<List<SehirCevap>> getirsehirleri(){
        Call<List<SehirCevap>>call= getsehir().Sehirler();

        //apiutilsa gidiyor
        //oradan getuserinter clasını çağırıyor oradan da interface den bilgiler clasını çağırıyor,
        //ondan sonra getirbilgiler sınıfını bilgilerin görüneceği yerde çağırıyoruz
        return call;
    }
}
