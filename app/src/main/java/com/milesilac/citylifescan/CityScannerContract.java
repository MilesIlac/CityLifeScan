package com.milesilac.citylifescan;

import java.util.List;

public interface CityScannerContract {

    interface View {
        void populateCityNames(String[] countryNames);
        void setImageData(String imageUrl, String photographer, String source, String site, String license);
        void setBasicInfo(String basicInfo);
        void setCityDetails(List<CityDetails> cityDetails, String cityName);
        void setCitySummaryAndTeleportScore(String summary, String teleportScore);
        void setCityScores(List<CityScore> cityScores);
        void setCitySalariesData(List<CitySalaries> citySalaries);
    }

    interface Presenter {
        void checkCityName();
        void getScanResults(String cityName);
        void getScanResultsScores(List<CityDetails> cityDetails, String cityName);
    }
}
