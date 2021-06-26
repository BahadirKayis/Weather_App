package com.layercontent.weather_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.layercontent.weather_app.Detalist;
import com.layercontent.weather_app.R;
import com.layercontent.weather_app.jsonpopjo.Condition;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;

public class Adapterhavadurumu extends RecyclerView.Adapter<Adapterhavadurumu.nesneler> {
    public Adapterhavadurumu(Context context, List<Condition> conditionLis) {
        this.context = context;
        this.conditionLis = conditionLis;
    }

    Context context;
    List<Condition>conditionLis;
    String resimid;


    @NonNull
    @Override
    public nesneler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stackhavadurumu, parent, false);

        return new nesneler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapterhavadurumu.nesneler holder, int position) {
        holder.days.setText(conditionLis.get(position).getText());
        resimid = conditionLis.get(position).getIcon();
        Picasso.get().load(resimid).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return conditionLis.size();
    }


    public class nesneler extends RecyclerView.ViewHolder {
        TextView days;
        ImageView image;

        public nesneler(@NonNull View itemView) {
            super(itemView);
            days = itemView.findViewById(R.id.stackgun);
            image = itemView.findViewById(R.id.stackresim);

        }
    }
}
