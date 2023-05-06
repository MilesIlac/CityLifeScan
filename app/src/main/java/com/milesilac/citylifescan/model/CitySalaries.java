package com.milesilac.citylifescan.model;

import androidx.annotation.NonNull;

public class CitySalaries {

    private String title;
    private double percentile_25;
    private double percentile_50;
    private double percentile_75;

    public CitySalaries(String title, double percentile_25, double percentile_50, double percentile_75) {
        this.title = title;
        this.percentile_25 = percentile_25;
        this.percentile_50 = percentile_50;
        this.percentile_75 = percentile_75;
    }


    @NonNull
    @Override
    public String toString() {
        return "CitySalaries{" +
                "title='" + title + '\'' +
                ", percentile_25=" + percentile_25 +
                ", percentile_50=" + percentile_50 +
                ", percentile_75=" + percentile_75 +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPercentile_25() {
        return percentile_25;
    }

    public void setPercentile_25(double percentile_25) {
        this.percentile_25 = percentile_25;
    }

    public double getPercentile_50() {
        return percentile_50;
    }

    public void setPercentile_50(double percentile_50) {
        this.percentile_50 = percentile_50;
    }

    public double getPercentile_75() {
        return percentile_75;
    }

    public void setPercentile_75(double percentile_75) {
        this.percentile_75 = percentile_75;
    }
}
