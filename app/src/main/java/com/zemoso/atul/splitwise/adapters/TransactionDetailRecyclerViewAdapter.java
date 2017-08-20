package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;

import java.util.List;

/**
 * Created by zemoso on 17/8/17.
 */

public class TransactionDetailRecyclerViewAdapter extends RecyclerView.Adapter<TransactionDetailRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = TransactionDetailRecyclerViewAdapter.class.getSimpleName();

    private List<TransactionHolder> mItems;
    private Context mContext;
    //endregion

    public TransactionDetailRecyclerViewAdapter(List mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    //region Inherited Methods
    @Override
    public TransactionDetailRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_transaction_detail, parent, false);
        return new TransactionDetailRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionDetailRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {
        final TransactionHolder mCardData = mItems.get(position);
        holder.mUsername.setText(mCardData.getName());
        holder.mAmount.setText(String.valueOf(mCardData.getAmount()));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView mUsername;
        TextView mAmount;


        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mUsername = itemView.findViewById(R.id.card_heading);
            mAmount = itemView.findViewById(R.id.card_status);

        }
    }
}
