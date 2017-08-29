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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.MembersRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.UserPresent;
import com.zemoso.atul.splitwise.models.Group;
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
    private List<UserPresent> mItems;
    private Group mGroup;
    private String mGroupNam;
    private List<User> mUsers;
    private String mUrl;
    private String mUrlForPost;
    private Long mGroupId;
    private String mGroupParam;

    private TextView mGroupName;
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
            mItems.add(new UserPresent(-1L, "master", false));
            mMembersRecyclerViewAdapter.notifyDataSetChanged();
        }
    };
    private View.OnClickListener postJsonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int ctr = 0;
            for (UserPresent userPresent : mItems) {
                if (userPresent.isVerified())
                    ctr++;
            }
            if (ctr != mItems.size()) {
                Toast.makeText(getContext(), "All users not verified", Toast.LENGTH_SHORT).show();
            } else {
                mUrl = mUrlForPost;
                Log.d(TAG, mUrlForPost);
                for (UserPresent data : mItems) {
                    addUrl(String.valueOf(data.getUserId()));
                    Log.d(TAG, data.getUsername());
                }
                Log.d(TAG, mUrl);
                postJsonObject();
                AddMembers.this.dismiss();
            }
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
    //endregion

    //region Inherited Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_members, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mGroupId = getArguments().getLong("groupId");
        mItems = new ArrayList<>();
        mUsers = new ArrayList<>();
        getMembersByGroupId();

//        mItems.add(new UserPresent(4L,"das",false));

        mGroupName = view.findViewById(R.id.add_member_group_name);
        mButton = view.findViewById(R.id.add_member_button_done);
        mAddButton = view.findViewById(R.id.add_member_button_add_lender);


        mRecyclerView = view.findViewById(R.id.add_member_recycler);
        mLayoutManager = new LinearLayoutManager(getContext());
        mMembersRecyclerViewAdapter = new MembersRecyclerViewAdapter(mItems, getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUrlForPost = preferences.getString("Hostname", "");
        Log.d(TAG, mUrlForPost);
        mUrlForPost = mUrlForPost + getResources().getString(R.string.url_group_add);
        mGroupParam = getResources().getString(R.string.url_group_id);
        mUrlForPost = mUrlForPost + "?" + mGroupParam + "=" + mGroupId;
        Log.d(TAG, mUrlForPost);

        Realm realm = Realm.getDefaultInstance();
        mGroup = realm.where(Group.class).equalTo("groupId", mGroupId).findFirst();
        mGroupNam = mGroup.getGroupName();

        mGroupName.setText(mGroupNam);

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
        mUrl = mUrl + "&" + param + "=" + userId;
    }

    private void getData() {
        mItems.clear();
        for (User user : mUsers) {
            String data = user.getName();
            mItems.add(new UserPresent(user.getUserId(), data, true));
        }
        mMembersRecyclerViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //region VolleyRequests
    private void postJsonObject() {
        Log.d(TAG, mUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        VolleyRequests.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void getMembersByGroupId() {
        String url = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "");
        url = url + getResources().getString(R.string.url_group_findAllUsersByGroupId);
        String param = getResources().getString(R.string.url_group_id);
        url = url + "?" + param + "=" + mGroupId;
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, String.valueOf(response));
                mUsers.clear();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (jsonObject.equals(null))
                            continue;
                        mUsers.add(new User(jsonObject));
//                        realm.insertOrUpdate(mUsers.get(mUsers.size() - 1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
                getData();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
//                Toast.makeText(getContext(),"No users in group #" + mGroupNam,Toast.LENGTH_SHORT).show();
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, listener, errorListener);
        VolleyRequests.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
    //endregion

    //endregion

}
