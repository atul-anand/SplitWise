package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.GroupRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Groups extends Fragment {

    private static final String TAG = Groups.class.getSimpleName();

    private Button mButton;
    private PopupMenu mPopupMenu;
    private Button mAddButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupRecyclerViewAdapter mGroupRecyclerViewAdapter;
    private List<RecyclerViewHolder> mItems;
    private List<Group> mGroups;

    private SharedPreferences preferences;
    private Long mUserId;

    public Groups() {
        // Required empty public constructor
    }
    public static Groups newInstance(){
        return new Groups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButton = view.findViewById(R.id.total_menu);
        mPopupMenu = new PopupMenu(getContext(),mButton);

        mRecyclerView = view.findViewById(R.id.recycler_groups);
        mLayoutManager = new LinearLayoutManager(getContext());
        mItems = new ArrayList<>();
        mGroups = new ArrayList<>();
        mGroupRecyclerViewAdapter = new GroupRecyclerViewAdapter(mItems, getContext());

        mAddButton = view.findViewById(R.id.addGroups);
//        preferences = getActivity().getSharedPreferences("Settings", 0);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUserId = preferences.getLong("UserId", 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        groupFindByUserId(mUserId);
        Log.d(TAG, String.valueOf(mItems.size()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mGroupRecyclerViewAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.getMenuInflater().inflate(R.menu.menu_total_balance,mPopupMenu.getMenu());
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getContext(),
                                "You clicked"+item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                mPopupMenu.show();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().add(new AddGroup(),"Add Group").commit();
                VolleyRequests.getInstance(getContext()).groupFindByUserId(mUserId);
                addGroups();
                mGroupRecyclerViewAdapter.notifyDataSetChanged();
//                getActivity().getSupportFragmentManager().beginTransaction().add(new AddMembers(),"Add Member").commit();
            }
        });
    }

    private void addGroups() {
        for (Group group : mGroups) {
            long mId = group.getGroupId();
            String mImageUrl = group.getImageFilePath();
            String mHeading = group.getGroupName();
            String mStatus = group.getCreatedBy();
            mItems.add(new RecyclerViewHolder(mId, mImageUrl, "", mHeading, mStatus));
        }
    }

    private void groupFindByUserId(long userId) {
        String extension = getResources().getString(R.string.url_group_findByUserId);
        String param = getResources().getString(R.string.url_user_id);
        String mUrl = getActivity().getSharedPreferences("Settings", 0).getString("Hostname", "") + extension
                + "?" + param + "=" + userId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Group group = new Group(jsonObject);
                        mGroups.add(group);
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                addGroups();
                mGroupRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonArrayRequest userJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getContext()).addToRequestQueue(userJsonObject);

    }
}
