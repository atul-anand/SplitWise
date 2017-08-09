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
import com.zemoso.atul.splitwise.modules.Friend;
import com.zemoso.atul.splitwise.modules.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;


public class LaunchDownloads extends IntentService {

    private static final String TAG = LaunchDownloads.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private JsonArrayRequest mFriendJsonArrayRequest;
    private JsonArrayRequest mGroupJsonArrayRequest;
    private JSONObject mJsonObject;
    private String mFriendsUrl;
    private String mGroupsUrl;
    private Long mUserId;
    private Bundle mBundle;
    private Friend mFriend;
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
//        TODO: Set GET Url Params
//        mFriendsUrl = mFriendsUrl + "/" + mUserId;
//        mGroupsUrl = mGroupsUrl + "/" + mUserId;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mFriendJsonArrayRequest = new JsonArrayRequest(
                mFriendsUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for(int i=0;i< response.length();i++){
                    try {
                        mJsonObject = response.getJSONObject(i);
                        mFriend = new Friend();
                        mFriend.setId((Integer) mJsonObject.get("id"));
                        mFriend.setJSON(mJsonObject.toString());
                        realm.insertOrUpdate(mFriend);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
            }
        }
        );
        mRequestQueue.add(mFriendJsonArrayRequest);
        mGroupJsonArrayRequest = new JsonArrayRequest(
                mGroupsUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for(int i=0;i< response.length();i++){
                    try {
                        mJsonObject = response.getJSONObject(i);
                        mGroup = new Group();
                        mGroup.setId((Integer) mJsonObject.get("id"));
                        mGroup.setJSON(mJsonObject.toString());
                        realm.insertOrUpdate(mGroup);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
            }
        }
        );
        mRequestQueue.add(mGroupJsonArrayRequest);
    }

}
