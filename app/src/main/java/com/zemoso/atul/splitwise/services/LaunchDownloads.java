package com.zemoso.atul.splitwise.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;


public class LaunchDownloads extends IntentService {

    private static final String TAG = LaunchDownloads.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private JsonArrayRequest mTransactionFindAll;
    private JsonArrayRequest mGroupJsonArrayRequest;
    private JSONObject mJsonObject;
    private String mFriendsUrl;
    private String mGroupsUrl;
    private Long mUserId;
    private Bundle mBundle;
    private User mUser;
    private Group mGroup;

    public LaunchDownloads() {
        super("LaunchDownloads");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mBundle = intent.getExtras();
        mUserId = mBundle.getLong("mUserId");
        mFriendsUrl = mBundle.getString("mFriendsUrl");
        mGroupsUrl = mBundle.getString("mGroupsUrl");
        Log.d(TAG,mFriendsUrl);
        mFriendsUrl = mFriendsUrl + "?id=" + mUserId;
//        mGroupsUrl = mGroupsUrl + "/" + mUserId;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mTransactionFindAll = new JsonArrayRequest(
                mFriendsUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                Log.d(TAG, String.valueOf(response.length()));
                realm.beginTransaction();
//                for(int i=0;i< response.length();i++){
//                    try {
//                        mJsonObject = response.getJSONObject(i);
//                        Log.d(TAG, String.valueOf(mJsonObject));
//                        mUser = new User();
//                        mUser.setId(Integer.parseInt(String.valueOf(mJsonObject.get("transID"))));
//                        mUser.setImageFilePath("");
//                        mUser.setJSON(mJsonObject.toString());
//                        Log.d(TAG,"User"+ mUser.getJSON());
//                        realm.insertOrUpdate(mUser);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
                realm.commitTransaction();
                realm.close();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.toString());
            }
        }
        );

//        mGroupJsonArrayRequest = new JsonArrayRequest(
//                mGroupsUrl, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Realm realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
//                for(int i=0;i< response.length();i++){
//                    try {
//                        mJsonObject = response.getJSONObject(i);
//                        Log.d(TAG, String.valueOf(mJsonObject));
//                        mGroup = new Group();
//                        mGroup.setId(Integer.parseInt(String.valueOf(mJsonObject.get("transId"))));
//                        mGroup.setJSON(String.valueOf(mJsonObject));
//                        Log.d(TAG,"Group"+mGroup.getJSON());
//                        realm.insertOrUpdate(mGroup);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
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

//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
                mRequestQueue.add(mTransactionFindAll);
//        Log.d(TAG,"mRequestQueue");
////                mRequestQueue.add(mGroupJsonArrayRequest);
//                Log.d(TAG,"Timer");
//            }
//        };
//        timer.schedule(timerTask,0,1000);



    }

}
