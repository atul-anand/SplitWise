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

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.modules.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;
import com.zemoso.atul.splitwise.utils.SplitWise;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGroup extends DialogFragment {

    private static final String TAG = AddGroup.class.getSimpleName();

    EditText mGroupName;
    String mUserName;
    Button mSubmit;

    public AddGroup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupName = view.findViewById(R.id.add_group_name);
        mSubmit = view.findViewById(R.id.add_group_create);
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("id", ((SplitWise)getActivity().getApplication()).getUserId()).findFirst();
//            mUserName = new JSONObject(user.getJSON()).getString("name");
        mUserName = "Arya Stark";
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
}
