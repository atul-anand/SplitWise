package com.zemoso.atul.splitwise.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.DialogRecyclerViewAdapter;
import com.zemoso.atul.splitwise.models.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiSelectionDialog extends DialogFragment {

    private String heading;
    private List<User> mItems;
    private List<User> mSelected;
    private Boolean isBorrower;

    private TextView mHeading;
    private Button mSubmit;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DialogRecyclerViewAdapter mDialogRecyclerViewAdapter;
    private dataCallback dataCallbackImpl;

    public MultiSelectionDialog() {
        // Required empty public constructor
    }

    public static MultiSelectionDialog newInstance(dataCallback instance) {
        MultiSelectionDialog newInst = new MultiSelectionDialog();
        newInst.dataCallbackImpl = instance;
        return newInst;
    }

    public void init(String heading, List<User> mItems, List<User> mSelected, Boolean isBorrower) {
        this.heading = heading;
        this.mItems = mItems;
        this.mSelected = mSelected;
        this.isBorrower = isBorrower;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_multi_selection_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeading = view.findViewById(R.id.lb_heading);
        mSubmit = view.findViewById(R.id.lb_submit);

        mRecyclerView = view.findViewById(R.id.lb_recycler);
        mLayoutManager = new LinearLayoutManager(getContext());
        mDialogRecyclerViewAdapter = new DialogRecyclerViewAdapter(getContext(), mItems, mSelected);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHeading.setText(heading);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mDialogRecyclerViewAdapter);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCallbackImpl.getSelectedData(mSelected, isBorrower);
                MultiSelectionDialog.this.dismiss();
            }
        });
    }

    public void notifyDataSetChanged() {
        mDialogRecyclerViewAdapter.notifyDataSetChanged();
    }

    public interface dataCallback {
        void getSelectedData(List<User> mUserLBs, Boolean isBorrower);
    }

}
