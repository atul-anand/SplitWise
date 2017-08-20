package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.MembersRecyclerViewAdapter;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMembers extends DialogFragment {

    //region Variable Declaration
    private static final String TAG = AddMembers.class.getSimpleName();

    private SharedPreferences preferences;
    private List<String> mItems;
    private List<User> mUsers;
    private String mUrl;
    private Long mGroupId;
    private String mGroupParam;

    private Button mButton;
    private Button mAddButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MembersRecyclerViewAdapter mMembersRecyclerViewAdapter;
    //endregion
    //region Listener
    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mItems.add(String.valueOf(-1));
            mMembersRecyclerViewAdapter.notifyDataSetChanged();
        }
    };
    private View.OnClickListener postJsonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (String data : mItems) {
                addUrl(data);
                Log.d(TAG, data);
            }
            postJsonObject();
            mUrl = getActivity().getSharedPreferences("Settings", 0).getString("Hostname", "");
            mUrl = mUrl + getResources().getString(R.string.url_group_add);
            AddMembers.this.dismiss();
        }
    };
    //endregion

    //region Constructor
    public AddMembers() {
        // Required empty public constructor
    }

    public static AddMembers newInstance() {
        return new AddMembers();
    }

    //region Inherited Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_members, container, false);
    }
    //endregion

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupId = 21L;
        mItems = new ArrayList<>();
        getMembersByGroupId();
        mItems.add(String.valueOf(-1));

        mButton = view.findViewById(R.id.add_member_button_done);
        mAddButton = view.findViewById(R.id.add_member_button_add);


        mRecyclerView = view.findViewById(R.id.add_member_recycler);
        mLayoutManager = new LinearLayoutManager(getContext());
        mMembersRecyclerViewAdapter = new MembersRecyclerViewAdapter(mItems, getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUrl = preferences.getString("Hostname", "");

        mUrl = mUrl + getResources().getString(R.string.url_group_add);
        mGroupParam = getResources().getString(R.string.url_group_id);
        mUrl = mUrl + "?" + mGroupParam + "=" + mGroupId;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMembersRecyclerViewAdapter);

        mAddButton.setOnClickListener(addButtonListener);
        mButton.setOnClickListener(postJsonListener);

    }
    //endregion

    //region Private Methods
    //region Data
    private void addUrl(String userId) {
        String param = getResources().getString(R.string.url_user_id);
        mUrl = mUrl + "?" + param + "=" + userId;
    }

    private void getData() {
        for (User user : mUsers) {
            String data = user.getName();
            mItems.add(data);
        }
    }
    //endregion

    //region VolleyRequests
    private void postJsonObject() {
        Log.d(TAG, mUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, String.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        VolleyRequests.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getMembersByGroupId() {
        String url = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "");
        url = url + getResources().getString(R.string.url_group_findAllUsersByGroupId);
        String param = getResources().getString(R.string.url_group_id);
        url = url + "?" + param + "=" + mGroupId;
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        mUsers.add(new User(jsonObject));
                        realm.insertOrUpdate(mUsers.get(mUsers.size() - 1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
                getData();
                mMembersRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, listener, errorListener);
        VolleyRequests.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
    //endregion

    //endregion

}
