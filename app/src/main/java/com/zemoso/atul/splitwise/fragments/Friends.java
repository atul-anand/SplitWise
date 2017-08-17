package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {

    private static final String TAG = Friends.class.getSimpleName();

    private Button mButton;
    private PopupMenu mPopupMenu;
    private Button mAddButton;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FriendRecyclerViewAdapter mFriendRecyclerViewAdapter;
    private List<RecyclerViewHolder> mItems;
    private List<User> mUsers;

    private SharedPreferences preferences;
    private Long mUserId;


    public Friends() {
        // Required empty public constructor
    }
    public static Friends newInstance(){
        return new Friends();
    }

    public static void changeData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton = view.findViewById(R.id.total_menu);
        mPopupMenu = new PopupMenu(getContext(),mButton);

        mRecyclerView = view.findViewById(R.id.recycler_friends);
        mLayoutManager = new LinearLayoutManager(getContext());
        mItems = new ArrayList<>();
        mUsers = new ArrayList<>();
        mFriendRecyclerViewAdapter = new FriendRecyclerViewAdapter(mItems, getContext());
        preferences = getActivity().getSharedPreferences("Settings", 0);
        mUserId = preferences.getLong("userId", 0);
        mAddButton = view.findViewById(R.id.addFriends);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        VolleyRequests.getInstance(getContext()).userFindAll();
        userFindAll();
        Log.d(TAG, String.valueOf(mItems.size()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFriendRecyclerViewAdapter);

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
                getActivity().getSupportFragmentManager().beginTransaction().add(new AddUser(),"Add User").commit();
                VolleyRequests.getInstance(getContext()).userFindAll();
                addUsers();
                mFriendRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

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
        String mUrl = getActivity().getSharedPreferences("Settings", 0).getString("Hostname", "") + extension;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mUsers = new ArrayList<User>();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject);
                        mUsers.add(user);
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
}
