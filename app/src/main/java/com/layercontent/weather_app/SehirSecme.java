package com.layercontent.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.layercontent.weather_app.Retrofit.ManegarAll;
import com.layercontent.weather_app.adapter.SehirAdapter;
import com.layercontent.weather_app.jsonpopjo.SehirCevap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SehirSecme extends AppCompatActivity implements SearchView.OnQueryTextListener  {
    RecyclerView recyclerViewsehir;
    Toolbar toolbar;
    SehirAdapter sehirAdapter;

    List<SehirCevap> listsehirle;
    SehirCevap sehirCevap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sehir_secme);

        recyclerViewsehir = findViewById(R.id.recysehir);
        recyclerViewsehir.setHasFixedSize(true);
        recyclerViewsehir.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SehirlerTR();
        Toolbar();


    }


    public void SehirlerTR() {
        Call<List<SehirCevap>> call = ManegarAll.getInstance().getirsehirleri();
        call.enqueue(new Callback<List<SehirCevap>>() {
            @Override
            public void onResponse(Call<List<SehirCevap>> call, Response<List<SehirCevap>> response) {
                if (response.isSuccessful()) {

                    SehirAdapter sehirAdapter = new SehirAdapter(SehirSecme.this, response.body());
                    recyclerViewsehir.setAdapter(sehirAdapter);



                }
            }

            @Override
            public void onFailure(Call<List<SehirCevap>> call, Throwable t) {

            }
        });


    }

    @SuppressLint("ResourceAsColor")
    public void Toolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Arama");
        toolbar.setTitleTextColor(R.color.black);
        setSupportActionBar(toolbar);
        


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sehirarama, menu);
        MenuItem item = menu.findItem(R.id.arama);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.trim().length()>=1){


        List<SehirCevap> newsehir=new ArrayList<>();
        for (SehirCevap s:listsehirle){
            if (s.getName().toLowerCase().contains(newText.toLowerCase().trim())){
                newsehir.add(s);
            }
        }
        sehirAdapter.setSehirCevapList(newsehir);
        sehirAdapter.notifyDataSetChanged();
        }
        return false;
    }
}
