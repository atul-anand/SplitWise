package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.GroupRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
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
public class Groups extends Fragment {

    //region Variable Declaration
    private static final String TAG = Groups.class.getSimpleName();

    private SharedPreferences preferences;
    private Long mUserId;
    private User mUser;
    private Double mDebt;
    private String mStatus;
    private List<RecyclerViewHolder> mItems;
    private List<Group> mGroups;

    //    private Button mButton;
//    private PopupMenu mPopupMenu;
    private Button mAddButton;
    private ImageView mProfilePic;
    private TextView mTotalStatus;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupRecyclerViewAdapter mGroupRecyclerViewAdapter;
    //endregion

    //region Listener
//    private View.OnClickListener popUpListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            mPopupMenu.getMenu().clear();
//            mPopupMenu.getMenuInflater().inflate(R.menu.menu_total_balance, mPopupMenu.getMenu());
//            mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    Toast.makeText(getContext(),
//                            "You clicked" + item.getTitle(),
//                            Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });
//            mPopupMenu.show();
//        }
//    };
    private View.OnClickListener addGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().beginTransaction().add(AddGroup.newInstance(), "Add Group").commit();
            groupFindByUserId(mUserId);
//                getActivity().getSupportFragmentManager().beginTransaction().add(new AddMembers(),"Add Member").commit();
        }
    };
    //endregion

    //region Constructor
    public Groups() {
        // Required empty public constructor
    }

    public static Groups newInstance(){
        return new Groups();
    }
    //endregion

    //region Inherited Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Data

        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mUserId = preferences.getLong("userId", 0);
        } catch (Exception e) {
            mUserId = -1L;
        }

        mItems = new ArrayList<>();
        mGroups = new ArrayList<>();


//        mItems.add(new RecyclerViewHolder(1, "a", "b", "c", "d"));
//        mGroups.add(new Group());
        //endregion

        //region Views
        mProfilePic = view.findViewById(R.id.totalProfilePic);
        mTotalStatus = view.findViewById(R.id.totalStatus);
//
//        mButton = view.findViewById(R.id.total_menu);
//        mPopupMenu = new PopupMenu(getContext(),mButton);
        mAddButton = view.findViewById(R.id.addGroups);
        //endregion

        //region Recycler View
        mRecyclerView = view.findViewById(R.id.recycler_groups);
        mLayoutManager = new LinearLayoutManager(getContext());
        mGroupRecyclerViewAdapter = new GroupRecyclerViewAdapter(mItems, getContext());
        //endregion

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        groupFindByUserId(mUserId);
        Log.d(TAG, String.valueOf(mItems.size()));

        try {
            Realm realm = Realm.getDefaultInstance();
            mUser = realm.where(User.class).equalTo("userId", mUserId).findFirst();
            Log.d(TAG, String.valueOf(mUser));
            mDebt = mUser.getDebt();
            if (mDebt.equals(0.0))
                mStatus = "You are settled up";
            else if (mDebt > 0.0)
                mStatus = "You are owed Rs." + mDebt;
            else
                mStatus = "You owe Rs." + Math.abs(mDebt);

        } catch (Exception e) {
            mStatus = "You are settled up.";
        }
        mTotalStatus.setText(mStatus);
        Glide.with(this)
                .load(mUser.getImageFilePath())
                .into(mProfilePic);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mGroupRecyclerViewAdapter);

//        mButton.setOnClickListener(popUpListener);
        mAddButton.setOnClickListener(addGroupListener);
    }
    //endregion

    //region Private Methods
    private void addGroups() {
        Realm realm = Realm.getDefaultInstance();
        mItems.clear();
        mGroups = realm.where(Group.class).findAll();
        for (Group group : mGroups) {
            long mId = group.getGroupId();
            String mImageUrl = group.getImageFilePath();
            String mHeading = group.getGroupName();
            String mStatus = group.getCreatedBy();
            mItems.add(new RecyclerViewHolder(mId, mImageUrl, "", mHeading, mStatus));
            mGroupRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void groupFindByUserId(long userId) {
        String extension = getResources().getString(R.string.url_group_findByUserId);
        String param = getResources().getString(R.string.url_user_id);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "") + extension
                + "?" + param + "=" + userId;
        extension = getResources().getString(R.string.url_group_findAll);
        mUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "") + extension;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Group group = new Group(jsonObject);
                        realm.insertOrUpdate(group);
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                realm.commitTransaction();
                realm.close();
                addGroups();
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
    //endregion

}
