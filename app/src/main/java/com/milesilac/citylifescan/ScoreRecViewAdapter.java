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

    public static String getCurrentScoreName;

    public ScoreRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ScoreDetailsRecViewAdapter scoreDetailsRecViewAdapter = new ScoreDetailsRecViewAdapter(context);
        holder.scoreDetailsRecView.setAdapter(scoreDetailsRecViewAdapter);
        holder.scoreDetailsRecView.setLayoutManager(new LinearLayoutManager(context));

        //-- set Score name, score rating, and color
        holder.scoreName.setText(cityScoresList.get(position).getName());
        if (cityScoresList.get(position).getName().equals("Housing")) {
            getCurrentScoreName = cityScoresList.get(position).getName();
        }
        holder.scoreRating.setProgress(cityScoresList.get(position).getScore());

        int currentColor = Color.parseColor(cityScoresList.get(position).getColor());
        holder.scoreRating.setProgressTintList(ColorStateList.valueOf(currentColor));
        //--

        //-- get details list
//        scoreDetailsRecViewAdapter.setCityDetails(cityScoresList.get(position).getCityDetails());
        scoreDetailsRecViewAdapter.setCityDetails(cityScoresList.get(position).getCityDetails());






        holder.scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //on item click template
//                if (holder.getAdapterPosition() == 0) {
//                    pieChartDialog.show();
//                }
                holder.scoreDetailsTable.setVisibility(View.VISIBLE);
            }
        });



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
