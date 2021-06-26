package com.layercontent.weather_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.layercontent.weather_app.Retrofit.ManegarAll;
import com.layercontent.weather_app.adapter.Adapterhavadurumu;
import com.layercontent.weather_app.jsonpopjo.Condition;
import com.layercontent.weather_app.jsonpopjo.Wee;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private int izinkontrol;
    ImageView add;
    String resimid;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView text;
    //Linear1
    TextView lnsehir, ulke, derece;
    ImageView havaresim;
    ///////
    double longitude;
    double latitude;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    LinearLayout linear1, linear2, linear3, linear4;
    //havadurumuapi
    final String api = "f4addb85b5e4492f8ed115420211606";
    List<Condition> bugunhavadurumu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.imageadd);
        add.setOnClickListener(this);
        text = findViewById(R.id.linear1text1);
        linear1 = findViewById(R.id.linearLayout1);
        linear2 = findViewById(R.id.linearLayout2);
        linear3 = findViewById(R.id.linearLayout3);
        linear4 = findViewById(R.id.linearLayout4);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        linear3.setOnClickListener(this);
        linear4.setOnClickListener(this);
        anlikkonum();
        ///
        tanim();
    }

    public void tanim() {
        lnsehir = findViewById(R.id.linear1text1);
        ulke = findViewById(R.id.linear2text2);
        derece = findViewById(R.id.linear1text3);
        havaresim = findViewById(R.id.linear1image);
    }

    public void anlikkonum() {
        izinkontrol = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (izinkontrol != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            //daha önce izin verilmemişse burası çalışcak :Todo : yeni şeyler

            getLocation();
        } else {
            //daha önce izin verilmiş ise burası
            // startActivity(new Intent(MainActivity.this,MapsActivity.class));
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                    getLocation();


            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();

            }
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                text.setText(state);

                todaysweather(api, state, 3, "tr");

            } catch (Exception e) {
                Log.e("Location Error ", e.getMessage());
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageadd:

                break;
            case R.id.linearLayout1:
                String country = text.getText().toString().trim();
                Intent i = new Intent(MainActivity.this, Detalist.class);
                i.putExtra("resim",resimid);
                i.putExtra("country", country);
                startActivity(i);
                break;
            case R.id.linearLayout2:
                break;
            case R.id.linearLayout3:
                break;
            case R.id.linearLayout4:
                break;
        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.must_accep_find_location)).setCancelable(false).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

//        latitude = location.getLatitude();
//        longitude = location.getLongitude();


        locationManager.removeUpdates(this);

        //open the map:
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
        // Toast.makeText(ProfileActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        // searchNearestPlace(voice2text);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            // Location locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationGPS = locationManager.getLastKnownLocation(bestProvider);
            onLocationChanged(locationGPS);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = Double.parseDouble(String.valueOf(lat));
                longitude = Double.parseDouble(String.valueOf(longi));
            } else {
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);


                Location locationGPS2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                // Location locationGPS2 = locationManager.getLastKnownLocation();
                onLocationChanged(locationGPS2);
                if (locationGPS2 != null) {
                    double lat = locationGPS2.getLatitude();
                    double longi = locationGPS2.getLongitude();
                    latitude = Double.parseDouble(String.valueOf(lat));
                    longitude = Double.parseDouble(String.valueOf(longi));
                } else {
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                    Toast.makeText(this, getResources().getString(R.string.cant_find_location), Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//                public void onComplete(@NonNull Task<Location>task) {
//                Location location = task.getResult();
//                if (location != null) {
//
//                    //initialize adress list
//                    try {
//                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                        //şehirin adresi
//
//                        text.setText(addresses.get(0).getLocality() + "s");
//                       // text.setText(addresses.get(0).getAddressLine(0) + "s"); ev adresine kadar hepsini alıyor
//                       // text.setText(addresses.get(0).getSubLocality() + "s"); mahalle
//                       // text.setText(addresses.get(0).getLatitude() + "s");    boylkam
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } });
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Lokasyon erişimine izin verildi", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(MainActivity.this,MapsActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "Lokasyon erişimini açın lan", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void todaysweatherresim(String durum, String resimtext) {
        if (durum.equals("Güneşli")) {
            havaresim.setImageResource(R.drawable.ic_sunny);
            resimid= String.valueOf(R.drawable.ic_sunny);
        }

      else   if (durum.equals("Bulutlu")
                & durum.equals("Çok Bulutlu")) {
            havaresim.setImageResource(R.drawable.ic_clouds);
            resimid= String.valueOf(R.drawable.ic_clouds);
        }
        else   if (durum.equals("Parçalı Bulutlu")
                ) {
            havaresim.setImageResource(R.drawable.ic_pblut);///BU KODU ALIPGÖNDERCEM
            resimid= String.valueOf(R.drawable.ic_pblut);
        }
        //YAĞMUR&KAR
        else   if (durum.equals("Hafif yağmurlu")
                & durum.equals("Düzensiz hafif yağmurlu")
                & durum.equals("Hafif sağnak yağışlı")) {

            havaresim.setImageResource(R.drawable.ic_hafifyamur);
            resimid= String.valueOf(R.drawable.ic_hafifyamur);
        }
        else   if (durum.equals("Bölgesel düzensiz yağmur yağışlı")
                & durum.equals("Ara ara orta kuvvetli yağmurlu")
                & durum.equals("Orta kuvvetli yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_yamur);
            resimid= String.valueOf(R.drawable.ic_yamur);
        }
        else   if (durum.equals("Şiddetli yağmurlu")
                & durum.equals("Bölgesel düzensiz gök gürültülü yağmurlu")
                & durum.equals("Orta kuvvetli veya yoğun sağnak yağışlı")
                & durum.equals("Şiddetli sağnak yağmur")
                & durum.equals("Ara ara şiddetli yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_saganakyamur);
            resimid= String.valueOf(R.drawable.ic_saganakyamur);

        }
        else  if (durum.equals("Hafif buz taneleri şeklinde sağnak yağış")
                & durum.equals("Hafif karla karışık yağmur")
                & durum.equals("Hafif dondurucu yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_karlayamur);
            resimid= String.valueOf(R.drawable.ic_karlayamur);

        }
        else   if (durum.equals("Orta kuvvetli veya yoğun buz taneleri sağnak yağışlı")
                & durum.equals("Orta kuvvetli veya şiddetli karla karışık yağmur")
                & durum.equals("Orta kuvvetli veya Şiddetli dondurucu yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_yukselikarlayamur);
            resimid= String.valueOf(R.drawable.ic_yukselikarlayamur);

        }
        else  if (durum.equals("Hafif sağnak şeklinde kar")
                & durum.equals("Düzensiz hafif karlı")
                & durum.equals("Hafif karlı")) {
            havaresim.setImageResource(R.drawable.ic_hafifkar);
            resimid= String.valueOf(R.drawable.ic_hafifkar);

        }
        else  if (durum.equals("Düzensiz yoğun kar yağışlı")
                & durum.equals("Orta kuvvetli veya yoğun ve sağnak şeklinde kar")
                & durum.equals("Yoğun kar yağışlı")) {
            havaresim.setImageResource(R.drawable.ic_yukkar);
            resimid= String.valueOf(R.drawable.ic_yukkar);

        }
        else   if (durum.equals("Orta kuvvetli karlı") & durum.equals("Bölgesel düzensiz kar yağışlı")) {
            havaresim.setImageResource(R.drawable.ic_kar);
            resimid= String.valueOf(R.drawable.ic_kar);

        }
        else    if (durum.equals("Kar fırtınası")
        ) {
            havaresim.setImageResource(R.drawable.ic_firtinakar);
            resimid= String.valueOf(R.drawable.ic_firtinakar);

        }
        else  if (durum.equals("Puslu")
                & durum.equals("Dondurucu sis")
                & durum.equals("Tipi")
                & durum.equals("Sisli")) {
            havaresim.setImageResource(R.drawable.ic_sisli);
            resimid= String.valueOf(R.drawable.ic_sisli);

        } else {
            Picasso.get().load(resimtext).into(havaresim);
            resimid=resimtext;
        }
    }

    public void todaysweather(String api, String sehir, int days, String lang) {//anlıkkonumda çağırdık bunu
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days, lang);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(Call<Wee> call, Response<Wee> response) {
                ulke.setText(response.body().getLocation().getCountry()+ "," + response.body().getLocation().getLocaltime().substring(10,16));

                String s = String.format("%.0f", response.body().getCurrent().getTempC());
                derece.setText(s + "°");
                Log.i("xxx", response.body().getCurrent().getCondition().getText());
                Log.i("xxx", response.body().getCurrent().getCondition().getIcon());
                String resim = "http:" + response.body().getCurrent().getCondition().getIcon();// http://cdn.weatherapi.com/weather/64x64/day/116.png
                String havatexti = response.body().getCurrent().getCondition().getText();
                todaysweatherresim(havatexti, resim);

            }

            @Override
            public void onFailure(Call<Wee> call, Throwable t) {
            }
        });

    }
}
