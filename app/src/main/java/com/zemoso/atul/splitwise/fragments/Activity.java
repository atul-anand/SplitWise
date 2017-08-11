package com.zemoso.atul.splitwise.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zemoso.atul.splitwise.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity extends Fragment {

    private static final String TAG = Activity.class.getSimpleName();

    public Activity() {
        // Required empty public constructor
    }
    public static Activity newInstance(){
        return new Activity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

}
