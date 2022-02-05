package com.milesilac.citylifescan;

import android.content.res.ColorStateList;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ScoreRecViewAdapter extends RecyclerView.Adapter<ScoreRecViewAdapter.ViewHolder> {



    ArrayList<CityScore> cityScoresList = new ArrayList<>();

    public ScoreRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.scoreName.setText(cityScoresList.get(position).getName());
        holder.scoreRating.setProgress(cityScoresList.get(position).getScore());
        int currentColor = Color.parseColor(cityScoresList.get(position).getColor());

        holder.scoreRating.setProgressTintList(ColorStateList.valueOf(currentColor));
    }

    @Override
    public int getItemCount() {
        return cityScoresList.size();
    }

    public void setCityScoresList(ArrayList<CityScore> cityScoresList) {
        this.cityScoresList = cityScoresList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView scoreName;
        ProgressBar scoreRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scoreName = itemView.findViewById(R.id.scoreName);
            scoreRating = itemView.findViewById(R.id.scoreRating);
        }


    }
}
