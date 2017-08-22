package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.User;

import java.util.List;

import io.realm.Realm;

/**
 * Created by zemoso on 20/8/17.
 */

public class SingleRecyclerViewAdapter extends RecyclerView.Adapter<SingleRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = SingleRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<User> mItems;
    private ArrayAdapter<String> userArrayAdapter;
    private List<String> mSelUserNames;
    //endregion

    public SingleRecyclerViewAdapter(List<User> mUsers, Context context, List<String> mSelUserNames) {
        this.mContext = context;
        this.mItems = mUsers;
        this.mSelUserNames = mSelUserNames;
    }

    //region Inherited Methods
    @Override
    public SingleRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_add_member_item, parent, false);
        return new SingleRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {
        userArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.card_add_member_item, mSelUserNames);
        holder.textView.setAdapter(userArrayAdapter);
        holder.textView.setThreshold(0);
        holder.textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String userName = (String) adapterView.getSelectedItem();
                Realm realm = Realm.getDefaultInstance();
                User user = realm.where(User.class).equalTo("name", userName).findFirst();
                int pos = holder.getAdapterPosition();
                mItems.set(pos, user);
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

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView textView;
        Button button;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.add_member_edit_text);
            button = itemView.findViewById(R.id.add_member_button_subtract);
        }
    }
}
