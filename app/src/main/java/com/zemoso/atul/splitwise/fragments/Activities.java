package com.zemoso.atul.splitwise.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activities extends Fragment {

    private static final String TAG = Activities.class.getSimpleName();

    public Activities() {
        // Required empty public constructor
    }
    public static Activities newInstance(){
        return new Activities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

}
