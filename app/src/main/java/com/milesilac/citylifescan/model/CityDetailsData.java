package com.milesilac.citylifescan.model;


public class CityDetailsData {

    private String scoreName;
    private String labelName;
    private  String type;
    private String value;

    public CityDetailsData(String scoreName, String labelName, String type, String value) {
        this.scoreName = scoreName;
        this.labelName = labelName;
        this.type = type;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
