package com.milesilac.citylifescan;

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


import java.util.ArrayList;


public class ScoreRecViewAdapter extends RecyclerView.Adapter<ScoreRecViewAdapter.ViewHolder> {

    ArrayList<CityScore> cityScoresList = new ArrayList<>();
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
        if (cityScoresList.get(position).getCityDetails().getCityDetailsName().equals(cityScoresList.get(position).getName())) {
            ArrayList<CityDetailsData> getEachCityDetailsData = new ArrayList<>(); //filters ArrayList<CityDetailsData>
            for (int i=0;i<cityScoresList.get(position).getCityDetails().getCityDetailsData().size();i++) {
                String scoreName = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getScoreName();
                if (scoreName.equals(cityScoresList.get(position).getName())) {
//                    System.out.println("getCityDetails (@scoresrecviewadapter): " + cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getLabelName()); //cityData element checker
                    String name = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getLabelName();
                    String type = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getType();
                    if (type.equals("float")) {
                        double decimal_value = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getDecimal_value();
                        getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,decimal_value));
                    }
                    if (type.equals("percent")) {
                        double decimal_value = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getDecimal_value();
                        getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,decimal_value));
                    }
                    if (type.equals("currency_dollar")) {
                        double decimal_value = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getDecimal_value();
                        getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,decimal_value));
                    }
                    if (type.equals("string")) {
                        String string_value = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getString_value();
                        getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,string_value));
                    }
                    if (type.equals("url")) {
                        String string_value = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getString_value();
                        getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,string_value));
                    }
                    if (type.equals("int")) {
                        int int_value = cityScoresList.get(position).getCityDetails().getCityDetailsData().get(i).getInt_value();
                        getEachCityDetailsData.add(new CityDetailsData(scoreName,name,type,int_value));
                    }
                }
            }
            scoreDetailsRecViewAdapter.setCityDetailsDataList(getEachCityDetailsData);
        }


        //set inner RecyclerView visibility
        holder.scoreDetailsTable.setVisibility(View.GONE);
        holder.scoreLayout.setOnClickListener(v -> {
            switch (holder.scoreDetailsTable.getVisibility()) {
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


    public void setCityScoresList(ArrayList<CityScore> cityScoresList) {
        this.cityScoresList = cityScoresList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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
