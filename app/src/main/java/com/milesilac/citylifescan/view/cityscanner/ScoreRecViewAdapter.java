package com.milesilac.citylifescan.view.cityscanner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.milesilac.citylifescan.R;
import com.milesilac.citylifescan.model.CityDetails;
import com.milesilac.citylifescan.model.CityDetailsData;
import com.milesilac.citylifescan.model.CityScore;

import java.util.ArrayList;
import java.util.List;


public class ScoreRecViewAdapter extends RecyclerView.Adapter<ScoreRecViewAdapter.ViewHolder> {

    List<CityScore> cityScoresList = new ArrayList<>();
    Context context;

    public ScoreRecViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_layout,parent,false);
        return new ViewHolder(view);
    } //onCreateViewHolder


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //set ScoreDetailsRecViewAdapter
        ScoreDetailsRecViewAdapter scoreDetailsRecViewAdapter = new ScoreDetailsRecViewAdapter(context);
        holder.scoreDetailsRecView.setAdapter(scoreDetailsRecViewAdapter);
        holder.scoreDetailsRecView.setLayoutManager(new LinearLayoutManager(context));

        //-- set Score name, score rating, and color
        holder.scoreName.setText(cityScoresList.get(position).getName()); //set Score name
        holder.scoreRating.setProgress(cityScoresList.get(position).getScore()); //set Score Value
        int currentColor = Color.parseColor(cityScoresList.get(position).getColor());
        holder.scoreRating.setProgressTintList(ColorStateList.valueOf(currentColor)); //set Score color
        //--

        //-- get details list
        CityDetails cityDetails = cityScoresList.get(position).getCityDetails();
        if (cityDetails.getCityDetailsName().equals(cityScoresList.get(position).getName())) {
            ArrayList<CityDetailsData> getEachCityDetailsData = new ArrayList<>();
            for (int i = 0, size = cityDetails.getCityDetailsData().size() ; i < size ; i++) {
                CityDetailsData cityDetailsData = cityDetails.getCityDetailsData().get(i);
                String scoreName = cityDetailsData.getScoreName();
                if (scoreName.equals(cityScoresList.get(position).getName())) {
                    String name = cityDetailsData.getLabelName();
                    String type = cityDetailsData.getType();
                    String value = cityDetailsData.getValue();
                    getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,value));
                }
            }
            scoreDetailsRecViewAdapter.setCityDetailsDataList(getEachCityDetailsData);
        }


        //set inner RecyclerView visibility
        holder.scoreDetailsTable.setVisibility(View.GONE);
        holder.scoreLayout.setOnClickListener(v -> {
            switch (holder.scoreDetailsTable.getVisibility()) {
                case View.INVISIBLE:
                case View.GONE:
                    holder.scoreDetailsTable.setVisibility(View.VISIBLE);
                    break;
                case View.VISIBLE:
                    holder.scoreDetailsTable.setVisibility(View.GONE);
                    break;
            }
        });

    } //onBindViewHolder


    @Override
    public int getItemCount() {
        return cityScoresList.size();
    }


    public void setCityScoresList(List<CityScore> cityScoresList) {
        this.cityScoresList = cityScoresList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView scoreName;
        ProgressBar scoreRating;
        LinearLayout scoreLayout, scoreDetailsTable;
        RecyclerView scoreDetailsRecView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scoreName = itemView.findViewById(R.id.scoreName);
            scoreRating = itemView.findViewById(R.id.scoreRating);
            scoreLayout = itemView.findViewById(R.id.scoreLayout);
            scoreDetailsTable = itemView.findViewById(R.id.scoreDetailsTable);
            scoreDetailsRecView = itemView.findViewById(R.id.scoreDetailsRecView);
        }

    }

}
