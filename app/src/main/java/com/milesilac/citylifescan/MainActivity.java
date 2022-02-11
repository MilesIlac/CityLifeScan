package com.milesilac.citylifescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pyramid;


import com.google.android.material.card.MaterialCardView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    AutoCompleteTextView inputCity;
    TextView outputBasicInfo, showMedianSalary;
    NetworkImageView networkImageView;
    RecyclerView scoresRecView;
    NestedScrollView scrollView;
    MaterialCardView imageCard, basicInfoCard, scoresCard, salaryChartCard;
    AnyChartView anyChartView;
    Spinner jobSpinner;

    public static final String SHOW_MEDIAN_SALARY = "MEDIAN SALARY: ";

    public static String getCityNameInput;

//    public static ArrayList<SortCityDetails> sortCityDetails = new ArrayList<>();
    public static ArrayList<CityScore> sortCityDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnScan = findViewById(R.id.btnScan);
        inputCity = findViewById(R.id.inputCity);
        outputBasicInfo = findViewById(R.id.outputBasicInfo);
        showMedianSalary = findViewById(R.id.showMedianSalary);
        networkImageView = findViewById(R.id.photo);
        scoresRecView = findViewById(R.id.scoresRecView);
        scrollView = findViewById(R.id.outputScrollView);
        jobSpinner = findViewById(R.id.jobSpinner);

        imageCard = findViewById(R.id.imageCard);
        basicInfoCard = findViewById(R.id.basicInfoCard);
        scoresCard = findViewById(R.id.scoresCard);
        salaryChartCard = findViewById(R.id.pyramidChartCard);

        //set default networkImageView
        networkImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);

        //set RecyclerViewAdapter
        ScoreRecViewAdapter scoreRecViewAdapter = new ScoreRecViewAdapter(this);
        scoresRecView.setAdapter(scoreRecViewAdapter);
        scoresRecView.setLayoutManager(new LinearLayoutManager(this));

        //set AnyChart view
        anyChartView = findViewById(R.id.pyramidChart);


        //empty screen on initialization
        imageCard.setVisibility(View.GONE);
        basicInfoCard.setVisibility(View.GONE);
        scoresCard.setVisibility(View.GONE);
        salaryChartCard.setVisibility(View.GONE); //

        //populate autocomplete
        CityScannerService cityScannerService = new CityScannerService(MainActivity.this);
        cityScannerService.checkCityName(new CityScannerService.VolleyArrayResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(String[] names) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.autocomplete_layout, names);
                inputCity.setThreshold(1);
                inputCity.setAdapter(adapter);
            }

        }); //checkCityName



        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getCityNameInput = inputCity.getText().toString();

                CityScannerService cityScannerService = new CityScannerService(MainActivity.this);
                outputBasicInfo.setText("");

                cityScannerService.getScanResultsImage(inputCity.getText().toString(), new CityScannerService.VolleyResponseListener() {

                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "There is a picture error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String imgUrl) {
                        scrollView.scrollTo(0,-1);
                        networkImageView.setImageUrl(imgUrl, MySingleton.getInstance(MainActivity.this).getImageLoader()); //ImgController from your code.
                        imageCard.setVisibility(View.VISIBLE);
                    }

                }); //get city image

                cityScannerService.getScanResultsBasicInfo(inputCity.getText().toString(), new CityScannerService.VolleyResponseListener() {

                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "There is a basic info error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String test1) {
                        String newOutput = outputBasicInfo.getText().toString() + test1;
                        outputBasicInfo.setText(newOutput);

//                        basicInfoCard.setVisibility(View.VISIBLE);
                    }

                }); //get city basic info

                cityScannerService.getScanResultsCityDetails(inputCity.getText().toString(), new CityScannerService.VolleyDetailsResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "There is a details error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(ArrayList<CityDetails> cityDetails) {

//                        sortCityDetails.add(new SortCityDetails(cityDetails));
                        sortCityDetails.add(new CityScore(cityDetails));

                    }


                });


                cityScannerService.getScanResultsScores(inputCity.getText().toString(), new CityScannerService.VolleyScoreResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "There is a score error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String score, String summary, ArrayList<CityScore> cityScoreBase) {

                        String output = outputBasicInfo.getText().toString() + score + HtmlCompat.fromHtml(summary,0);
                        outputBasicInfo.setText(output);

                        if (sortCityDetails.isEmpty()) {
                            System.out.println("empty bruh");
                        }

                        ArrayList<CityScore> newCityScore = new ArrayList<>();


                        for (int i=0;i<cityScoreBase.size();i++) {
                            String name = cityScoreBase.get(i).getName();
                            int scoreValue = cityScoreBase.get(i).getScore();
                            String color = cityScoreBase.get(i).getColor();
                            for (int j=0;j<sortCityDetails.size();j++) {
                                for (int k=0;k<sortCityDetails.get(j).getCityDetailsArray().size();k++) {
                                    if (sortCityDetails.get(j).getCityDetailsArray().get(k).getCityDetailsName().equals(name)) {
                                        newCityScore.add(new CityScore(name,scoreValue,color,sortCityDetails.get(j).getCityDetailsArray().get(k)));
                                        scoreRecViewAdapter.setCityScoresList(newCityScore);
                                    }

                                }
                            }
                        }








                        basicInfoCard.setVisibility(View.VISIBLE);
                        scoresCard.setVisibility(View.VISIBLE);
                    }

                }); //get city scores

                cityScannerService.getScanResultsSalaries(inputCity.getText().toString(), new CityScannerService.VolleySalaryResponseListener() {

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(ArrayList<CitySalaries> citySalaries) {
                        //-- populate jobSpinner
                        String[] allJobTitles = new String[citySalaries.size()];
                        for (int i=0;i<citySalaries.size();i++) {
                            allJobTitles[i] = citySalaries.get(i).getTitle();

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allJobTitles);
                        jobSpinner.setAdapter(adapter);
                        //--

                        //-- show Median Salary & Pyramid Chart
                        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //round off values to 2 decimal places
                                double percentile25th = citySalaries.get(position).getPercentile_25();
                                double percentile50th = citySalaries.get(position).getPercentile_50();
                                double percentile75th = citySalaries.get(position).getPercentile_75();
                                double percentile25thRoundOff = Math.round(percentile25th*100.0)/100.0;
                                double percentile50thRoundOff = Math.round(percentile50th*100.0)/100.0;
                                double percentile75thRoundOff = Math.round(percentile75th*100.0)/100.0;

                                //-- show Median Salary for each job
                                String showMedianSalaryString = SHOW_MEDIAN_SALARY + percentile50thRoundOff;
                                showMedianSalary.setText(showMedianSalaryString);
                                //--

                                //--set salary percentiles to pyramid chart
                                Pyramid pyramid = AnyChart.pyramid();
                                List<DataEntry> data = new ArrayList<>();

                                data.add( new CustomDataEntry("What about 75% are making: " + percentile25thRoundOff,75,25, percentile25thRoundOff));
                                data.add( new CustomDataEntry("What about 50% are making: " + percentile50thRoundOff,50,50, percentile50thRoundOff));
                                data.add( new CustomDataEntry("What about 25% are making: " + percentile75thRoundOff,25,75, percentile75thRoundOff));

                                pyramid.data(data);
                                pyramid.legend(false);
                                pyramid.tooltip().titleFormat("{%percentile}th Percentile");
                                pyramid.tooltip().format("Estimated earnings: {%salary}");

                                anyChartView.setChart(pyramid);
                                //--
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        //--

                        salaryChartCard.setVisibility(View.VISIBLE);
                    }
                }); //get city salaries


                System.out.println("Every service was called");
            } //btnScan OnClick
        }); //btnScan OnClickListener
    } //OnCreate



}