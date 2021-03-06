package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.javaBeans.GroupSplit;

import java.util.List;

/**
 * Created by zemoso on 19/8/17.
 */

public class GroupDetailRecyclerViewAdapter extends RecyclerView.Adapter<GroupDetailRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = GroupDetailRecyclerViewAdapter.class.getSimpleName();

    private List<GroupSplit> mItems;
    private Context mContext;
    //endregion

    public GroupDetailRecyclerViewAdapter(List<GroupSplit> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    //region Inherited Methods
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_group_detail, parent, false);
        return new GroupDetailRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        GroupSplit detail = mItems.get(pos);
        String creditorName = detail.getCreditorName();
        String debtorName = detail.getDebtorName();
        Double amount = detail.getAmount();
        Log.d(TAG, String.valueOf(amount));
        holder.creditorName.setText(creditorName);
        holder.debtorName.setText(debtorName);
        holder.amount.setText("Rs " + String.valueOf(amount));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView creditorName;
        TextView debtorName;
        TextView amount;
        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            creditorName = itemView.findViewById(R.id.group_detail_creditor);
            debtorName = itemView.findViewById(R.id.group_detail_debtor);
            amount = itemView.findViewById(R.id.group_detail_amount);
        }
    }
}
