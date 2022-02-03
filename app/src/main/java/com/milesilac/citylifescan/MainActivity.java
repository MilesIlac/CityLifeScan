package com.milesilac.citylifescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    EditText inputCity;
    TextView outputBasicInfo;
    NetworkImageView networkImageView;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        inputCity = findViewById(R.id.inputCity);
        outputBasicInfo = findViewById(R.id.outputBasicInfo);
        networkImageView = findViewById(R.id.photo);
        scrollView = findViewById(R.id.outputScrollView);

        networkImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);

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
                    public void onResponse(String score, String summary, String scoreVerbose) {
//                        String newOutput = outputBasicInfo.getText().toString() + string;
                        String output = outputBasicInfo.getText().toString() + score + HtmlCompat.fromHtml(summary,0) + scoreVerbose;
                        outputBasicInfo.setText(output);
                    }
                });

//                cityScannerService.getScanResultsCostOfLiving(inputCity.getText().toString(), new CityScannerService.VolleyArrayResponseListener() {
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(MainActivity.this, "There is a CoL error (main)", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onResponse(JSONArray jsonArray) {
//                        outputCostOfLiving.setText(jsonArray.toString());
//                    }
//                }); //get city CoL


            } //btnScan OnClick
        }); //btnScan OnClickListener
    } //OnCreate
}