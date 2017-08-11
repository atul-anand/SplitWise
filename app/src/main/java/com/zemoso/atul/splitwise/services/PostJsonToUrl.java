package com.zemoso.atul.splitwise.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class PostJsonToUrl extends IntentService {

    private static final String TAG = PostJsonToUrl.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private JsonObjectRequest mJsonObjectRequest;


    private JSONObject mJsonObject;
    private String mUrl;
    private Bundle mBundle;


    public PostJsonToUrl() {
        super("PostJsonToUrl");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mBundle = intent.getExtras();
        try {
            mJsonObject = new JSONObject(mBundle.getString("json"));
            mUrl = mBundle.getString("url");
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mUrl, mJsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, String.valueOf(response));
                        }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, String.valueOf(error));
                    }
            });
            mRequestQueue.add(mJsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
