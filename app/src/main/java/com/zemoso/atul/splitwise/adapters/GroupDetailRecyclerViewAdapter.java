package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zemoso.atul.splitwise.R;

import java.util.List;

/**
 * Created by zemoso on 19/8/17.
 */

public class GroupDetailRecyclerViewAdapter extends RecyclerView.Adapter<GroupDetailRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = GroupDetailRecyclerViewAdapter.class.getSimpleName();

    private List<String> mItems;
    private Context mContext;
    //endregion

    public GroupDetailRecyclerViewAdapter(List<String> mItems, Context mContext) {
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
        String detail = mItems.get(pos);
        holder.textView.setText(detail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.group_detail_text_view);
        }
    }
}
