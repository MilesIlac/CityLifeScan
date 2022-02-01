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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    EditText inputCity;
    RecyclerView resultsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        inputCity = findViewById(R.id.inputCity);
        resultsRecView = findViewById(R.id.resultsRecView);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            CityScannerService cityScannerService = new CityScannerService(MainActivity.this);

            cityScannerService.getScanResults(inputCity.getText().toString(), new CityScannerService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "There is an error (main)", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String test2) {
                    Toast.makeText(MainActivity.this, "Output is: " + test2, Toast.LENGTH_LONG).show();
                }
            });


            } //btnScan OnClick
        }); //btnScan OnClickListener
    } //OnCreate
}