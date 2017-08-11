package com.zemoso.atul.splitwise.singletons;

import android.content.Context;
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
import com.zemoso.atul.splitwise.modules.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;


public class VolleyRequests {

    //region Variable Declaration
    private static final String TAG = VolleyRequests.class.getSimpleName();
    private static int reqNo;
    private String image;
    private static VolleyRequests mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;
    private String mUrl;
    //endregion

    //region Constructors
    private VolleyRequests(Context mContext){
        this.mContext = mContext;
        mRequestQueue = getRequestQueue();
        mUrl = mContext.getResources().getString(R.string.url_address);
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

    public <T> void addToRequestQueue(Request<T> request, String tag){
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

    public void transactionFindById(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findById);
        final String param = mContext.getResources().getString(R.string.url_transaction_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        mUrl = mUrl + tag + "?"+ param +"=" + id;
        JsonObjectRequest request = new JsonObjectRequest(mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Transaction transaction = new Transaction();
                            transaction.setId((Integer) response.get(resId));
                            transaction.setReqNo(reqNo);
                            transaction.setImageFilePath((String) response.get(image));
                            transaction.setJSON(String.valueOf(response));
                            Realm realm = Realm.getDefaultInstance();
                            realm.insertOrUpdate(transaction);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(tag,error.getMessage());
            }
        });
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        mUrl = mUrl + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Transaction transaction = new Transaction();
                                transaction.setId((Integer) jsonObject.get(resId));
                                transaction.setReqNo(reqNo);
                                transaction.setImageFilePath((String) jsonObject.get(image));
                                transaction.setJSON(String.valueOf(response));
                                Realm realm = Realm.getDefaultInstance();
                                realm.insertOrUpdate(transaction);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(tag,error.getMessage());
            }
        });
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindByGroupId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findByGroupId);
        final String param = mContext.getResources().getString(R.string.url_group_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        mUrl = mUrl + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Transaction transaction = new Transaction();
                                transaction.setId((Integer) jsonObject.get(resId));
                                transaction.setReqNo(reqNo);
                                transaction.setImageFilePath((String) jsonObject.get(image));
                                transaction.setJSON(String.valueOf(response));
                                Realm realm = Realm.getDefaultInstance();
                                realm.insertOrUpdate(transaction);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(tag,error.getMessage());
            }
        });
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindGroupByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findGroupExpByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        mUrl = mUrl + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Transaction transaction = new Transaction();
                                transaction.setId((Integer) jsonObject.get(resId));
                                transaction.setReqNo(reqNo);
                                transaction.setImageFilePath((String) jsonObject.get(image));
                                transaction.setJSON(String.valueOf(response));
                                Realm realm = Realm.getDefaultInstance();
                                realm.insertOrUpdate(transaction);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(tag,error.getMessage());
            }
        });
        addToRequestQueue(request, String.valueOf(reqNo));
    }

    public void transactionFindNonGroupByUserId(long id){
        final String tag = mContext.getResources().getString(R.string.url_transaction_findNonGroupExpByUserId);
        final String param = mContext.getResources().getString(R.string.url_user_id);
        final String resId = mContext.getResources().getString(R.string.url_transaction_id);
        mUrl = mUrl + tag + "?"+ param +"=" + id;
        JsonArrayRequest request = new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++)
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Transaction transaction = new Transaction();
                                transaction.setId((Integer) jsonObject.get(resId));
                                transaction.setReqNo(reqNo);
                                transaction.setImageFilePath((String) jsonObject.get(image));
                                transaction.setJSON(String.valueOf(response));
                                Realm realm = Realm.getDefaultInstance();
                                realm.insertOrUpdate(transaction);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        reqNo++;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(tag,error.getMessage());
            }
        });
        addToRequestQueue(request, String.valueOf(reqNo));
    }

}
