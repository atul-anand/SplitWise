package com.zemoso.atul.splitwise.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.javaBeans.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillCategory extends DialogFragment {


    public BillCategory() {
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

    private Category getDropDownMenu(){
        Category games = new Category("Games","",null);
        Category movies = new Category("Movies","",null);
        Category music = new Category("Music","",null);
        Category other = new Category("Other","",null);
        Category sports = new Category("Sports","",null);
        List<Category> entertainmentList = new ArrayList<Category>();
        entertainmentList.add(games);
        entertainmentList.add(movies);
        entertainmentList.add(music);
        entertainmentList.add(other);
        entertainmentList.add(sports);
        Category entertainment = new Category("Entertainment","",entertainmentList);
        Category dining = new Category("Dining","",null);
        Category groceries = new Category("Groceries","",null);
        Category liquor = new Category("Liquor","",null);
        Category other2 = new Category("Other","",null);
        List<Category> foodList = new ArrayList<>();
        foodList.add(dining);
        foodList.add(groceries);
        foodList.add(liquor);
        foodList.add(other2);
        Category foodAndDrink = new Category("Food and Drink","",foodList);
        List<Category> totalMenu = new ArrayList<>();
        totalMenu.add(entertainment);
        totalMenu.add(foodAndDrink);
        return new Category("Choose a category","",totalMenu);
    }
}
