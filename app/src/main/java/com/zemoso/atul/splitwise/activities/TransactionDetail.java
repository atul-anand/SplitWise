package com.zemoso.atul.splitwise.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.TransactionDetailRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.TransactionBalances;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;
import com.zemoso.atul.splitwise.models.Transaction;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class TransactionDetail extends AppCompatActivity {

    //region Variable Declaration
    private static final String TAG = TransactionDetail.class.getSimpleName();

    //region Views
    private TextView mDescription;
    private TextView mAmount;
    private Button mTotalButton;
    private Button mDebtButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionDetailRecyclerViewAdapter mTransactionDetailRecyclerViewAdapter;
    //endregion

    //region Data
    private Long transId;
    private Transaction mTransaction;
    private String description;
    private Double amount;
    private List<TransactionBalances> mData;
    private List<TransactionHolder> mItems;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        //region Action Bar
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //endregion

        //region Data
        Bundle mBundle = getIntent().getExtras();
        transId = mBundle.getLong("transId");
        mData = new ArrayList<>();
        mItems = new ArrayList<>();
        getTransactionData();
        getTransaction();
        //endregion

        //region Views
        mDescription = (TextView) findViewById(R.id.trans_detail_description);
        mAmount = (TextView) findViewById(R.id.trans_detail_total_amount);
        mTotalButton = (Button) findViewById(R.id.trans_detail_amount);
        mDebtButton = (Button) findViewById(R.id.trans_detail_debt);

        //region Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_show_transaction);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTransactionDetailRecyclerViewAdapter = new TransactionDetailRecyclerViewAdapter(mItems, getApplicationContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTransactionDetailRecyclerViewAdapter);
        //endregion

        //endregion

        //region Attach Listeners
        mTotalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBalanceMode();
            }
        });

        mDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDebtMode();
            }
        });
        //endregion


    }

    //region Private Methods

    //region Change Data
    private void toDebtMode() {
        mItems.clear();
        for (TransactionBalances balances : mData) {
            Long userId = balances.getUserId();
            String name = balances.getUsername();
            Double amount = balances.getDebt();
            mItems.add(new TransactionHolder(userId, name, amount));
        }
        mTransactionDetailRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void toBalanceMode() {
        mItems.clear();
        for (TransactionBalances balances : mData) {
            Long userId = balances.getUserId();
            String name = balances.getUsername();
            Double amount = balances.getAmount();
            mItems.add(new TransactionHolder(userId, name, amount));
        }
        mTransactionDetailRecyclerViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //region Volley Requests
    private void getTransactionData() {
        String extension = getResources().getString(R.string.url_transaction_findAmountsByTransId);
        String param = getResources().getString(R.string.url_transaction_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mUrl = preferences.getString("Hostname", "") + extension + "?"
                + param + "=" + transId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        TransactionBalances balances = new TransactionBalances(jsonObject);
                        mData.add(balances);
                        Log.d(TAG, String.valueOf(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                mTransactionDetailRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonArrayRequest transJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(transJsonObject);
    }

    private void getTransaction() {
        String extension = getResources().getString(R.string.url_transaction_findById);
        String param = getResources().getString(R.string.url_transaction_id);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("Hostname", "") + extension + "?"
                + param + "=" + transId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mTransaction = new Transaction(response);
                description = mTransaction.getDescription();
                amount = mTransaction.getAmount();
                mDescription.setText(description);
                mAmount.setText(String.valueOf(amount));
                Log.d(TAG, String.valueOf(response));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonObjectRequest transJsonObject = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(transJsonObject);
        Realm realm = Realm.getDefaultInstance();
        mTransaction = realm.where(Transaction.class).equalTo("transId", transId).findFirst();
        realm.close();
    }
    //endregion

    //endregion
}
