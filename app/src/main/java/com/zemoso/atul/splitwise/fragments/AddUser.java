package com.zemoso.atul.splitwise.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUser extends DialogFragment {

    //region Variable Declaration
    private static final String TAG = AddUser.class.getSimpleName();

    EditText mUser;
    EditText mEmail;
    EditText mPhone;
    EditText mAge;
    Button mSubmit;

    String mUserName;
    String mEmailId;
    String mPhoneNumber;
    int mAges;
    String mUrl;

    private FriendDataCallback friendDataCallback;
    //endregion

    //region Constructor
    public AddUser() {
        // Required empty public constructor
    }

    public static AddUser newInstance() {
        return new AddUser();
    }

    public static AddUser newInstance(FriendDataCallback instance) {
        AddUser addUser = new AddUser();
        addUser.friendDataCallback = instance;
        return addUser;
    }
    //endregion


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = view.findViewById(R.id.user_name);
        mEmail = view.findViewById(R.id.user_email);
        mPhone = view.findViewById(R.id.user_phone);
        mAge = view.findViewById(R.id.user_age);
        mSubmit = view.findViewById(R.id.user_submit);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName = String.valueOf(mUser.getText());
                mEmailId = String.valueOf(mEmail.getText());
                mPhoneNumber = String.valueOf(mPhone.getText());
                mAges = Integer.parseInt(String.valueOf(mAge.getText()));
                mUrl = getResources().getString(R.string.image_url_user);
                Log.d(TAG,mUserName);
                Log.d(TAG,mEmailId);
                Log.d(TAG,mPhoneNumber);
                Log.d(TAG, String.valueOf(mAges));
                Map<String, Object> newUser = new HashMap<>();
                newUser.put("name",mUserName);
                newUser.put("emailId",mEmailId);
                newUser.put("phoneNumber",mPhoneNumber);
                newUser.put("age",mAges);
                newUser.put("url", mUrl);
                JSONObject jsonObject = new JSONObject(newUser);
                Log.d(TAG, String.valueOf(jsonObject));
                friendDataCallback.updateFriendData(jsonObject);
                Toast.makeText(getContext(), "User " + mUserName + " registered\n Use this username for login", Toast.LENGTH_SHORT).show();
                AddUser.this.dismiss();
            }
        });
    }

    public interface FriendDataCallback {
        void updateFriendData(JSONObject jsonObject);
    }
}
