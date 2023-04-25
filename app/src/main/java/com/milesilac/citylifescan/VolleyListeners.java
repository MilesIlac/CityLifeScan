package com.milesilac.citylifescan;


import com.android.volley.VolleyError;

import org.json.JSONObject;


public interface VolleyListeners {

    interface VolleyJSONResponseListener {
        void onResponse(JSONObject jsonObject);
        void onError(VolleyError error);
    }

}
