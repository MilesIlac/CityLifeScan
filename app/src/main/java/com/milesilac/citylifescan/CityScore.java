package com.milesilac.citylifescan;

public class CityScore {

    String name;
    int score;
    String color;

    public CityScore(String name, int score, String color) {
        this.name = name;
        this.score = score;
        this.color = color;
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
}
