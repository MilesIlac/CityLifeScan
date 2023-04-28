package com.milesilac.citylifescan.view.cityscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milesilac.citylifescan.R;
import com.milesilac.citylifescan.model.CityDetailsData;

import java.util.ArrayList;

public class ScoreDetailsRecViewAdapter extends RecyclerView.Adapter<ScoreDetailsRecViewAdapter.ViewHolder> {

    ArrayList<CityDetailsData> cityDetailsData = new ArrayList<>();
    Context context;

    public ScoreDetailsRecViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_details_layout,parent,false);
        return new ViewHolder(view);
    } //onCreateViewHolder


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.scoreDetailName.setText((cityDetailsData.get(position).getLabelName()));
        String value = cityDetailsData.get(position).getValue();
        if (cityDetailsData.get(position).getType().equals("float")) {
            double getFloat = Double.parseDouble(value);
            double formatFloat = Math.round(getFloat*100.0)/100.0;
            holder.scoreDetailValue.setText(String.valueOf(formatFloat));
        }
        if (cityDetailsData.get(position).getType().equals("percent")) {
            double getPercentage = Double.parseDouble(value);
            double formatPercentage = Math.round(getPercentage*100.0)/100.0;
            String percent = formatPercentage + "%";
            holder.scoreDetailValue.setText(percent);
        }
        if (cityDetailsData.get(position).getType().equals("currency_dollar")) {
            String price = "$" + value;
            holder.scoreDetailValue.setText(price);
        }
        if (cityDetailsData.get(position).getType().equals("int")) {
            int getInt = (int) Double.parseDouble(value);
            holder.scoreDetailValue.setText(String.valueOf(getInt));
        }
        if (cityDetailsData.get(position).getType().equals("string")) {
            holder.scoreDetailValue.setText(value);
        }
        if (cityDetailsData.get(position).getType().equals("url")) {
            holder.scoreDetailValue.setText(value);
        }

    } //onBindViewHolder


    @Override
    public int getItemCount() {
        return cityDetailsData.size();
    }


    public void setCityDetailsDataList(ArrayList<CityDetailsData> cityDetailsData) {
        this.cityDetailsData = cityDetailsData;
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
