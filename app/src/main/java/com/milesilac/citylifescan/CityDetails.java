package com.milesilac.citylifescan;

import java.util.ArrayList;

public class CityDetails {

    String cityDetailsName;
    ArrayList<CityDetailsData> cityDetailsData;

    public CityDetails(String cityDetailsName) {
        this.cityDetailsName = cityDetailsName;
    }

    public CityDetails(ArrayList<CityDetailsData> cityDetailsData) {
        this.cityDetailsData = cityDetailsData;
    }

    public CityDetails(String cityDetailsName, ArrayList<CityDetailsData> cityDetailsData) {
        this.cityDetailsName = cityDetailsName;
        this.cityDetailsData = cityDetailsData;
    }

    public String getCityDetailsName() {
        return cityDetailsName;
    }

    public void setCityDetailsName(String cityDetailsName) {
        this.cityDetailsName = cityDetailsName;
    }

    public ArrayList<CityDetailsData> getCityDetailsData() {
        return cityDetailsData;
    }

    public void setCityDetailsData(ArrayList<CityDetailsData> cityDetailsData) {
        this.cityDetailsData = cityDetailsData;
    }
}
