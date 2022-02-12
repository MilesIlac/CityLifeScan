package com.milesilac.citylifescan;

import java.util.ArrayList;

public class CityDetailsData {

    String scoreName;
    String labelName;
    String type;
    String string_value;
    double decimal_value;
    int int_value;

    public CityDetailsData(String labelName) {
        this.labelName = labelName;
    }

    public CityDetailsData(String scoreName, String labelName, String type, String string_value) {
        this.scoreName = scoreName;
        this.labelName = labelName;
        this.type = type;
        this.string_value = string_value;
    }

    public CityDetailsData(String scoreName, String labelName, String type, double decimal_value) {
        this.scoreName = scoreName;
        this.labelName = labelName;
        this.type = type;
        this.decimal_value = decimal_value;
    }

    public CityDetailsData(String scoreName, String labelName, String type, int int_value) {
        this.scoreName = scoreName;
        this.labelName = labelName;
        this.type = type;
        this.int_value = int_value;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getString_value() {
        return string_value;
    }

    public void setString_value(String string_value) {
        this.string_value = string_value;
    }

    public double getDecimal_value() {
        return decimal_value;
    }

    public void setDecimal_value(double decimal_value) {
        this.decimal_value = decimal_value;
    }

    public int getInt_value() {
        return int_value;
    }

    public void setInt_value(int int_value) {
        this.int_value = int_value;
    }
}
