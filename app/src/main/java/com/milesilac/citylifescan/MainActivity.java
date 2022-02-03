package com.milesilac.citylifescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    EditText inputCity;
    TextView outputBasicInfo;
    NetworkImageView networkImageView;
    RecyclerView scoresRecView;
    NestedScrollView scrollView;
    MaterialCardView imageCard, basicInfoCard, scoresCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        inputCity = findViewById(R.id.inputCity);
        outputBasicInfo = findViewById(R.id.outputBasicInfo);
        networkImageView = findViewById(R.id.photo);
        scoresRecView = findViewById(R.id.scoresRecView);
        scrollView = findViewById(R.id.outputScrollView);

        imageCard = findViewById(R.id.imageCard);
        basicInfoCard = findViewById(R.id.basicInfoCard);
        scoresCard = findViewById(R.id.scoresCard);

        networkImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);

        ScoreRecViewAdapter scoreRecViewAdapter = new ScoreRecViewAdapter();
        scoresRecView.setAdapter(scoreRecViewAdapter);
        scoresRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        imageCard.setVisibility(View.GONE);
        basicInfoCard.setVisibility(View.GONE);
        scoresCard.setVisibility(View.GONE);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    }
                }); //get city basic info

                cityScannerService.getScanResultsScores(inputCity.getText().toString(), new CityScannerService.VolleyScoreResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "There is a score error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String score, String summary, ArrayList<CityScore> cityScore) {
                        String output = outputBasicInfo.getText().toString() + score + HtmlCompat.fromHtml(summary,0);
                        outputBasicInfo.setText(output);


                        scoreRecViewAdapter.setCityScoresList(cityScore);

                        basicInfoCard.setVisibility(View.VISIBLE);
                        scoresCard.setVisibility(View.VISIBLE);
                    }

                }); //get city scores

            } //btnScan OnClick
        }); //btnScan OnClickListener
    } //OnCreate
}