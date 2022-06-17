package com.milesilac.citylifescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
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
import com.milesilac.citylifescan.model.CityDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements CityContract.View {



    Button btnScan;
    AutoCompleteTextView inputCity;
    TextView outputBasicInfo, outputSummary, outputTeleportCityScore, showMedianSalary, imagePhotographer, imageAttribution;
    NetworkImageView networkImageView, networkImageViewZoomed;
    RecyclerView scoresRecView;
    NestedScrollView scrollView;
    MaterialCardView imageCard, basicInfoCard, scoresCard, salaryChartCard;
    AnyChartView anyChartView;
    Spinner jobSpinner;
    Dialog photoZoomed;

    public static final String SHOW_MEDIAN_SALARY = "MEDIAN SALARY: $";
    public String getCityNameInput;
    public static ArrayList<CityDetails> sortCityDetails = new ArrayList<>();

    ArrayList<CitySalaries> citySalariesBase = null;
    Pyramid pyramid;


    String[] checkImageDataArray = null;
    String checkBasicInfo = null;
    ArrayList<CityScore> checkCityScores = null;
    String source = null;
    String license = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init mainActivity elements
        btnScan = findViewById(R.id.btnScan);
        inputCity = findViewById(R.id.inputCity);
        outputBasicInfo = findViewById(R.id.outputBasicInfo);
        outputSummary = findViewById(R.id.outputSummary);
        outputTeleportCityScore = findViewById(R.id.outputTeleportCityScore);
        showMedianSalary = findViewById(R.id.showMedianSalary);
        networkImageView = findViewById(R.id.photo);
        scoresRecView = findViewById(R.id.scoresRecView);
        scrollView = findViewById(R.id.outputScrollView);
        jobSpinner = findViewById(R.id.jobSpinner);



        //init MaterialCardViews
        imageCard = findViewById(R.id.imageCard);
        basicInfoCard = findViewById(R.id.basicInfoCard);
        scoresCard = findViewById(R.id.scoresCard);
        salaryChartCard = findViewById(R.id.pyramidChartCard);

        //set default networkImageView
        networkImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);

        //setup networkImageView dialog
        photoZoomed = new Dialog(this);
        photoZoomed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photoZoomed.setContentView(R.layout.image_view_dialog);
        photoZoomed.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        photoZoomed.setCancelable(true);

        networkImageViewZoomed = photoZoomed.findViewById(R.id.photoZoomed);
        imagePhotographer = photoZoomed.findViewById(R.id.imagePhotographer);
        imageAttribution = photoZoomed.findViewById(R.id.imageAttribution);

        //set RecyclerViewAdapter
        ScoreRecViewAdapter scoreRecViewAdapter = new ScoreRecViewAdapter(this);
        scoresRecView.setAdapter(scoreRecViewAdapter);
        scoresRecView.setLayoutManager(new LinearLayoutManager(this));

        //set AnyChart view
        anyChartView = findViewById(R.id.pyramidChart);

        //empty screen on initialization
        imageCard.setVisibility(View.INVISIBLE);
        basicInfoCard.setVisibility(View.INVISIBLE);
        scoresCard.setVisibility(View.INVISIBLE);
        salaryChartCard.setVisibility(View.INVISIBLE);

        CityScanResults cityScanResults = new CityScanResults(this);

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


        pyramid = AnyChart.pyramid();


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());



        //btnScan OnClick
        btnScan.setOnClickListener(v -> {
            imageCard.setVisibility(View.INVISIBLE);
            basicInfoCard.setVisibility(View.INVISIBLE);
            scoresCard.setVisibility(View.INVISIBLE);
            salaryChartCard.setVisibility(View.INVISIBLE);

            cityScanResults.getScanResults(inputCity.getText().toString());
            CityScannerService cityScannerService1 = new CityScannerService(MainActivity.this);

            executorService.execute(new Runnable() {
                @Override
                public void run() {



                    while (checkImageDataArray == cityScanResults.readImageDataArray()) { }
                    while (checkBasicInfo == cityScanResults.readBasicInfo()) { }
                    while (checkCityScores == cityScanResults.readCityScores()) { }

                    checkImageDataArray = cityScanResults.readImageDataArray();
                    checkBasicInfo = cityScanResults.readBasicInfo();
                    checkCityScores = cityScanResults.readCityScores();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            networkImageView.setImageUrl(cityScanResults.readImgUrl(), MySingleton.getInstance(MainActivity.this).getImageLoader()); //ImgController from your code.
                            networkImageViewZoomed.setImageUrl(cityScanResults.readImgUrl(), MySingleton.getInstance(MainActivity.this).getImageLoader());


                            String personAndSite = cityScanResults.readPhotographer() + "@" + cityScanResults.readSite();
                            SpannableString string = new SpannableString(personAndSite);
                            int index = personAndSite.indexOf("@");
                            string.setSpan(new URLSpan(cityScanResults.readSource()), index+1, personAndSite.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            imagePhotographer.setText(string);
                            imagePhotographer.setMovementMethod(LinkMovementMethod.getInstance());


                            imageAttribution.setText(cityScanResults.readLicense());

                            imageCard.setVisibility(View.VISIBLE);


                            outputBasicInfo.setText(cityScanResults.readBasicInfo());

                            basicInfoCard.setVisibility(View.VISIBLE);

                            outputSummary.setText(cityScanResults.readSummary());
                            outputTeleportCityScore.setText(cityScanResults.readTeleportCityScore());
                            scoreRecViewAdapter.setCityScoresList(cityScanResults.readCityScores());

                            scoresCard.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });






            networkImageView.setOnClickListener(v1 -> photoZoomed.show());



            //run()
//            executorService.execute(() -> {
//
//                cityScannerService1.getScanResultsImage(getCityNameInput, new CityScannerService.VolleyImageResponseListener() {
//
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(MainActivity.this, "There is a picture error (main)", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onResponse(String imgUrl, String photographer, String source, String site, String license) {
////                            scrollView.scrollTo(0,-1);
//                        networkImageView.setImageUrl(imgUrl, MySingleton.getInstance(MainActivity.this).getImageLoader()); //ImgController from your code.
//                        networkImageViewZoomed.setImageUrl(imgUrl, MySingleton.getInstance(MainActivity.this).getImageLoader());
//
//                        String personAndSite = photographer + "@" + site;
//                        SpannableString string = new SpannableString(personAndSite);
//                        int index = personAndSite.indexOf("@");
//                        string.setSpan(new URLSpan(source), index+1, personAndSite.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        imagePhotographer.setText(string);
//                        imagePhotographer.setMovementMethod(LinkMovementMethod.getInstance());
//                        imageAttribution.setText(license);
//
//                    } //onResponse
//
//                }); //get city image
//
//                System.out.println("Task 1 done");
//
//                cityScannerService1.getScanResultsBasicInfo(getCityNameInput, new CityScannerService.VolleyResponseListener() {
//
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(MainActivity.this, "There is a basic info error (main)", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onResponse(String string) {
//                        outputBasicInfo.setText(string);
//
//                    }
//
//                }); //get city basic info
//
//                System.out.println("Task 2 done");
//
//
//
//
//                cityScannerService1.getScanResultsCityDetails(getCityNameInput, new CityScannerService.VolleyDetailsResponseListener() {
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(MainActivity.this, "There is a details error (main)", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onResponse(ArrayList<CityDetails> cityDetails) {
//
//
//                        cityScannerService1.getScanResultsScores(getCityNameInput, new CityScannerService.VolleyScoreResponseListener() {
//                            @Override
//                            public void onError(String message) {
//                                Toast.makeText(MainActivity.this, "There is a score error (main)", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onResponse(double score, String summary, ArrayList<CityScore> cityScoreBase) {
//
//                                outputSummary.setText(String.valueOf(HtmlCompat.fromHtml(summary,0)));
//
//                                double roundedScore =  Math.round(score*100.0)/100.0;
//                                String scoreString = "Teleport City Score: " + roundedScore;
//                                outputTeleportCityScore.setText(scoreString);
//
//                                if (cityDetails.isEmpty()) {
//                                    System.out.println("empty bruh");
//                                } //check if sortCityDetails is empty
//
//
//                                ArrayList<CityScore> newCityScore = new ArrayList<>();
//
//                                for (int i=0;i<cityScoreBase.size();i++) {
//                                    String name = cityScoreBase.get(i).getName();
//                                    int scoreValue = cityScoreBase.get(i).getScore();
//                                    String color = cityScoreBase.get(i).getColor();
//                                    for (int j=0;j<cityDetails.size();j++) {
//                                        if (cityDetails.get(j).getCityDetailsName().equals(name)) {
//                                            newCityScore.add(new CityScore(name,scoreValue,color,cityDetails.get(j)));
//                                        }
//                                    }
//                                }
//                                scoreRecViewAdapter.setCityScoresList(newCityScore);
//
//
//                            } // onResponse
//
//                        }); //get city scores
//
//
//                    } //onResponse
//
//                }); //get city details
//
//                System.out.println("Task 3 done");
//
//                cityScannerService1.getScanResultsSalaries(getCityNameInput, new CityScannerService.VolleySalaryResponseListener() {
//
//                    @Override
//                    public void onError(String message) {
//
//                    }
//
//                    @Override
//                    public void onResponse(ArrayList<CitySalaries> citySalaries) {
//
//                        citySalariesBase = citySalaries;
//
//                        //-- populate jobSpinner
//                        String[] allJobTitles = new String[citySalariesBase.size()];
//
//                        for (int i=0;i<citySalariesBase.size();i++) {
//                            allJobTitles[i] = citySalariesBase.get(i).getTitle();
//                        }
//
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.custom_spinner_layout, allJobTitles);
//                        jobSpinner.setAdapter(adapter);
//
//                        //--
//
//                        //-- show Median Salary & Pyramid Chart
////                    Pyramid pyramid = AnyChart.pyramid();
////                    pyramid.legend(false);
////                    pyramid.tooltip().titleFormat("{%percentile}th Percentile");
////                    pyramid.tooltip().format("Estimated earnings: ${%salary}");
////
////                    anyChartView.setChart(pyramid);
//
//                        pyramid.enabled(true);
//
//                        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                //round off values to 2 decimal places
//                                double percentile25th = citySalariesBase.get(position).getPercentile_25();
//                                double percentile50th = citySalariesBase.get(position).getPercentile_50();
//                                double percentile75th = citySalariesBase.get(position).getPercentile_75();
//                                double percentile25thRoundOff = Math.round(percentile25th*100.0)/100.0;
//                                double percentile50thRoundOff = Math.round(percentile50th*100.0)/100.0;
//                                double percentile75thRoundOff = Math.round(percentile75th*100.0)/100.0;
//
//                                //-- show Median Salary for each job
//                                String showMedianSalaryString = SHOW_MEDIAN_SALARY + percentile50thRoundOff;
//                                showMedianSalary.setText(showMedianSalaryString);
//                                //--
//
//                                //--set salary percentiles to pyramid chart
//
//                                List<DataEntry> data = new ArrayList<>();
//
//                                data.add( new CustomDataEntry("What about 75% are making: $" + percentile25thRoundOff,75,25, percentile25thRoundOff));
//                                data.add( new CustomDataEntry("What about 50% are making: $" + percentile50thRoundOff,50,50, percentile50thRoundOff));
//                                data.add( new CustomDataEntry("What about 25% are making: $" + percentile75thRoundOff,25,75, percentile75thRoundOff));
//
//                                pyramid.data(data);
//
//                                //--
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });
//                        //--
//
////                            salaryChartCard.setVisibility(View.VISIBLE);
//                    }
//                }); //get city salaries
//
//                System.out.println("Task 4 done");
//
//
//               handler.postDelayed(() -> {
//                   imageCard.setVisibility(View.VISIBLE);
//                   networkImageView.setOnClickListener(v1 -> photoZoomed.show());
//
//                   basicInfoCard.setVisibility(View.VISIBLE);
//                   scoresCard.setVisibility(View.VISIBLE);
//
//                   salaryChartCard.setVisibility(View.VISIBLE);
//               }, 1750);
//
//
//
//
//                scrollView.scrollTo(0,-1);
//                System.out.println("Every service was called");
//
//            }); //executor runnable

        }); //btnScan OnClickListener
    } //OnCreate



    @Override
    protected void onResume() {
        super.onResume();

        pyramid = AnyChart.pyramid();

        pyramid.legend(false);
        pyramid.tooltip().titleFormat("{%percentile}th Percentile");
        pyramid.tooltip().format("Estimated earnings: ${%salary}");
        pyramid.enabled(false);

        anyChartView.setChart(pyramid);

        if (citySalariesBase != null) {
            pyramid.enabled(true);

            String[] allJobTitles = new String[citySalariesBase.size()];

            for (int i=0;i<citySalariesBase.size();i++) {
                allJobTitles[i] = citySalariesBase.get(i).getTitle();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.custom_spinner_layout, allJobTitles);
            jobSpinner.setAdapter(adapter);
            //--

            //-- show Median Salary & Pyramid Chart
//            Pyramid pyramid = AnyChart.pyramid();
//            pyramid.legend(false);
//            pyramid.tooltip().titleFormat("{%percentile}th Percentile");
//            pyramid.tooltip().format("Estimated earnings: ${%salary}");
//
//            anyChartView.setChart(pyramid);

            jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //round off values to 2 decimal places
                    double percentile25th = citySalariesBase.get(position).getPercentile_25();
                    double percentile50th = citySalariesBase.get(position).getPercentile_50();
                    double percentile75th = citySalariesBase.get(position).getPercentile_75();
                    double percentile25thRoundOff = Math.round(percentile25th*100.0)/100.0;
                    double percentile50thRoundOff = Math.round(percentile50th*100.0)/100.0;
                    double percentile75thRoundOff = Math.round(percentile75th*100.0)/100.0;

                    //-- show Median Salary for each job
                    String showMedianSalaryString = SHOW_MEDIAN_SALARY + percentile50thRoundOff;
                    showMedianSalary.setText(showMedianSalaryString);
                    //--

                    //--set salary percentiles to pyramid chart

                    List<DataEntry> data = new ArrayList<>();

                    data.add( new CustomDataEntry("What about 75% are making: $" + percentile25thRoundOff,75,25, percentile25thRoundOff));
                    data.add( new CustomDataEntry("What about 50% are making: $" + percentile50thRoundOff,50,50, percentile50thRoundOff));
                    data.add( new CustomDataEntry("What about 25% are making: $" + percentile75thRoundOff,25,75, percentile75thRoundOff));

                    pyramid.data(data);

                    //--
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    } //onResume

}