package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.User;

import java.util.List;

/**
 * Created by zemoso on 20/8/17.
 */

public class SingleRecyclerViewAdapter extends RecyclerView.Adapter<SingleRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = SingleRecyclerViewAdapter.class.getSimpleName();
    ArrayAdapter<String> userArrayAdapter;
    private Context mContext;
    private List<User> mItems;
    private List<User> mUsers;
    private List<String> mSelUserNames;
    //endregion

    public SingleRecyclerViewAdapter(List<User> mItems, List<User> mUsers, Context context, List<String> mSelUserNames) {
        this.mContext = context;
        this.mItems = mItems;
        this.mUsers = mUsers;
        this.mSelUserNames = mSelUserNames;
        userArrayAdapter = new ArrayAdapter<>(mContext, R.layout.card_autocomplete_item, mSelUserNames);
    }

    //region Inherited Methods
    @Override
    public SingleRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_add_lender_borrower_item, parent, false);
        return new SingleRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleRecyclerViewAdapter.RecyclerViewViewHolder holder, final int position) {

        holder.spinner.setAdapter(userArrayAdapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String userName = (String) adapterView.getSelectedItem();
                User user = getUserByName(userName);
                int pos = holder.getAdapterPosition();
                mItems.set(position, user);
                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                mItems.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    private User getUserByName(String userName) {
        for (User user : mUsers) {
            String name = user.getName();
            if (name.equals(userName))
                return user;
            Log.d(TAG, String.valueOf(user));
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        Spinner spinner;
        Button button;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.add_lender_borrower_edit_text);
            button = itemView.findViewById(R.id.add_lender_borrower_button_subtract);
        }
    }
}
