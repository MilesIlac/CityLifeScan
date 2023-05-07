package com.milesilac.citylifescan.view.cityscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pyramid;

import com.bumptech.glide.Glide;
import com.milesilac.citylifescan.model.CityDetails;
import com.milesilac.citylifescan.model.CitySalaries;
import com.milesilac.citylifescan.model.CityScore;
import com.milesilac.citylifescan.R;
import com.milesilac.citylifescan.databinding.ActivityMainBinding;
import com.milesilac.citylifescan.network.CityScanController;
import com.milesilac.citylifescan.view.CityPhotoDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements CityScannerContract.View {

    public static final String SHOW_MEDIAN_SALARY = "MEDIAN SALARY: $";

    private ActivityMainBinding binding;

    private CityPhotoDialogFragment dialogFragment;
    private ScoreRecViewAdapter scoreRecViewAdapter;
    Pyramid pyramid;

    @Inject
    CityScanController controller;

    private CityScannerContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CityScannerPresenter(this, controller);

        presenter.checkCityName();

        dialogFragment = new CityPhotoDialogFragment();

        setupScoresAdapter();
        setupPyramidChart();
        addEventListeners();
        setAllFieldsVisibility(false);

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

    private void setAllFieldsVisibility(boolean visible) {
        binding.imageCard.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        binding.basicInfoCard.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        binding.scoresCard.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        binding.pyramidChartCard.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void viewResults() {
        setAllFieldsVisibility(true);
    }

    @Override
    public void populateCityNames(String[] countryNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.autocomplete_layout, countryNames);
        binding.inputCity.setThreshold(1);
        binding.inputCity.setAdapter(adapter);

        isPopulateCityNamesSuccessful(true);
    }

    @Override
    public void onPopulateCityNamesFailed() {
        isPopulateCityNamesSuccessful(false);
    }

    private void isPopulateCityNamesSuccessful(boolean success) {
        binding.inputCity.setVisibility(success ? View.VISIBLE : View.GONE);
        binding.btnInputCityLoadFailed.setVisibility(!success ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setImageData(String imageUrl, String photographer, String source, String site, String license, SpannableString photographerAndSite) {
        Glide.with(this)
             .load(imageUrl)
             .into(binding.photo);

        dialogFragment.setImageDetails(imageUrl, photographerAndSite, license);

        presenter.setImageDataSet(true);
    }

    @Override
    public void setBasicInfo(String basicInfo) {
        binding.outputBasicInfo.setText(basicInfo);

        presenter.setBasicInfoSet(true);
    }
    @Override
    public void setCityDetails(List<CityDetails> cityDetails, String cityName) {
        presenter.getScanResultsScores(cityDetails, cityName);
    }

    public void setCitySummaryAndTeleportScore(String summary, String teleportScore) {
        binding.outputSummary.setText(summary);
        binding.outputTeleportCityScore.setText(teleportScore);

        presenter.setCitySummaryAndTeleportScoreSet(true);
    }

    @Override
    public void setCityScores(List<CityScore> cityScores) {
        scoreRecViewAdapter.setCityScoresList(cityScores);

        presenter.setCityScoresSet(true);
    }

    @Override
    public void setCitySalariesData(List<CitySalaries> citySalaries) {
        String[] allJobTitles = new String[citySalaries.size()];

        for (int i = 0, size = citySalaries.size() ; i < size ; i++) {
            allJobTitles[i] = citySalaries.get(i).getTitle();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.custom_spinner_layout, allJobTitles);
        binding.jobSpinner.setAdapter(adapter);
        setJobSpinnerListener(citySalaries);

        presenter.setCitySalariesDataSet(true);
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
        loadFailedClickListener();
        searchTextChangeListener();
        scanOnClickListener();

        binding.photo.setOnClickListener(v -> dialogFragment.show(getSupportFragmentManager(), null));
    }

    private void loadFailedClickListener() {
        binding.btnInputCityLoadFailed.setOnClickListener(v -> {
            if (presenter != null) {
                presenter.checkCityName();
            }
        });
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
            if (presenter != null) {
                presenter.setImageDataSet(false);
                presenter.setBasicInfoSet(false);
                presenter.setCitySummaryAndTeleportScoreSet(false);
                presenter.setCityScoresSet(false);
                presenter.setCitySalariesDataSet(false);
                setAllFieldsVisibility(false);
                presenter.getScanResults(binding.inputCity.getText().toString());
            }
        });
    }

}