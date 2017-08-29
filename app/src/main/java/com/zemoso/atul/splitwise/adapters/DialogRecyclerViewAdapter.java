package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.User;

import java.util.List;

/**
 * Created by zemoso on 24/8/17.
 */

public class DialogRecyclerViewAdapter extends RecyclerView.Adapter<DialogRecyclerViewAdapter.RecyclerViewViewHolder> {

    private Context mContext;
    private List<User> mItems;
    private List<User> mSelected;

    public DialogRecyclerViewAdapter(Context mContext, List<User> mItems, List<User> mSelected) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mSelected = mSelected;
    }

    @Override
    public DialogRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_multi_selection, parent, false);
        return new DialogRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final User user = mItems.get(pos);
        String userName = user.getName();
        for (User use : mSelected) {
            if (use.equals(user))
                holder.mCheckBox.setChecked(true);
        }
        holder.mUserName.setText(userName);

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked())
                    mSelected.add(user);
                else
                    mSelected.remove(user);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        CheckBox mCheckBox;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.lb_text);
            mCheckBox = itemView.findViewById(R.id.lb_check);
        }
    }
}
