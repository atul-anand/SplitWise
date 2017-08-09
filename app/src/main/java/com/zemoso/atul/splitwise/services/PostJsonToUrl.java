package com.zemoso.atul.splitwise.services;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.JsonObject;

public class PostJsonToUrl extends IntentService {

    private static final String TAG = PostJsonToUrl.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private JsonArrayRequest mJsonArrayRequest;
    private JsonObject mJsonObject;
    private String mUrl;

    public PostJsonToUrl() {
        super("PostJsonToUrl");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
