package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class weatherRVAdapter extends RecyclerView.Adapter<weatherRVAdapter.viewHolder> {

    private Context context;
    private ArrayList<weatherRVModel> weatherRVModelArrayList;

    public weatherRVAdapter(Context context, ArrayList<weatherRVModel> weatherRVModelArrayList) {
        this.context = context;
        this.weatherRVModelArrayList = weatherRVModelArrayList;
    }

    @NonNull
    @Override
    public weatherRVAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        weatherRVModel model =  weatherRVModelArrayList.get(position);
        holder.tempratureTV.setText(model.getTemprature().concat("°c"));
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(model.getWindSpeed().concat("k/h"));
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh-mm");
        SimpleDateFormat output = new SimpleDateFormat("hh-mm aa");
        try {
            Date t = input.parse(model.getTime());
            holder.timeTV.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherRVModelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView windTV, tempratureTV, timeTV;
        private ImageView conditionIV;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            tempratureTV = itemView.findViewById(R.id.idTVTTemprature);
            timeTV = itemView.findViewById(R.id.idTVTime);
           conditionIV = itemView.findViewById(R.id.idTVCondition);
        }
    }
}
