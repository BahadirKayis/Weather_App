package com.layercontent.weather_app.Retrofit;

import com.layercontent.weather_app.HavadurumuadoInter;

public class BaseManager {
    protected HavadurumuadoInter getresthava(){
        RestApiClient restApiClient=new RestApiClient(BaseUrl.BASE_URL);
        return restApiClient.getmRestApi();
    }
    protected HavadurumuadoInter getsehir(){
        RestApiClient restApiClient=new RestApiClient(BaseUrl.BASE_URLSEHİR);
        return restApiClient.getmRestApi();
    }
}
