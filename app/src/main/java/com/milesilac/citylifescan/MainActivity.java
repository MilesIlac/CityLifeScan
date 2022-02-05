package com.milesilac.citylifescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    AutoCompleteTextView inputCity;
    TextView outputBasicInfo;
    NetworkImageView networkImageView;
    RecyclerView scoresRecView;
    NestedScrollView scrollView;
    MaterialCardView imageCard, basicInfoCard, scoresCard;


    public static final String QUERY_FOR_ALL_URBAN_AREAS_2 = "https://api.teleport.org/api/urban_areas/";


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

        //set default networkImageView
        networkImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);

        //set RecyclerViewAdapter
        ScoreRecViewAdapter scoreRecViewAdapter = new ScoreRecViewAdapter();
        scoresRecView.setAdapter(scoreRecViewAdapter);
        scoresRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //empty screen on initialization
        imageCard.setVisibility(View.GONE);
        basicInfoCard.setVisibility(View.GONE);
        scoresCard.setVisibility(View.GONE);

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

////         Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        ArrayList<String> names = new ArrayList<>();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_ALL_URBAN_AREAS_2, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONObject links;
//                JSONArray urbanAreas;
//                JSONObject getName;
//
//                try {
//                    links = response.getJSONObject("_links");
//                    urbanAreas = links.getJSONArray("ua:item");
//                    for (int i=0;i<urbanAreas.length();i++) {
//                        getName = urbanAreas.getJSONObject(i);
//                        names.add(getName.getString("name"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                String[] countryNames = new String[names.size()];
//                for (int i=0;i<names.size();i++) {
//                    countryNames[i] = names.get(i);
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.autocomplete_layout, names);
//                System.out.println("Has Country: " + names.get(5));
//                inputCity.setThreshold(1);
//                inputCity.setAdapter(adapter);
//
//                queue.stop();
//                System.out.println("Autocomplete populated");
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        queue.add(jsonObjectRequest);




        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Button was called");
                Toast.makeText(MainActivity.this,"Button was called",Toast.LENGTH_LONG).show();

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

                System.out.println("Every service was called");
            } //btnScan OnClick
        }); //btnScan OnClickListener
    } //OnCreate

}