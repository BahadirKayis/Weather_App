package com.layercontent.weather_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.layercontent.weather_app.MainActivity;
import com.layercontent.weather_app.R;
import com.layercontent.weather_app.jsonpopjo.SehirCevap;

import java.util.ArrayList;
import java.util.List;

public class SehirAdapter extends RecyclerView.Adapter<SehirAdapter.tanimlama> implements Filterable {
    Context context;
    List<SehirCevap> sehirCevapList;
    List<SehirCevap> ListFull;

    public SehirAdapter(Context context, List<SehirCevap> sehirCevapList) {
        this.context = context;
        this.sehirCevapList = sehirCevapList;
        ListFull = new ArrayList<>(sehirCevapList);
    }

    @NonNull

    @Override
    public tanimlama onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sehirlayoutselect, parent, false);

        return new tanimlama(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SehirAdapter.tanimlama holder, int position) {
        holder.textsehir.setText(sehirCevapList.get(position).getName());
        holder.testplaka.setText(sehirCevapList.get(position).getId().toString());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Intent i = new Intent(context, MainActivity.class);
                                                       i.putExtra("sehir", sehirCevapList.get(position).getName());
                                                       context.startActivity(i);
                                                   }
                                               }
        );
    }

    @Override
    public int getItemCount() {
        return sehirCevapList.size();
    }

    @Override
    public Filter getFilter() {

        return examplefilter;
    }//tweqw

    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SehirCevap> filtredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filtredlist.addAll(ListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SehirCevap item:ListFull){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filtredlist.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filtredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          sehirCevapList.clear();
          sehirCevapList.addAll((List)results.values);
          notifyDataSetChanged();


        }
    };

    public class tanimlama extends RecyclerView.ViewHolder {
        TextView testplaka, textsehir;
        LinearLayout linearLayout;

        public tanimlama(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearsehir);
            testplaka = itemView.findViewById(R.id.sehirid);
            textsehir = itemView.findViewById(R.id.sehirname);
        }
    }

}
