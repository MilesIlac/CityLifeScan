package com.milesilac.citylifescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    EditText inputCity;
    NetworkImageView networkImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        inputCity = findViewById(R.id.inputCity);
        networkImageView = findViewById(R.id.photo);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            CityScannerService cityScannerService = new CityScannerService(MainActivity.this);

            cityScannerService.getScanResultsImage(inputCity.getText().toString(), new CityScannerService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "There is a picture error (main)", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String imgUrl) {
                        networkImageView.setDefaultImageResId(R.id.photo);
                        networkImageView.setImageUrl(imgUrl, MySingleton.getInstance(MainActivity.this).getImageLoader()); //ImgController from your code.
                    }
                });

            cityScannerService.getScanResultsData(inputCity.getText().toString(), new CityScannerService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "There is an error (main)", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String test1) {
                    Toast.makeText(MainActivity.this, test1, Toast.LENGTH_LONG).show();
                }
            });


            } //btnScan OnClick
        }); //btnScan OnClickListener
    } //OnCreate
}