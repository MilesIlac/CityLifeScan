package com.milesilac.citylifescan;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class ScoreRecViewAdapter extends RecyclerView.Adapter<ScoreRecViewAdapter.ViewHolder> {


    Dialog pieChartDialog;

    ArrayList<CityScore> cityScoresList = new ArrayList<>();

    Context context;

//    public ScoreRecViewAdapter() {
//    }

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

//        //Dialog template
//        pieChartDialog = new Dialog(context);
//        pieChartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pieChartDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        pieChartDialog.setContentView(R.layout.salary_chart_layout);
//        pieChartDialog.setCancelable(true);


        holder.scoreName.setText(cityScoresList.get(position).getName());
        holder.scoreRating.setProgress(cityScoresList.get(position).getScore());

        System.out.println("Name: " + holder.scoreName.getText().toString() + "\n" +
                           "Position: " + position);

        int currentColor = Color.parseColor(cityScoresList.get(position).getColor());
        holder.scoreRating.setProgressTintList(ColorStateList.valueOf(currentColor));

        holder.scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //on item click template
//                if (holder.getAdapterPosition() == 0) {
//                    pieChartDialog.show();
//                }

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
        TableLayout scoreLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            scoreName = itemView.findViewById(R.id.scoreName);
            scoreRating = itemView.findViewById(R.id.scoreRating);
            scoreLayout = itemView.findViewById(R.id.scoreLayout);
        }


    }

}
