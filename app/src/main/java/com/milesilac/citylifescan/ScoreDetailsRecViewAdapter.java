package com.milesilac.citylifescan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreDetailsRecViewAdapter extends RecyclerView.Adapter<ScoreDetailsRecViewAdapter.ViewHolder> {

    CityDetails cityDetails;

    Context context;

    public ScoreDetailsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_details_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.scoreDetailName.setText(cityDetails.getCityDetailsName());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

//    public void setCityDetails(ArrayList<CityDetails> cityDetails) {
//        this.cityDetails = cityDetails;
//        notifyDataSetChanged();
//    }

    public void setCityDetails(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView scoreDetailName, scoreDetailValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scoreDetailName = itemView.findViewById(R.id.scoreDetailName);
            scoreDetailValue = itemView.findViewById(R.id.scoreDetailValue);
        }


    }
}
