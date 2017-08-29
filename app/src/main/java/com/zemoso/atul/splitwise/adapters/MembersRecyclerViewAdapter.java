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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
            holder.mEditText.setText(userName);
        if (userPresent.isVerified()) {
            holder.mEditText.setBackgroundResource(R.drawable.text_border_green);
            holder.mCheckButton.setVisibility(View.GONE);
            holder.mEditText.setWidth(R.dimen.add_member_edit_width_checked);
        }
        else
            holder.mEditText.setBackgroundResource(R.drawable.text_border_red);

        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                holder.mEditText.setWidth(R.dimen.add_member_edit_width);
                holder.mCheckButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Checking user present in database", Toast.LENGTH_SHORT).show();
                String userName = String.valueOf(holder.mEditText.getText());
                Log.d(TAG, userName);
                Log.d(TAG, String.valueOf(holder));
                checkUserPresent(userName, holder);
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

    private void checkUserPresent(String userName, final RecyclerViewViewHolder holder) {
        Log.d(TAG, String.valueOf(holder));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mUrl = preferences.getString("Hostname", "");
        String extension = mContext.getResources().getString(R.string.url_user_string);
        String param = mContext.getResources().getString(R.string.url_user_name);
        final int pos = holder.getAdapterPosition();
        mUrl = mUrl + extension + "?" + param + "=" + userName;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, String.valueOf(response));
                User user = new User(response);
                UserPresent userPresent = mItems.get(pos);
                Long userId = user.getUserId();
                String userName = user.getName();
                userPresent.setUserId(userId);
                userPresent.setUsername(userName);
                userPresent.setVerified(true);

                Toast.makeText(mContext, "User Present in Database", Toast.LENGTH_SHORT).show();
                holder.mEditText.setText(userName);
                holder.mEditText.setBackgroundResource(R.drawable.text_border_green);
                holder.mCheckButton.setVisibility(View.GONE);
                holder.mEditText.setWidth(R.dimen.add_member_edit_width_checked);
                notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "User not present in Database", Toast.LENGTH_SHORT).show();
                Log.d(TAG, String.valueOf(error));
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

        EditText mEditText;
        Button mCheckButton;
        Button mSubtractButton;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mEditText = itemView.findViewById(R.id.add_member_edit_text);
            mCheckButton = itemView.findViewById(R.id.add_member_button_check);
            mSubtractButton = itemView.findViewById(R.id.add_member_button_subtract);
        }
    }
}
