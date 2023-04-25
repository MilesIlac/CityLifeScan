package com.milesilac.citylifescan.view.cityscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pyramid;

import com.milesilac.citylifescan.model.CityDetails;
import com.milesilac.citylifescan.model.CitySalaries;
import com.milesilac.citylifescan.network.CityScannerService;
import com.milesilac.citylifescan.model.CityScore;
import com.milesilac.citylifescan.network.VolleySingleton;
import com.milesilac.citylifescan.R;
import com.milesilac.citylifescan.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CityScannerContract.View {

    TextView imagePhotographer, imageAttribution;
    NetworkImageView networkImageViewZoomed;
    Dialog photoZoomed;

    public static final String SHOW_MEDIAN_SALARY = "MEDIAN SALARY: $";

    private ActivityMainBinding binding;
    private ScoreRecViewAdapter scoreRecViewAdapter;
    Pyramid pyramid;

    private CityScannerContract.Presenter presenter;
    private boolean isImageDataSet = false;
    private boolean isBasicInfoSet = false;
    private boolean isCitySummaryAndTeleportScoreSet = false;
    private boolean isCityScoresSet = false;
    private boolean isCitySalariesDataSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CityScannerService cityScannerService = new CityScannerService(this);
        presenter = new CityScannerPresenter(this, cityScannerService);

        presenter.checkCityName();

        //set default networkImageView
        binding.photo.setDefaultImageResId(R.drawable.ic_launcher_foreground);

        //setup networkImageView dialog
        photoZoomed = new Dialog(this);
        photoZoomed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photoZoomed.setContentView(R.layout.image_view_dialog);
        photoZoomed.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        photoZoomed.setCancelable(true);

        networkImageViewZoomed = photoZoomed.findViewById(R.id.photoZoomed);
        imagePhotographer = photoZoomed.findViewById(R.id.imagePhotographer);
        imageAttribution = photoZoomed.findViewById(R.id.imageAttribution);


        setupScoresAdapter();
        setupPyramidChart();
        addEventListeners();
        emptyScreenInit();

    }

    private void setupScoresAdapter() {
        scoreRecViewAdapter = new ScoreRecViewAdapter(this);
        binding.scoresRecView.setAdapter(scoreRecViewAdapter);
        binding.scoresRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupPyramidChart() {
        pyramid = AnyChart.pyramid();

        pyramid.legend(false);
        pyramid.tooltip().titleFormat("{%percentile}th Percentile");
        pyramid.tooltip().format("Estimated earnings: ${%salary}");

        binding.pyramidChart.setChart(pyramid);
    }

    private void emptyScreenInit() {
        binding.imageCard.setVisibility(View.INVISIBLE);
        binding.basicInfoCard.setVisibility(View.INVISIBLE);
        binding.scoresCard.setVisibility(View.INVISIBLE);
        binding.pyramidChartCard.setVisibility(View.INVISIBLE);
    }

    private void checkResponsesCompletion() {
        if (isImageDataSet && isBasicInfoSet && isCitySummaryAndTeleportScoreSet && isCityScoresSet && isCitySalariesDataSet) {
            binding.imageCard.setVisibility(View.VISIBLE);
            binding.basicInfoCard.setVisibility(View.VISIBLE);
            binding.scoresCard.setVisibility(View.VISIBLE);
            binding.pyramidChartCard.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void populateCityNames(String[] countryNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.autocomplete_layout, countryNames);
        binding.inputCity.setThreshold(1);
        binding.inputCity.setAdapter(adapter);
    }

    @Override
    public void setImageData(String imageUrl, String photographer, String source, String site, String license) {
        binding.photo.setImageUrl(imageUrl, VolleySingleton.getInstance(MainActivity.this).getImageLoader());
        networkImageViewZoomed.setImageUrl(imageUrl, VolleySingleton.getInstance(MainActivity.this).getImageLoader());

        String personAndSite = photographer + "@" + site;
        SpannableString string = new SpannableString(personAndSite);
        int index = personAndSite.indexOf("@");
        string.setSpan(new URLSpan(source), index+1, personAndSite.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        imagePhotographer.setText(string);
        imagePhotographer.setMovementMethod(LinkMovementMethod.getInstance());

        imageAttribution.setText(license);

        isImageDataSet = true;
        checkResponsesCompletion();
    }

    @Override
    public void setBasicInfo(String basicInfo) {
        binding.outputBasicInfo.setText(basicInfo);

        isBasicInfoSet = true;
        checkResponsesCompletion();
    }
    @Override
    public void setCityDetails(List<CityDetails> cityDetails, String cityName) {
        presenter.getScanResultsScores(cityDetails, cityName);
    }

    public void setCitySummaryAndTeleportScore(String summary, String teleportScore) {
        binding.outputSummary.setText(summary);
        binding.outputTeleportCityScore.setText(teleportScore);

        isCitySummaryAndTeleportScoreSet = true;
        checkResponsesCompletion();
    }

    @Override
    public void setCityScores(List<CityScore> cityScores) {
        scoreRecViewAdapter.setCityScoresList(cityScores);

        isCityScoresSet = true;
        checkResponsesCompletion();
    }

    @Override
    public void setCitySalariesData(List<CitySalaries> citySalaries) {
        String[] allJobTitles = new String[citySalaries.size()];

        for (int i=0;i<citySalaries.size();i++) {
            allJobTitles[i] = citySalaries.get(i).getTitle();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.custom_spinner_layout, allJobTitles);
        binding.jobSpinner.setAdapter(adapter);
        setJobSpinnerListener(citySalaries);

        isCitySalariesDataSet = true;
        checkResponsesCompletion();
    }

    private void setJobSpinnerListener(List<CitySalaries> citySalaries) {
        binding.jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //round off values to 2 decimal places
                double percentile25th = citySalaries.get(position).getPercentile_25();
                double percentile50th = citySalaries.get(position).getPercentile_50();
                double percentile75th = citySalaries.get(position).getPercentile_75();
                double percentile25thRoundOff = Math.round(percentile25th*100.0)/100.0;
                double percentile50thRoundOff = Math.round(percentile50th*100.0)/100.0;
                double percentile75thRoundOff = Math.round(percentile75th*100.0)/100.0;


                String showMedianSalaryString = SHOW_MEDIAN_SALARY + percentile50thRoundOff;
                binding.showMedianSalary.setText(showMedianSalaryString);


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

    private void addEventListeners() {
        searchTextChangeListener();
        scanOnClickListener();
        binding.photo.setOnClickListener(v -> photoZoomed.show());
    }

    private void searchTextChangeListener() {
        binding.inputCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void scanOnClickListener() {
        binding.btnScan.setOnClickListener(v -> {
            emptyScreenInit();

            isImageDataSet = false;
            isBasicInfoSet = false;
            isCitySummaryAndTeleportScoreSet = false;
            isCityScoresSet = false;
            isCitySalariesDataSet = false;

            presenter.getScanResults(binding.inputCity.getText().toString());
        });
    }

}