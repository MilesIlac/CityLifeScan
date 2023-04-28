package com.milesilac.citylifescan;

public class StringUtils {

    public static String formatCityName(String cityName) {
        cityName = cityName.toLowerCase();
        if (cityName.contains(", ")) {
            cityName = cityName.replace(", ","-");
        }
        else if (cityName.contains(". ")) {
            cityName = cityName.replace(". ","-");
        }
        else if (cityName.contains(" ")) {
            cityName = cityName.replace(" ","-");
        }
        if (cityName.contains(".")) {
            cityName = cityName.replace(".","");
        }
        return cityName;
    }
}
