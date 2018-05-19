package com.example.l.gamedb.callback;

import com.android.volley.VolleyError;
import org.json.JSONArray;


public interface onSuccessCallback {
    void onSuccess(JSONArray result);
    void onError(VolleyError error);
}
