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

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.GroupRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
import com.zemoso.atul.splitwise.modules.Group;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

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
        mGroupRecyclerViewAdapter = new GroupRecyclerViewAdapter(mItems, getContext());

        mAddButton = view.findViewById(R.id.addGroups);

        mUserId = preferences.getLong("UserId", 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        VolleyRequests.getInstance(getContext()).groupFindByUserId(mUserId);
        addGroups();
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
            }
        });
    }

    private void addGroups() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Group> mGroupData = realm.where(Group.class).findAll();
        for (Group group : mGroupData) {
            long mId = group.getGroupId();
            String mImageUrl = group.getImageFilePath();
            String mHeading = group.getGroupName();
            String mStatus = group.getCreatedBy();
            mItems.add(new RecyclerViewHolder(mId, mImageUrl, "", mHeading, mStatus));
        }
    }
}
