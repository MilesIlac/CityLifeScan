package com.milesilac.citylifescan;


import android.content.Context;

import androidx.core.text.HtmlCompat;

import com.milesilac.citylifescan.model.CityDataRepository;

import java.util.ArrayList;


public class CityScanResults implements CityContract.CityScanResults {

    CityContract.CityDataRepository cityDataRepository = new CityDataRepository();
    CityScannerService cityScannerService;

    Context context;

    public CityScanResults(Context context) {
        this.context = context;
        cityScannerService = new CityScannerService(context);
    }



    public void getScanResults(String cityName) {

        cityScannerService.getScanResultsImage(cityName, new CityScannerService.VolleyImageResponseListener() {

            @Override
            public void onError(String message) {
//                Toast.makeText(MainActivity.this, "There is a picture error (main)", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String imgUrl, String photographer, String source, String site, String license) {
                System.out.println("This is working. Proof: " + photographer);

                String[] imageData = {imgUrl,photographer,source,site,license};

                cityDataRepository.setImageData(imageData);

            } //onResponse

        }); //get city image



        cityScannerService.getScanResultsBasicInfo(cityName, new CityScannerService.VolleyResponseListener() {

            @Override
            public void onError(String message) {
//                Toast.makeText(MainActivity.this, "There is a basic info error (main)", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String string) {

                cityDataRepository.setBasicInfo(string);
            }

        }); //get city basic info


        cityScannerService.getScanResultsCityDetails(cityName, new CityScannerService.VolleyDetailsResponseListener() {
            @Override
            public void onError(String message) {
//                Toast.makeText(MainActivity.this, "There is a details error (main)", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(ArrayList<CityDetails> cityDetails) {

                cityDataRepository.setCityDetailsData(cityDetails);

                cityScannerService.getScanResultsScores(cityName, new CityScannerService.VolleyScoreResponseListener() {
                    @Override
                    public void onError(String message) {
//                        Toast.makeText(MainActivity.this, "There is a score error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(double score, String summary, ArrayList<CityScore> cityScoreBase) {

                        String summaryString = String.valueOf(HtmlCompat.fromHtml(summary,0));
                        cityDataRepository.setCitySummary(summaryString);

                        double roundedScore =  Math.round(score*100.0)/100.0;
                        String scoreString = "Teleport City Score: " + roundedScore;
                        cityDataRepository.setCityTeleportScore(scoreString);


                        if (cityDataRepository.getCityDetailsData().isEmpty()) {
                            System.out.println("empty bruh");
                        } //check if sortCityDetails is empty


                        ArrayList<CityScore> newCityScore = new ArrayList<>();

                        for (int i=0;i<cityScoreBase.size();i++) {
                            String name = cityScoreBase.get(i).getName();
                            int scoreValue = cityScoreBase.get(i).getScore();
                            String color = cityScoreBase.get(i).getColor();
                            for (int j=0;j<cityDataRepository.getCityDetailsData().size();j++) {
                                if (cityDataRepository.getCityDetailsData().get(j).getCityDetailsName().equals(name)) {
                                    newCityScore.add(new CityScore(name,scoreValue,color,cityDataRepository.getCityDetailsData().get(j)));
                                }
                            }
                        }
//                scoreRecViewAdapter.setCityScoresList(newCityScore);
                        cityDataRepository.setCityScores(newCityScore);

                    } // onResponse

                }); //get city scores
            } //onResponse

        }); //get city details

        System.out.println("Task 3 done");






        cityScannerService.getScanResultsSalaries(cityName, new CityScannerService.VolleySalaryResponseListener() {

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(ArrayList<CitySalaries> citySalaries) {

//                citySalariesBase = citySalaries;
//
//                //-- populate jobSpinner
//                String[] allJobTitles = new String[citySalariesBase.size()];
//
//                for (int i=0;i<citySalariesBase.size();i++) {
//                    allJobTitles[i] = citySalariesBase.get(i).getTitle();
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.custom_spinner_layout, allJobTitles);
//                jobSpinner.setAdapter(adapter);


            }
        }); //get city salaries

        System.out.println("Task 4 done");

    }

    @Override
    public String[] readImageDataArray() { return cityDataRepository.getImageData(); }

    @Override
    public String readImgUrl() { return cityDataRepository.getImageData()[0]; }

    @Override
    public String readPhotographer() { return cityDataRepository.getImageData()[1]; }

    @Override
    public String readSource() { return cityDataRepository.getImageData()[2]; }

    @Override
    public String readSite() { return cityDataRepository.getImageData()[3]; }

    @Override
    public String readLicense() { return cityDataRepository.getImageData()[4]; }


    @Override
    public String readBasicInfo() { return cityDataRepository.getBasicInfo(); }

    @Override
    public String readSummary() { return cityDataRepository.getSummary(); }

    @Override
    public String readTeleportCityScore() { return cityDataRepository.getTeleportScore(); }

    @Override
    public ArrayList<CityScore> readCityScores() { return cityDataRepository.getCityScores(); }


}
