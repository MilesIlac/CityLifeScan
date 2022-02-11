package com.milesilac.citylifescan;

import java.util.ArrayList;

public class CityScore {

    String name;
    int score;
    String color;
    CityDetails cityDetails;
    ArrayList<CityDetails> cityDetailsArray;

    public CityScore(String name, int score, String color) {
        this.name = name;
        this.score = score;
        this.color = color;
    }

    public CityScore(ArrayList<CityDetails> cityDetailsArray) {
        this.cityDetailsArray = cityDetailsArray;
    }

    //    public CityScore(String name, int score, String color, ArrayList<CityDetails> cityDetails) {
//        this.name = name;
//        this.score = score;
//        this.color = color;
//        this.cityDetails = cityDetails;
//    }


    public CityScore(String name, int score, String color, CityDetails cityDetails) {
        this.name = name;
        this.score = score;
        this.color = color;
        this.cityDetails = cityDetails;
    }

    public CityScore() {
    }

    @Override
    public String toString() {
        return "CityScore{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", color=" + color +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CityDetails getCityDetails() {
        return cityDetails;
    }

    public void setCityDetails(CityDetails cityDetails) {
        this.cityDetails = cityDetails;
    }

        public ArrayList<CityDetails> getCityDetailsArray() {
        return cityDetailsArray;
    }

//    public void setCityDetails(ArrayList<CityDetails> cityDetails) {
//        this.cityDetails = cityDetails;
//    }
}
