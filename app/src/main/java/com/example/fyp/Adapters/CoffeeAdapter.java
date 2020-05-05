package com.example.fyp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fyp.App.CoffeeSuggestionsActivity;
import com.example.fyp.Entities.places;
import com.example.fyp.R;

import java.util.ArrayList;


public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.MyViewHolder> {

    private ArrayList<places> dataSet;



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewPlaceName;
        TextView textViewPlaceRating;
        TextView textViewPlaceVicinity;
        TextView textViewDistance;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewPlaceName = itemView.findViewById(R.id.placeNameTextView);
            this.textViewPlaceRating =  itemView.findViewById(R.id.placeRatingTextView);
            this.textViewPlaceVicinity =  itemView.findViewById(R.id.placeVicinityTextView);
            this.textViewDistance = itemView.findViewById(R.id.placeDistanceTextView);
        }
    }

    public CoffeeAdapter(ArrayList<places> data){
        this.dataSet=data;
    }

    @NonNull
    @Override
    public CoffeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        view.setOnClickListener(CoffeeSuggestionsActivity.optionClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeAdapter.MyViewHolder holder, int position) {
        TextView textViewPlaceName = holder.textViewPlaceName;
        TextView textViewPlaceRating = holder.textViewPlaceRating;
        TextView textViewPlaceVicinity = holder.textViewPlaceVicinity;
        TextView textViewDistance = holder.textViewDistance;

        textViewPlaceName.setText(dataSet.get(position).getName());
        textViewPlaceRating.setText(dataSet.get(position).getRating());
        textViewPlaceVicinity.setText(dataSet.get(position).getVicinity());
        textViewDistance.setText("1.54km from here" );
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
