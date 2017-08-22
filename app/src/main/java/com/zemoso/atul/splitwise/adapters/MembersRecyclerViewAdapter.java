package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.javaBeans.UserPresent;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

/**
 * Created by zemoso on 18/8/17.
 */

public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = MembersRecyclerViewAdapter.class.getSimpleName();

    private List<UserPresent> mItems;
    private Context mContext;
    //endregion

    public MembersRecyclerViewAdapter(List<UserPresent> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    //region Inherited Methods
    @Override
    public MembersRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_add_member_item, parent, false);
        return new MembersRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MembersRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {

        final int pos = holder.getAdapterPosition();
        UserPresent userPresent = mItems.get(pos);
        String userName = userPresent.getUsername();
        Log.d(TAG, userPresent.getUsername());
        if (!Objects.equals(userName, "master"))
            holder.mAutoCompleteTextView.setText(userName);
        if (userPresent.getVerified())
            holder.mAutoCompleteTextView.setBackgroundResource(R.drawable.text_border_green);
        else
            holder.mAutoCompleteTextView.setBackgroundResource(R.drawable.text_border_red);

        holder.mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                holder.mAutoCompleteTextView.setBackgroundResource(R.drawable.text_border_red);
//                mItems.get(pos).setUsername(String.valueOf(charSequence));
                Log.d(TAG, String.valueOf(charSequence));
//                notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = String.valueOf(holder.mAutoCompleteTextView.getText());
                checkUserPresent(userName, pos, holder);
            }
        });
        holder.mSubtractButton.setVisibility(View.GONE);
        holder.mSubtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    //    TODO: Customize Listener
    private void checkUserPresent(String userName, final int pos, final RecyclerViewViewHolder holder) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mUrl = preferences.getString("Hostname", "");
        String extension = mContext.getResources().getString(R.string.url_user_string);
        mUrl = mUrl + extension + "?s=" + userName;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User user = new User(response);
                UserPresent userPresent = mItems.get(pos);
                Long userId = user.getUserId();
                String userName = user.getName();
                userPresent.setUserId(userId);
                userPresent.setVerified(true);
                holder.mAutoCompleteTextView.setText(userName);
                holder.mAutoCompleteTextView.setBackgroundResource(R.drawable.text_border_green);
                notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(mContext).addToRequestQueue(jsonObjectRequest);


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        AutoCompleteTextView mAutoCompleteTextView;
        Button mCheckButton;
        Button mSubtractButton;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mAutoCompleteTextView = itemView.findViewById(R.id.add_member_edit_text);
            mCheckButton = itemView.findViewById(R.id.add_member_button_check);
            mSubtractButton = itemView.findViewById(R.id.add_member_button_subtract);
        }
    }
}
