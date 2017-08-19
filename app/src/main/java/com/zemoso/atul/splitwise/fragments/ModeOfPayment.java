package com.zemoso.atul.splitwise.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zemoso.atul.splitwise.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModeOfPayment extends DialogFragment {

    List<String> totalMenu;

    public ModeOfPayment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_category, container, false);
        getDialog().setTitle("Choose a category");
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private List<String> getDropDownMenu() {
        totalMenu = new ArrayList<>();
        totalMenu.add("Cash");
        totalMenu.add("Debit Card");
        totalMenu.add("Credit Card");
        totalMenu.add("Paytm");
        totalMenu.add("Net Banking");
        return totalMenu;
    }
}
