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

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.User;

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
    Long mGroupId;
    String mGroupNam;
    User mUser;
    String mHostname;
    String mUrl;
    private GroupDataCallback groupDataCallback;
    //endregion

    //region Constructor
    public AddGroup() {
        // Required empty public constructor
    }

    public static AddGroup newInstance(GroupDataCallback instance) {
        AddGroup addGroup = new AddGroup();
        addGroup.groupDataCallback = instance;
        return addGroup;
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
        mHostname = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Hostname", "");
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
                mGroupNam = String.valueOf(AddGroup.this.mGroupName.getText());
                mUrl = getResources().getString(R.string.image_url_group);
                newGroup.put("groupName", mGroupNam);
                newGroup.put("createdBy",mUserName);
                newGroup.put("url", mUrl);
                JSONObject jsonObject = new JSONObject(newGroup);
                Log.d(TAG, String.valueOf(jsonObject));
                groupDataCallback.updateGroupData(jsonObject, mGroupNam);
                AddGroup.this.dismiss();

            }
        });
    }


    //endregion

    //region Private Methods
    private void getUser() {

        Realm realm = Realm.getDefaultInstance();
        mUser = realm.where(User.class).equalTo("userId", mUserId).findFirst();
        mUserName = mUser.getName();

    }

    public interface GroupDataCallback {
        void updateGroupData(JSONObject jsonObject, String mGroupName);
    }
    //endregion
}
