package com.zemoso.atul.splitwise.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
    //    private Button mTotalButton;
//    private Button mDebtButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionDetailRecyclerViewAdapter mTransactionDetailRecyclerViewAdapter;
    //endregion

    //region Data
    private Long transId;
    private Transaction mTransaction;
    private String description;
    private Double amount;
    private String mop;
    private String dot;
    private String imageUrl;
    private List<TransactionBalances> mData;
    private List<TransactionHolder> mItems;
    private TextView mMop;
    private TextView mDot;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        //region Data
        Bundle mBundle = getIntent().getExtras();
        transId = mBundle.getLong("transId");
        Realm realm = Realm.getDefaultInstance();
        mTransaction = realm.where(Transaction.class).equalTo("transId", transId).findFirst();

        mData = new ArrayList<>();
        mItems = new ArrayList<>();
        getTransactionData();

        //region Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_trans);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mTransaction.getDescription());
        //endregion

//        mData.add(new TransactionBalances());
//        mItems.add(new TransactionHolder());
        //endregion

        //region Views
        final ImageView mImageView = (ImageView) findViewById(R.id.profile_image);
//        mDescription = (TextView) findViewById(R.id.trans_detail_description);
        mAmount = (TextView) findViewById(R.id.trans_detail_total_amount);
        mMop = (TextView) findViewById(R.id.trans_detail_mop);
        mDot = (TextView) findViewById(R.id.trans_detail_dot);
//        mTotalButton = (Button) findViewById(R.id.trans_detail_amount);
//        mDebtButton = (Button) findViewById(R.id.trans_detail_debt);


        description = mTransaction.getDescription();
        amount = mTransaction.getAmount();
        mop = mTransaction.getMop();
        dot = mTransaction.getDot();
        imageUrl = mTransaction.getImageFilePath();
//          mDescription.setText(description);
        mAmount.setText(String.valueOf(amount));
        mMop.setText(mop);
        mDot.setText(dot);
        Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(mImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mImageView.setImageDrawable(circularBitmapDrawable);
                    }
                });


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
//        mTotalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toBalanceMode();
//            }
//        });
//
//        mDebtButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toDebtMode();
//            }
//        });
        //endregion


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                        Log.d(TAG, String.valueOf(jsonObject));
                        TransactionHolder transactionHolder = new TransactionHolder(jsonObject);
                        mItems.add(transactionHolder);
                        Log.d(TAG, String.valueOf(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Log.d(TAG, String.valueOf(mItems.size()));
                mTransactionDetailRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast.makeText(getApplicationContext(), "No data for transaction #" + transId, Toast.LENGTH_SHORT).show();

            }
        };
        JsonArrayRequest transJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(transJsonObject);
    }

    //endregion

    //endregion
}
