package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.TransactionRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
import com.zemoso.atul.splitwise.modules.Transaction;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Transactions extends Fragment {

    private static final String TAG = Transactions.class.getSimpleName();

    private Button mButton;
    private PopupMenu mPopupMenu;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionRecyclerViewAdapter mTransactionRecyclerViewAdapter;
    private List<RecyclerViewHolder> mItems;

    private SharedPreferences preferences;
    private Long mUserId;

    public Transactions() {
        // Required empty public constructor
    }

    public static Transactions newInstance() {
        return new Transactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton = view.findViewById(R.id.total_menu);
        mPopupMenu = new PopupMenu(getContext(), mButton);

        mRecyclerView = view.findViewById(R.id.recycler_transaction);
        mLayoutManager = new LinearLayoutManager(getContext());
        mItems = new ArrayList<>();
        mTransactionRecyclerViewAdapter = new TransactionRecyclerViewAdapter(mItems, getContext());


        mUserId = preferences.getLong("UserId", 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        VolleyRequests.getInstance(getContext()).transactionFindByUserId(mUserId);
        addTransactions();
        Log.d(TAG, String.valueOf(mItems.size()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTransactionRecyclerViewAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.getMenuInflater().inflate(R.menu.menu_total_balance, mPopupMenu.getMenu());
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getContext(),
                                "You clicked" + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                mPopupMenu.show();
            }
        });


    }

    private void addTransactions() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Transaction> mTransactionData = realm.where(Transaction.class).findAll();
        for (Transaction transaction : mTransactionData) {
            long mId = transaction.getTransId();
            String mImageUrl = transaction.getImageFilePath();
            String mHeading = transaction.getDescription();
            String mStatus = String.valueOf(transaction.getAmount());
            mItems.add(new RecyclerViewHolder(mId, mImageUrl, "", mHeading, mStatus));
        }
    }

}
