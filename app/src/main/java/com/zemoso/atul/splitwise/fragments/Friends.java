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
import com.zemoso.atul.splitwise.adapters.FriendRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
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
public class Friends extends Fragment {

    //region Variable Decalaration
    private static final String TAG = Friends.class.getSimpleName();

    //region Data
    private SharedPreferences preferences;
    private Long mUserId;
    private List<RecyclerViewHolder> mItems;
    private List<User> mUsers;
    //endregion

    //region Views
    private Button mButton;
    private PopupMenu mPopupMenu;
    private Button mAddButton;
    //endregion

    //region Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FriendRecyclerViewAdapter mFriendRecyclerViewAdapter;
    //endregion
    //endregion
    //region Listeners
    private View.OnClickListener popUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupMenu.getMenuInflater().inflate(R.menu.menu_total_balance, mPopupMenu.getMenu());
            mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(getContext(),
                            "You clicked" + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mPopupMenu.show();
        }
    };
    private View.OnClickListener addUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().beginTransaction().add(AddUser.newInstance(), "Add User").commit();
            VolleyRequests.getInstance(getContext()).userFindAll();
            addUsers();
            mFriendRecyclerViewAdapter.notifyDataSetChanged();
        }
    };
    //endregion

    //region Constructor
    public Friends() {
        // Required empty public constructor
    }

    public static Friends newInstance(){
        return new Friends();
    }

    //region Inherited Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
    //endregion

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Data
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mUserId = preferences.getLong("userId", 0);
        mItems = new ArrayList<>();
        mUsers = new ArrayList<>();
        //endregion

        //region Views
        mButton = view.findViewById(R.id.total_menu);
        mPopupMenu = new PopupMenu(getContext(),mButton);
        mAddButton = view.findViewById(R.id.addFriends);
        //endregion

        //region Recycler View
        mRecyclerView = view.findViewById(R.id.recycler_friends);
        mLayoutManager = new LinearLayoutManager(getContext());
        mFriendRecyclerViewAdapter = new FriendRecyclerViewAdapter(mItems, getContext());
        //endregion

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //region Data
        userFindAll();
        Log.d(TAG, String.valueOf(mItems.size()));
        //endregion

        //region Recycler View
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFriendRecyclerViewAdapter);
        //endregion

        //region Attach Listeners
        mButton.setOnClickListener(popUpListener);
        mAddButton.setOnClickListener(addUserListener);
        //endregion
    }

    //endregion

    //region Private Methods
    private void addUsers() {
        for (User user : mUsers) {
            if (user.getUserId() == mUserId)
                continue;
            long mId = user.getUserId();
            String mImageUrl = user.getImageFilePath();
            String mHeading = user.getName();
            String mStatus = String.valueOf(user.getDebt());
            mItems.add(new RecyclerViewHolder(mId, mImageUrl, "", mHeading, mStatus));
        }
    }

    private void userFindAll() {
        String extension = getResources().getString(R.string.url_user_findAll);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "") + extension;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject);
                        mUsers.add(user);
                        realm.insertOrUpdate(user);
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                realm.commitTransaction();
                realm.close();
                addUsers();
                mFriendRecyclerViewAdapter.notifyDataSetChanged();
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
