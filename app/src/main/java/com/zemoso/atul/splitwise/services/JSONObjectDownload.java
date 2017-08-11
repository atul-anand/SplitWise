package com.zemoso.atul.splitwise.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zemoso.atul.splitwise.modules.Transaction;
import com.zemoso.atul.splitwise.modules.User;
import com.zemoso.atul.splitwise.modules.Group;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmObject;


public class JSONObjectDownload extends IntentService {

    private static final String TAG = JSONObjectDownload.class.getSimpleName();
    private Bundle mBundle;
    private String mUrl;
    private String mType;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mJSONObjectRequest;
    private Object mObject;

    public JSONObjectDownload() {
        super("JSONObjectDownload");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mBundle = intent.getExtras();
        mUrl = mBundle.getString("url");
        mType = mBundle.getString("type");
        assert mType != null;
        if(mType.equals("User"))
            mObject = new User();
        if(mType.equals("Group"))
            mObject = new Group();
        if(mType.equals("Transaction"))
            mObject = new Transaction();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//        mJSONObjectRequest = new JsonObjectRequest(
//                mUrl, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Realm realm = Realm.getDefaultInstance();
//                Log.d(TAG, String.valueOf(response.length()));
//                realm.beginTransaction();
//                try {
//                    Log.d(TAG, String.valueOf(response));
////                    mObject.setId(Integer.parseInt(String.valueOf(response.get("id"))));
////                    mObject.setImageFilePath("");
////                    mObject.setJSON(response.toString());
//                    realm.insertOrUpdate(mObject);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                realm.commitTransaction();
//                realm.close();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG,error.toString());
//            }
//        }
//        );

    }
}
