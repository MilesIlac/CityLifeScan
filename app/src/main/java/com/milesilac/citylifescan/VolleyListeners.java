package com.milesilac.citylifescan;


import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;


public interface VolleyListeners {

    interface VolleyJSONResponseListener {
        void onResponse(JSONObject jsonObject);
        void onError(VolleyError error);
    }
    interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String string);
    }

    interface VolleyImageResponseListener {
        void onError(String message);

        void onResponse(String string, String photographer, String source, String site, String license);
    }

    interface VolleyArrayResponseListener {
        void onError(String message);

        void onResponse(String[] names);

    }

    interface VolleyScoreResponseListener {
        void onError(String message);

        void onResponse(double score, String summary, ArrayList<CityScore> cityScore);
    }

    interface VolleySalaryResponseListener {
        void onError(String message);

        void onResponse(ArrayList<CitySalaries> citySalaries);
    }

    interface VolleyDetailsResponseListener {
        void onError(String message);

        void onResponse(ArrayList<CityDetails> cityDetails);
    }
}
