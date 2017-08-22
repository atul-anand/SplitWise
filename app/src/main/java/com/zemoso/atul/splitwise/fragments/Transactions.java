package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.TransactionRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
import com.zemoso.atul.splitwise.models.Transaction;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class Transactions extends Fragment {

    //region Variable Declaration
    private static final String TAG = Transactions.class.getSimpleName();

    private Long mUserId;
    private List<RecyclerViewHolder> mItems;
    private List<Transaction> mTransactions;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionRecyclerViewAdapter mTransactionRecyclerViewAdapter;
    //endregion

    //region Constructor
    public Transactions() {
        // Required empty public constructor
    }

    public static Transactions newInstance() {
        return new Transactions();
    }
    //endregion

    //region Inherited Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUserId = preferences.getLong("userId", 0);
        mItems = new ArrayList<>();
        mTransactions = new ArrayList<>();

//        mItems.add(new RecyclerViewHolder());
//        mTransactions.add(new Transaction());

        mRecyclerView = view.findViewById(R.id.recycler_transaction);

        mLayoutManager = new LinearLayoutManager(getContext());
        mTransactionRecyclerViewAdapter = new TransactionRecyclerViewAdapter(mItems, getContext());


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        transactionFindByUserId(mUserId);
        Log.d(TAG, String.valueOf(mItems.size()));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTransactionRecyclerViewAdapter);


    }
    //endregion

    //region Private Methods
    private void addTransactions() {
        Realm realm = Realm.getDefaultInstance();
        mTransactions = realm.where(Transaction.class).findAll();
        for (Transaction transaction : mTransactions) {
            long mId = transaction.getTransId();
            String mImageUrl = transaction.getImageFilePath();
            String mHeading = transaction.getDescription();
            String mStatus = String.valueOf(transaction.getAmount());
            mItems.add(new RecyclerViewHolder(mId, mImageUrl, "", mHeading, mStatus));
        }
        mTransactionRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void transactionFindByUserId(long userId) {
        String extension = getResources().getString(R.string.url_transaction_findByUserId);
        String param = getResources().getString(R.string.url_user_id);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "") + extension
                + "?" + param + "=" + userId;
//        extension = getResources().getString(R.string.url_transaction_findAll);
//        mUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname","") + extension;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Transaction transaction = new Transaction(jsonObject);
                        realm.insertOrUpdate(transaction);
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                realm.commitTransaction();
                realm.close();
                addTransactions();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonArrayRequest userJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getContext()).addToRequestQueue(userJsonObject);

    }
    //endregion
}
