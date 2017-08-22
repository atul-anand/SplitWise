package com.zemoso.atul.splitwise.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGroup extends DialogFragment {

    //region Variable Declaration
    private static final String TAG = AddGroup.class.getSimpleName();

    EditText mGroupName;
    String mUserName;
    Button mSubmit;
    Long mUserId;
    User mUser;
    //endregion

    //region Constructor
    public AddGroup() {
        // Required empty public constructor
    }

    public static AddGroup newInstance() {
        return new AddGroup();
    }
    //endregion

    //region Inherited Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mUserId = preferences.getLong("userId", 0);
        } catch (NumberFormatException e) {
            mUserId = -1L;
        }
        getUser();

        mGroupName = view.findViewById(R.id.add_group_name);
        mSubmit = view.findViewById(R.id.add_group_create);
//        Realm realm = Realm.getDefaultInstance();
//        User user = realm.where(User.class).equalTo("id", ((SplitWise)getActivity().getApplication()).getUserId()).findFirst();
////            mUserName = new JSONObject(user.getJSON()).getString("name");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map newGroup = new HashMap();
                String name = String.valueOf(mGroupName.getText());
                newGroup.put("groupName", name);
                newGroup.put("createdBy",mUserName);
                JSONObject jsonObject = new JSONObject(newGroup);
                Log.d(TAG, String.valueOf(jsonObject));
                VolleyRequests.getInstance(getContext()).save(jsonObject,2);
                AddGroup.this.dismiss();
            }
        });
    }


    //endregion

    //region Private Methods
    private void getUser() {
        String extension = getResources().getString(R.string.url_user_findById);
        String param = getResources().getString(R.string.url_user_id);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "") + extension + "?"
                + param + "=" + mUserId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mUser = new User(response);
                mUserName = mUser.getName();
                Log.d(TAG, String.valueOf(response));
                Realm realm = Realm.getDefaultInstance();
                mUser = realm.where(User.class).equalTo("userId", mUserId).findFirst();
                mUserName = mUser.getName();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonObjectRequest transJsonObject = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(getContext()).addToRequestQueue(transJsonObject);

    }
    //endregion
}
