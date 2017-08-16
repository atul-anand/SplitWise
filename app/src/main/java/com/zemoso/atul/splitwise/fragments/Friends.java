package com.zemoso.atul.splitwise.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.zemoso.atul.splitwise.activities.AddFriend;
import com.zemoso.atul.splitwise.adapters.RecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
import com.zemoso.atul.splitwise.modules.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;


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
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private List<RecyclerViewHolder> mItems;

    public Friends() {
        // Required empty public constructor
    }
    public static Friends newInstance(){
        return new Friends();
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

        mButton = (Button) view.findViewById(R.id.total_menu);
        mPopupMenu = new PopupMenu(getContext(),mButton);

        mRecyclerView = view.findViewById(R.id.recycler_friends);
        mLayoutManager = new LinearLayoutManager(getContext());
        mItems = new ArrayList<>();
        mRecyclerViewAdapter = new RecyclerViewAdapter(mItems,getContext());

        mAddButton = view.findViewById(R.id.addFriends);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> mUserData = realm.where(User.class).findAll();
        for(User user : mUserData){
            String data = user.getJSON();
            Log.d(TAG,data);
            JSONObject jsonObject;
            int mId = -1;
            String mImageUrl="";
            String mHeading="";
            String mStatus="";
            try {
                jsonObject = new JSONObject(data);

                mId = (int) jsonObject.get("transID");
//                mImageUrl = (String) jsonObject.get("imageUrl");
                mImageUrl = getResources().getString(R.string.image_url);
                mHeading = (String) jsonObject.get("description");
                mStatus = String.valueOf(jsonObject.get("amount"));
                mItems.add(new RecyclerViewHolder(mId,mImageUrl,"",mHeading,mStatus));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, String.valueOf(mItems.size()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

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
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 0);
//                startActivity(new Intent(getContext(), AddFriend.class));
                getActivity().getSupportFragmentManager().beginTransaction().add(new AddUser(),"Add User").commit();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            int i;
            for(i=0;i<permissions.length;i++) {
                if (Objects.equals(permissions[i], Manifest.permission.WRITE_CONTACTS))
                    break;
            }
            Log.d(TAG,"Permissions");
            if(grantResults[i]==PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(getContext(), AddFriend.class));
            else
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 0);
        }
    }
}
