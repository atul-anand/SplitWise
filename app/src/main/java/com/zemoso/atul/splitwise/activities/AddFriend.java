package com.zemoso.atul.splitwise.activities;

import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.RecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.Contact;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFriend extends AppCompatActivity {

    private static final String TAG = AddFriend.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private List<RecyclerViewHolder> mItems;

    EditText mFriendId;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        getSupportActionBar().setTitle(getResources().getString(R.string.friend_title));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


//        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
//        List<Contact> contacts = new ArrayList<>();
//
//        mItems = new ArrayList<>();
//        while (phones.moveToNext())
//        {
//            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////            Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
//            contacts.add(new Contact(name,phoneNumber));
//            mItems.add(new RecyclerViewHolder(1,"","",name,phoneNumber));
//            Log.d(TAG, (new Contact(name,phoneNumber)).toString());
//        }
//        phones.close();
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_add_friends);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerViewAdapter = new RecyclerViewAdapter(mItems,this);
//
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mFriendId = (EditText) findViewById(R.id.friend_id);
        mSubmit = (Button) findViewById(R.id.friend_submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map newFriend = new HashMap();
                JSONObject jsonObject = new JSONObject(newFriend);
//                VolleyRequests.getInstance(getApplicationContext()).save(jsonObject,2);
            }
        });
    }
}
