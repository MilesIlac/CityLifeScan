package com.milesilac.citylifescan;

public class StringUtils {

    public static String ALL_URBAN_AREAS = "allUrbanAreas";
    public static String CITY_IMAGE_DETAILS = "imageDetails";
    public static String CITY_SUMMARY = "summary";
    public static String CITY_DETAILS = "details";
    public static String CITY_SCORES = "scores";
    public static String CITY_SALARIES = "salaries";

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
