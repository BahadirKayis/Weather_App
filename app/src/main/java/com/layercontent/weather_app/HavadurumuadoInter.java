package com.layercontent.weather_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HavadurumuadoInter {
/*@POST("f4addb85b5e4492f8ed115420211606")
    @FormUrlEncoded
    Call <Wee>konum(@Field("location")Location location);
*/
@POST("current.json")
    @FormUrlEncoded
    Call<Wee> havadurumu(@Field("key") String key,@Field("q")String sehir,@Field("days")int days);
}
