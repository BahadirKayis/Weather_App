package com.layercontent.weather_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.layercontent.weather_app.Retrofit.ManegarAll;
import com.layercontent.weather_app.jsonpopjo.Wee;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private int izinkontrol;
    int click;

    ImageView add;
    String resimid;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView text;
    //Linear1
    TextView lnsehir, ulke, derece;
    ImageView havaresim1;
    //linear2
    TextView lnsehir2, ulke2, derece2;
    ImageView havaresim2;
    ///////
    //linear3
    TextView lnsehir3, ulke3, derece3;
    ImageView havaresim3;
    ///////
    //linear4
    TextView lnsehir4, ulke4, derece4;
    ImageView havaresim4;
    ///////
    double longitude;
    double latitude;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    LinearLayout linear1, linear2, linear3, linear4;
    //havadurumuapi
    final String api = "f4addb85b5e4492f8ed115420211606";
    String secilensehir;

    private SharedPreferences sh;
    private SharedPreferences.Editor shE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sh = getSharedPreferences("SehirSayaci", MODE_PRIVATE);

        shE = sh.edit();
  /* shE.remove("sayac").apply();
          shE.putString("Sehir1",null);
        shE.putString("Sehir2",null);
        shE.putString("Sehir3",null);
        shE.commit();*/
        add = findViewById(R.id.imageadd);
        add.setOnClickListener(this);
        text = findViewById(R.id.linear1text1);
        linear1 = findViewById(R.id.linearLayout1);
        linear2 = findViewById(R.id.linearLayout2);
        linear3 = findViewById(R.id.linearLayout3);
        linear4 = findViewById(R.id.linearLayout4);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sh.getString("resim1", null) != null) {
                    String country = text.getText().toString().trim();
                    Intent i = new Intent(MainActivity.this, Detalist.class);
                    i.putExtra("resim", sh.getString("resim1", resimid));
                    i.putExtra("country", country);
                    startActivity(i);
                }
            }
        });
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sh.getString("resim2", null) != null) {

                    Intent i = new Intent(MainActivity.this, Detalist.class);
                    i.putExtra("resim", sh.getString("resim2", resimid));
                    i.putExtra("country", lnsehir2.getText().toString());
                    startActivity(i);
                }
            }
        });
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sh.getString("resim3", null) != null) {

                    Intent i = new Intent(MainActivity.this, Detalist.class);
                    i.putExtra("resim", sh.getString("resim3", resimid));
                    i.putExtra("country", lnsehir3.getText().toString());
                    startActivity(i);
                }
            }
        });
        linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sh.getString("resim4", null) != null) {

                    Intent i = new Intent(MainActivity.this, Detalist.class);
                    i.putExtra("resim", sh.getString("resim4", resimid));
                    i.putExtra("country", lnsehir4.getText().toString());
                    startActivity(i);
                }
            }
        });
        anlikkonum();
        ///
        tanim();

        secilensehir = getIntent().getStringExtra("sehir");
        ListSehirsec();


/*if (click==2){
    Log.i("Sehir1",sh.getString("Sehir1","gelmedi") );
    Log.i("Sehir2",sh.getString("Sehir2","gelmedi") );
    todaysweatherlinear2(api, sh.getString("Sehir1",null), 3, "tr");
    todaysweatherlinear3(api, sh.getString("Sehir2",null), 3, "tr");
        }
 if (click==1){
    todaysweatherlinear2(api, sh.getString("Sehir1","gelmedi"), 3, "tr");
}*/

    }

    private void todaysweatherlinear4(String api, String sehir2, int i, String tr) {
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir2, i, tr);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(Call<Wee> call, Response<Wee> response) {
                if (response.isSuccessful()){
                final int lenght = response.body().getLocation().getLocaltime().length();
                lnsehir4.setText(sh.getString("Sehir3", "gelmedi"));
                ulke4.setText(response.body().getLocation().getCountry() + "," + response.body().getLocation().getLocaltime().substring(10, lenght));

                String s = String.format("%.0f", response.body().getCurrent().getTempC());
                derece4.setText(s + "°");

                String resim = "http:" + response.body().getCurrent().getCondition().getIcon();// http://cdn.weatherapi.com/weather/64x64/day/116.png
                String havatexti = response.body().getCurrent().getCondition().getText();
                todaysweatherresim(havatexti, resim, havaresim4);
                }
            }

            @Override
            public void onFailure(Call<Wee> call, Throwable t) {
            }
        });
    }

    public void ListSehirsec() {

        click = sh.getInt("sayac", 0);
        switch (click) {

            case 1:
                if (secilensehir != null) {
                    shE.putString("Sehir1", secilensehir);
                    shE.commit();
                }

                break;
            case 2:
                if (secilensehir != null) {

                 //   secilensehir = sh.getString("Sehir2", secilensehir);
                    shE.putString("Sehir2", secilensehir);
                    shE.commit();
                }
                break;
            case 3:

                if (secilensehir != null) {
                    //secilensehir = sh.getString("Sehir3", secilensehir);
                    shE.putString("Sehir3", secilensehir);
                    shE.putInt("sayac", 0);
                    shE.commit();
                    click=sh.getInt("sayac",0);
                }

                break;

        }

        if (sh.getString("Sehir1", null) != null) {
            todaysweatherlinear2(api, sh.getString("Sehir1", null), 3, "tr");
            linear2.setVisibility(View.VISIBLE);


        }
        if (sh.getString("Sehir2", null) != null) {
            todaysweatherlinear3(api, sh.getString("Sehir2", null), 3, "tr");
            linear3.setVisibility(View.VISIBLE);

        }
        if (sh.getString("Sehir3", null) != null) {
            todaysweatherlinear4(api, sh.getString("Sehir3", null), 3, "tr");
            linear4.setVisibility(View.VISIBLE);

        }
        Log.i("Sehir", sh.getString("Sehir1","Gelmedi1"));
        Log.i("Sehir", sh.getString("Sehir2","Gelmedi2"));
        Log.i("Sehir", sh.getString("Sehir3","Gelmedi3"));
        Log.i("Sehir", String.valueOf(sh.getInt("sayac",0)));
        Log.i("Sehir", String.valueOf(click)+"Clik");

    }

    public void tanim() {
        lnsehir = findViewById(R.id.linear1text1);
        ulke = findViewById(R.id.linear2text2);
        derece = findViewById(R.id.linear1text3);
        havaresim1 = findViewById(R.id.linear1image);

        lnsehir2 = findViewById(R.id.linear2text1);
        ulke2 = findViewById(R.id.lineart2ext2);
        derece2 = findViewById(R.id.linear2text3);
        havaresim2 = findViewById(R.id.linear2image);

        lnsehir3 = findViewById(R.id.linear3text1);
        ulke3 = findViewById(R.id.lineart3ext2);
        derece3 = findViewById(R.id.linear3text3);
        havaresim3 = findViewById(R.id.linear3image);

        lnsehir4 = findViewById(R.id.linear4text1);
        ulke4 = findViewById(R.id.linear42ext2);
        derece4 = findViewById(R.id.linear4text3);
        havaresim4 = findViewById(R.id.linear4image);
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
                click++;
                shE.putInt("sayac", click);
                shE.commit();

                startActivity(new Intent(MainActivity.this, SehirSecme.class));

                break;
           /*  case R.id.linearLayout1:
                String country = text.getText().toString().trim();
                Intent i = new Intent(MainActivity.this, Detalist.class);
                i.putExtra("resim", sh.getString("resim1", resimid));
                i.putExtra("country", country);
                startActivity(i);
                break;
            case R.id.linearLayout2:
                String lnsehirr = lnsehir.getText().toString().trim();
                Intent i2 = new Intent(MainActivity.this, Detalist.class);
                i2.putExtra("resim", sh.getString("resim2", resimid));
                i2.putExtra("country", lnsehirr);
                startActivity(i2);
                break;

            case R.id.linearLayout3:
                String lnsehir2r = lnsehir2.getText().toString().trim();
                Intent i3 = new Intent(MainActivity.this, Detalist.class);
                i3.putExtra("resim", sh.getString("resim3", resimid));
                i3.putExtra("country", lnsehir2r);
                startActivity(i3);
                break;

            case R.id.linearLayout4:
                String lnsehirr4 = lnsehir4.getText().toString().trim();
                Intent i4 = new Intent(MainActivity.this, Detalist.class);
                i4.putExtra("resim", sh.getString("resim4", resimid));
                i4.putExtra("country", lnsehirr4);
                startActivity(i4);
                break;
*/
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

    public void todaysweatherresim(String durum, String resimtext, ImageView havaresim) {


        if (durum.equals("Güneşli")) {

            havaresim.setImageResource(R.drawable.ic_sunny);
            resimid = String.valueOf(R.drawable.ic_sunny);
        } else if (durum.equals("Bulutlu")
                & durum.equals("Çok Bulutlu")) {
            havaresim.setImageResource(R.drawable.ic_clouds);
            resimid = String.valueOf(R.drawable.ic_clouds);
        } else if (durum.equals("Parçalı Bulutlu")
        ) {
            havaresim.setImageResource(R.drawable.ic_pblut);///BU KODU ALIPGÖNDERCEM
            resimid = String.valueOf(R.drawable.ic_pblut);
        }
        //YAĞMUR&KAR
        else if (durum.equals("Hafif yağmurlu")
                || durum.equals("Düzensiz hafif yağmurlu")
                || durum.equals("Hafif sağnak yağışlı")) {

            havaresim.setImageResource(R.drawable.ic_hafifyamur);
            resimid = String.valueOf(R.drawable.ic_hafifyamur);
        } else if (durum.equals("Bölgesel düzensiz yağmur yağışlı")
                || durum.equals("Ara ara orta kuvvetli yağmurlu")
                || durum.equals("Orta kuvvetli yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_yamur);
            resimid = String.valueOf(R.drawable.ic_yamur);
        } else if (durum.equals("Şiddetli yağmurlu")
                || durum.equals("Bölgesel düzensiz gök gürültülü yağmurlu")
                || durum.equals("Orta kuvvetli veya yoğun sağnak yağışlı")
                || durum.equals("Şiddetli sağnak yağmur")
                || durum.equals("Ara ara şiddetli yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_saganakyamur);
            resimid = String.valueOf(R.drawable.ic_saganakyamur);

        } else if (durum.equals("Hafif buz taneleri şeklinde sağnak yağış")
                || durum.equals("Hafif karla karışık yağmur")
                || durum.equals("Hafif dondurucu yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_karlayamur);
            resimid = String.valueOf(R.drawable.ic_karlayamur);

        } else if (durum.equals("Orta kuvvetli veya yoğun buz taneleri sağnak yağışlı")
                || durum.equals("Orta kuvvetli veya şiddetli karla karışık yağmur")
                || durum.equals("Orta kuvvetli veya Şiddetli dondurucu yağmurlu")) {
            havaresim.setImageResource(R.drawable.ic_yukselikarlayamur);
            resimid = String.valueOf(R.drawable.ic_yukselikarlayamur);

        } else if (durum.equals("Hafif sağnak şeklinde kar")
                || durum.equals("Düzensiz hafif karlı")
                || durum.equals("Hafif karlı")) {
            havaresim.setImageResource(R.drawable.ic_hafifkar);
            resimid = String.valueOf(R.drawable.ic_hafifkar);

        } else if (durum.equals("Düzensiz yoğun kar yağışlı")
                || durum.equals("Orta kuvvetli veya yoğun ve sağnak şeklinde kar")
                || durum.equals("Yoğun kar yağışlı")) {
            havaresim.setImageResource(R.drawable.ic_yukkar);
            resimid = String.valueOf(R.drawable.ic_yukkar);

        } else if (durum.equals("Orta kuvvetli karlı") || durum.equals("Bölgesel düzensiz kar yağışlı")) {
            havaresim.setImageResource(R.drawable.ic_kar);
            resimid = String.valueOf(R.drawable.ic_kar);

        } else if (durum.equals("Kar fırtınası")
        ) {
            havaresim.setImageResource(R.drawable.ic_firtinakar);
            resimid = String.valueOf(R.drawable.ic_firtinakar);

        } else if (durum.equals("Puslu")
                || durum.equals("Dondurucu sis")
                || durum.equals("Tipi")
                || durum.equals("Sisli")) {
            havaresim.setImageResource(R.drawable.ic_sisli);
            resimid = String.valueOf(R.drawable.ic_sisli);

        } else {
            Picasso.get().load(resimtext).into(havaresim);
            resimid = resimtext;
        }

        if (havaresim.getId() == havaresim1.getId()) {
            shE.putString("resim1", resimid);
            shE.commit();
        }
        if (havaresim.getId() == havaresim2.getId()) {
            shE.putString("resim2", resimid);
            shE.commit();
        }
        if (havaresim.getId() == havaresim3.getId()) {
            shE.putString("resim3", resimid);
            shE.commit();
        }
        if (havaresim.getId() == havaresim4.getId()) {
            shE.putString("resim4", resimid);
            shE.commit();
        }

    }

    public void todaysweather(String api, String sehir, int days, String lang) {//anlıkkonumda çağırdık bunu
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days, lang);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(Call<Wee> call, Response<Wee> response) {
                final int lenght = response.body().getLocation().getLocaltime().length();
                ulke.setText(response.body().getLocation().getCountry() + "," + response.body().getLocation().getLocaltime().substring(10, lenght));

                String s = String.format("%.0f", response.body().getCurrent().getTempC());
                derece.setText(s + "°");

                String resim = "http:" + response.body().getCurrent().getCondition().getIcon();// http://cdn.weatherapi.com/weather/64x64/day/116.png
                String havatexti = response.body().getCurrent().getCondition().getText();
                todaysweatherresim(havatexti, resim, havaresim1);

            }

            @Override
            public void onFailure(Call<Wee> call, Throwable t) {
            }
        });

    }

    public void todaysweatherlinear2(String api, String sehir, int days, String lang) {//anlıkkonumda çağırdık bunu
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days, lang);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(Call<Wee> call, Response<Wee> response) {
                if (response.isSuccessful()) {
                    final int lenght = response.body().getLocation().getLocaltime().length();
                    lnsehir2.setText(sh.getString("Sehir1", "gelmedi"));
                    ulke2.setText(response.body().getLocation().getCountry() + "," + response.body().getLocation().getLocaltime().substring(10, lenght));

                    String s = String.format("%.0f", response.body().getCurrent().getTempC());
                    derece2.setText(s + "°");

                    String resim = "http:" + response.body().getCurrent().getCondition().getIcon();// http://cdn.weatherapi.com/weather/64x64/day/116.png
                    String havatexti = response.body().getCurrent().getCondition().getText();
                    todaysweatherresim(havatexti, resim, havaresim2);
                }

            }

            @Override
            public void onFailure(Call<Wee> call, Throwable t) {
            }
        });

    }

    public void todaysweatherlinear3(String api, String sehir, int days, String lang) {//anlıkkonumda çağırdık bunu
        Call<Wee> call = ManegarAll.getInstance().getirbilgier(api, sehir, days, lang);
        call.enqueue(new Callback<Wee>() {
            @Override
            public void onResponse(Call<Wee> call, Response<Wee> response) {
                if (response.isSuccessful()){
                final int lenght = response.body().getLocation().getLocaltime().length();
                lnsehir3.setText(sh.getString("Sehir2", "gelmedi"));
                ulke3.setText(response.body().getLocation().getCountry() +
                        "," + response.body().getLocation().getLocaltime().substring(10, lenght));

                String s = String.format("%.0f", response.body().getCurrent().getTempC());
                derece3.setText(s + "°");

                String resim = "http:" + response.body().getCurrent().getCondition().getIcon();// http://cdn.weatherapi.com/weather/64x64/day/116.png
                String havatexti = response.body().getCurrent().getCondition().getText();
                todaysweatherresim(havatexti, resim, havaresim3);
                }
            }

            @Override
            public void onFailure(Call<Wee> call, Throwable t) {
            }
        });

    }


   /* public void SehirlerTR() {
        Call<List<SehirCevap>>call = ManegarAll.getInstance().getirsehirleri();
        call.enqueue(new Callback<List<SehirCevap>>() {
            @Override
            public void onResponse(Call<List<SehirCevap>> call, Response<List<SehirCevap>> response) {
                if (response.isSuccessful())
                {
                    SehirAdapter sehirAdapter=new SehirAdapter(MainActivity.this,response.body());

                }
            }

            @Override
            public void onFailure(Call<List<SehirCevap>> call, Throwable t) {

            }
        });



    }*/
}
