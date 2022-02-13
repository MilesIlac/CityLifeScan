package com.milesilac.citylifescan;


public class CityScore {

    String name;
    int score;
    String color;
    CityDetails cityDetails;

    public CityScore(String name, int score, String color) {
        this.name = name;
        this.score = score;
        this.color = color;
    }


    public CityScore(String name, int score, String color, CityDetails cityDetails) {
        this.name = name;
        this.score = score;
        this.color = color;
        this.cityDetails = cityDetails;
    }


    public CityScore() {
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

}
