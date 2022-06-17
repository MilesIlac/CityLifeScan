package com.milesilac.citylifescan;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

public interface CityContract {

    interface View {

    }

    interface CityDataRepository {
        void setAllCityNames(String[] allCityNames);
        String[] getAllCityNames();

        void setImageData(String[] imageDataArray);
        String[] getImageData();
        String getImgUrl();
        String getPhotographer();
        String getSource();
        String getSite();
        String getLicense();

        void setBasicInfo(String basicInfo);
        String getBasicInfo();

        void setCityDetailsData(ArrayList<CityDetails> cityDetails);
        ArrayList<CityDetails> getCityDetailsData();

        void setCityTeleportScore(String teleportScore);
        void setCitySummary(String summary);
        void setCityScores(ArrayList<CityScore> cityScores);
        String getTeleportScore();
        String getSummary();
        ArrayList<CityScore> getCityScores();

        void setCitySalaries(ArrayList<CitySalaries> citySalaries);
        ArrayList<CitySalaries> getCitySalaries();
    }



    interface CityScanResults {
        String[] readImageDataArray();
        String readImgUrl();
        String readPhotographer();
        String readSource();
        String readSite();
        String readLicense();

        String readBasicInfo();

        String readSummary();
        String readTeleportCityScore();
        ArrayList<CityScore> readCityScores();
    }

}
