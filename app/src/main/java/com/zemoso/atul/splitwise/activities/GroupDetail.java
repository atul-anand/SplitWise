package com.zemoso.atul.splitwise.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.GroupDetailRecyclerViewAdapter;
import com.zemoso.atul.splitwise.fragments.AddMembers;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class GroupDetail extends AppCompatActivity {

    //region Variable Declaration
    private static final String TAG = GroupDetail.class.getSimpleName();

    //region Views
    private TextView mGroupName;
    private Button mSettleUp;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupDetailRecyclerViewAdapter mGroupDetailRecyclerViewAdapter;
    //endregion

    //region Data
    private List<String> mItems;
    private Long mGroupId;
    private String mGroupNam;
    private Group mGroup;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        //region Data
        Bundle mBundle = getIntent().getExtras();
        mGroupId = mBundle.getLong("groupId");
        mItems = new ArrayList<>();
        getGroupData();
        getGroup();
        //endregion

        //region Views
        mGroupName = (TextView) findViewById(R.id.group_detail_group_name);
        mSettleUp = (Button) findViewById(R.id.group_detail_settle_up);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_detail_recycler);
        //endregion

        //region Recycler View
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mGroupDetailRecyclerViewAdapter = new GroupDetailRecyclerViewAdapter(mItems, getApplicationContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mGroupDetailRecyclerViewAdapter);
        //endregion

        //region View Properties
        mGroupName.setText(mGroupNam);
        mSettleUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: Refresh mItems Data like Everyone owes nothing.
            }
        });
        //endregion
    }

    //region Private Methods
    private void getGroupData() {
        String extension = getResources().getString(R.string.url_group_findDataById);
        String param = getResources().getString(R.string.url_group_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mUrl = preferences.getString("Hostname", "") + extension + "?"
                + param + "=" + mGroupId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mItems.clear();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String data = jsonObject.toString();
                        mItems.add(data);
                        Log.d(TAG, String.valueOf(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                mGroupDetailRecyclerViewAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonArrayRequest transJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(transJsonObject);
    }


    private void getGroup() {
        String extension = getResources().getString(R.string.url_group_findById);
        String param = getResources().getString(R.string.url_group_id);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("Hostname", "") + extension + "?"
                + param + "=" + mGroupId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mGroup = new Group(response);
                mGroupNam = mGroup.getGroupName();
                Log.d(TAG, String.valueOf(response));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonObjectRequest transJsonObject = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(transJsonObject);
//
//
        Realm realm = Realm.getDefaultInstance();
        mGroup = realm.where(Group.class).equalTo("groupId", mGroupId).findFirst();
        mGroupNam = "Steve";
//        mGroupNam = mGroup.getGroupName();
        realm.close();
    }


    public void addMemberDialog(View view) {
        getSupportFragmentManager().beginTransaction().add(new AddMembers(), "Add Member").commit();
        getGroupData();
    }
    //endregion
}
