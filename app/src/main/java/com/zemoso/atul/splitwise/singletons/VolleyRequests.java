package com.zemoso.atul.splitwise.singletons;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.models.Transaction;
import com.zemoso.atul.splitwise.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;


public class VolleyRequests {

    //region Variable Declaration
    private static final String TAG = VolleyRequests.class.getSimpleName();

    private static final int USER = 1;
    private static final int GROUP = 2;
    private static final int TRANSACTION = 3;

    private static int reqNo;
    private static VolleyRequests mInstance;
    private String image;
    private RequestQueue mRequestQueue;
    private Context mContext;
    private String mHostName;

    private SharedPreferences preferences;
    //endregion

    //region Constructors
//    @TargetApi(Build.VERSION_CODES.N)
    private VolleyRequests(Context mContext){
        this.mContext = mContext;
        mRequestQueue = getRequestQueue();
//        mHostName = mContext.getResources().getString(R.string.url_address);
//        preferences = mContext.getSharedPreferences("Settings", 0);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mHostName = preferences.getString("Hostname", "");
//        mHostName = ;
        Log.d(TAG, mHostName);
//        mHostName = mContext.getSharedPreferences();
        VolleyRequests.reqNo = 0;
        image = mContext.getResources().getString(R.string.url_image);
    }

    public static synchronized VolleyRequests getInstance(Context mContext){
        if(mInstance==null)
            mInstance = new VolleyRequests(mContext);
        return mInstance;
    }

    private RequestQueue getRequestQueue(){
        if(mRequestQueue==null)
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mRequestQueue;
    }
    //endregion

    //region Volley Methods
    private <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue!=null)
            mRequestQueue.cancelAll(tag);
    }
    //endregion

    //region GET Requests

    //region Public Methods

    //region Transactions
    public void transactionFindAll(){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findAll);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        final String mUrl = mHostName + tag;
        JsonArrayRequest request = transactionJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindById(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findById);
        final String param = mContext.getResources().getString(R.string.url_transaction_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonObjectRequest request = transactionJsonObject(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = transactionJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindByGroupId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findByGroupId);
        final String param = mContext.getResources().getString(R.string.url_group_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = transactionJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindGroupByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findGroupExpByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = transactionJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindNonGroupByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findNonGroupExpByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = transactionJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }
    //endregion

    //region Group
    public void groupFindAll(){
        final String tag = mContext.getResources().getString(R.string.url_group_findAll);
        final String resId = mContext.getResources().getString(R.string.url_group_id);
        final String mUrl = mHostName + tag;
        JsonArrayRequest request = groupJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void groupFindById(long id){
        final String tag = mContext.getResources().getString(R.string.url_group_findById);
        final String param = mContext.getResources().getString(R.string.url_group_id);
        final String resId = mContext.getResources().getString(R.string.url_group_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonObjectRequest request = groupJsonObject(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void groupFindByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_group_findByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_group_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = groupJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }
    //endregion

    //region User
    public void userFindAll(){
        final String tag = mContext.getResources().getString(R.string.url_user_findAll);
        final String resId = mContext.getResources().getString(R.string.url_user_id);
        final String mUrl = mHostName + tag;
        JsonArrayRequest request = groupJsonArray(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }


    public void userFindById(long id){
        final String tag = mContext.getResources().getString(R.string.url_user_findById);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_user_id);
        final String mUrl = mHostName + tag + "?"+ param +"=" + id;
        JsonObjectRequest request = groupJsonObject(mUrl,resId,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }
    //endregion

    //endregion

    //region Private Requests

    //region Transactions
    private JsonArrayRequest transactionJsonArray(String mUrl, final String resId, final String tag) {
        return new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Transaction transaction = new Transaction(jsonObject);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.insertOrUpdate(transaction);
                                realm.commitTransaction();
                                realm.close();
                                Log.d(TAG, String.valueOf(jsonObject));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }

    private JsonObjectRequest transactionJsonObject(String mUrl, final String resId, final String tag) {
        return new JsonObjectRequest(mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Transaction transaction = new Transaction(response);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.insertOrUpdate(transaction);
                        realm.commitTransaction();
                        realm.close();
                        reqNo++;
                        Log.d(TAG, String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }
    //endregion

    //region Group
    private JsonArrayRequest groupJsonArray(String mUrl, final String resId, final String tag) {
        return new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Group group = new Group(jsonObject);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.insertOrUpdate(group);
                                realm.commitTransaction();
                                realm.close();
                                Log.d(TAG, String.valueOf(jsonObject));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }

    private JsonObjectRequest groupJsonObject(String mUrl, final String resId, final String tag) {
        return new JsonObjectRequest(mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Group group = new Group(response);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.insertOrUpdate(group);
                        realm.commitTransaction();
                        realm.close();
                        reqNo++;
                        Log.d(TAG, String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }
    //endregion

    //region User
    private JsonArrayRequest userJsonArray(String mUrl, final String resId, final String tag) {
        return new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                User user = new User(jsonObject);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.insertOrUpdate(user);
                                realm.commitTransaction();
                                realm.close();
                                Log.d(TAG, String.valueOf(jsonObject));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }

    private JsonObjectRequest userJsonObject(String mUrl, final String resId, final String tag) {
        return new JsonObjectRequest(mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        User user = new User(response);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.insertOrUpdate(user);
                        realm.commitTransaction();
                        realm.close();
                        reqNo++;
                        Log.d(TAG, String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }
    //endregion

    //endregion

    //endregion

    //region POST Requests

    public void save(JSONObject jsonObject, int type){
        String tag = "";
        switch (type){
            case VolleyRequests.USER: tag = mContext.getResources().getString(R.string.url_user_save);
                break;
            case VolleyRequests.GROUP: tag = mContext.getResources().getString(R.string.url_group_save);
                break;
            case VolleyRequests.TRANSACTION: tag = mContext.getResources().getString(R.string.url_transaction_save);
                break;
        }
        final String mUrl = mHostName + tag;
        Log.d(TAG,mUrl);
        JsonObjectRequest request = postJsonObject(mUrl,jsonObject,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    private JsonObjectRequest postJsonObject(String mUrl, final JSONObject jsonObject, final String tag) {
        return new JsonObjectRequest(Request.Method.POST, mUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }

    //endregion

    //region PUT Requests

    public void update(JSONObject jsonObject, int type){
        String tag = "";
        switch (type){
            case VolleyRequests.USER: tag = mContext.getResources().getString(R.string.url_user_update);
                break;
            case VolleyRequests.GROUP: tag = mContext.getResources().getString(R.string.url_group_update);
                break;
            case VolleyRequests.TRANSACTION: tag = mContext.getResources().getString(R.string.url_transaction_update);
                break;
        }
        final String mUrl = mHostName + tag;
        JsonObjectRequest request = putJsonObject(mUrl,jsonObject,tag);
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    private JsonObjectRequest putJsonObject(String mUrl, final JSONObject jsonObject, final String tag) {
        return new JsonObjectRequest(Request.Method.PUT, mUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
            }
        });
    }



    //endregion

}
