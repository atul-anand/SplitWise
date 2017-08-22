package com.zemoso.atul.splitwise.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.AddBillRecyclerViewAdapter;
import com.zemoso.atul.splitwise.adapters.SingleRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

public class AddBill extends AppCompatActivity {

    //region Variable Declaration

    private static final String TAG  = AddBill.class.getSimpleName();
    private static final int DIALOG_DATE = 0;

    //region Data
    private Long mUserId;
    private List<Group> mGroups;
    private List<String> mSelUserNames;
    private List<User> mUsers;
    private List<String> mSelGroupNames;
    private int year, month, day;
    //endregion

    //region Views
    private EditText mDescription;
    private Button mDate;
    private Spinner mMode;
    private EditText mValue;

    //region Auto Complete Text View
    private AutoCompleteTextView mSelGroup;
    private ArrayAdapter<String> groupArrayAdapter;
    //endregion

    private Button mAddLenderButton;
    private Button mAddBorrowerButton;

    //region Recycler View
    private RecyclerView mRecyclerViewLender;
    private RecyclerView.LayoutManager mLayoutManagerLender;
    private RecyclerView mRecyclerViewBorrower;
    private RecyclerView.LayoutManager mLayoutManagerBorrower;

    private List<TransactionHolder> mItems;
    private AddBillRecyclerViewAdapter mAddBillRecyclerViewAdapter;

    private List<User> mUserLenders;
    private List<User> mUserBorrowers;
    private SingleRecyclerViewAdapter mSingleRecyclerViewAdapterLender;
    private SingleRecyclerViewAdapter mSingleRecyclerViewAdapterBorrower;
    //endregion
    //endregion

    //region Final Data
    private Long mGroupId;
    private List<Long> lender;
    private List<Long> borrower;
    private String mDesc;
    private String mDot;
    private String mMop;
    private Double mAmt;
    //endregion

    //endregion
    private DatePickerDialog.OnDateSetListener dpickerlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1;
            day = i2;
            mDot = day + "/" + month + "/" + year;
            mDate.setText(mDot);
//            Toast.makeText(getApplicationContext(), year + month + day, Toast.LENGTH_SHORT).show();
        }
    };
    //region Item Listener
    private View.OnClickListener addMemberLenderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//                mItems.add(new TransactionHolder(-1L, "", 0.0));
//                mAddBillRecyclerViewAdapter.notifyDataSetChanged();
            mUserLenders.add(null);
            mSingleRecyclerViewAdapterLender.notifyDataSetChanged();
        }
    };
    private View.OnClickListener addMemberBorrowerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//                mItems.add(new TransactionHolder(-1L, "", 0.0));
//                mAddBillRecyclerViewAdapter.notifyDataSetChanged();
            mUserBorrowers.add(null);
            mSingleRecyclerViewAdapterBorrower.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemSelectedListener selectGroupListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            String item = adapterView.getSelectedItem().toString();
            if (item.equals("Non Group Expenses")) {
                mGroupId = -1L;


            } else {
                Realm realm = Realm.getDefaultInstance();
                try {
                    mGroupId = realm.where(Group.class).equalTo("groupName", item).findFirst().getGroupId();
                } catch (Exception e) {
                    mGroupId = -1L;
                }
                getUsersByGroupId();

            }

            mRecyclerViewLender.setVisibility(View.VISIBLE);
            mRecyclerViewBorrower.setVisibility(View.VISIBLE);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    //endregion
    private View.OnClickListener selectDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialog(DIALOG_DATE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        //region User Data
        mUserId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("userId", 0);
        mGroupId = -1L;
        //endregion

        //region Action Bar
        getSupportActionBar().setTitle(getResources().getString(R.string.bill_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //endregion

        //region View Lookup
        mDescription = (EditText) findViewById(R.id.bill_description);
        mMode = (Spinner) findViewById(R.id.bill_value);
        mDate = (Button) findViewById(R.id.bill_date);
        mSelGroup = (AutoCompleteTextView) findViewById(R.id.bill_select_group);
        mRecyclerViewLender = (RecyclerView) findViewById(R.id.add_bill_recycler_lender);
        mRecyclerViewBorrower = (RecyclerView) findViewById(R.id.add_bill_recycler_borrower);
        mAddLenderButton = (Button) findViewById(R.id.add_member_button_add_lender);
        mAddBorrowerButton = (Button) findViewById(R.id.add_member_button_add_borrower);
        mValue = (EditText) findViewById(R.id.bill_single_value_amt);
        //endregion

        //region Date Initialization
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        month++;
        day = cal.get(Calendar.DAY_OF_MONTH);
        day++;
        mDot = day + "/" + month + "/" + year;
        mDate.setText(mDot);
        //endregion

        //region Collections Initialization
        mSelGroupNames = new ArrayList<>();
        mSelUserNames = new ArrayList<>();
        mUsers = new ArrayList<>();
        mUserLenders = new ArrayList<>();
        mUserBorrowers = new ArrayList<>();

        mItems = new ArrayList<>();
        lender = new ArrayList<>();
        borrower = new ArrayList<>();
        mGroups = new ArrayList<>();
        //endregion

        //region Fake Data Addition
//        mItems.add(new TransactionHolder());
//
//        mUsers.add(new User());
//        mUserLenders.add(new User());
//
//        mGroups.add(new Group());
//
//        Log.d(TAG, String.valueOf(mUsers.size()));
//
//        mSelGroupNames.add("DSA");
//        mSelGroupNames.add("FUN");
//
//        mSelUserNames.add("INDIA");
//        mSelUserNames.add("PAK");

        mSelGroupNames.add("Non Group Expenses");
        //endregion

        //region Recycler View Attributes
        mLayoutManagerLender = new LinearLayoutManager(this);
        mRecyclerViewLender.setHasFixedSize(true);
        mRecyclerViewLender.setLayoutManager(mLayoutManagerLender);
        mRecyclerViewLender.setItemAnimator(new DefaultItemAnimator());

        mLayoutManagerBorrower = new LinearLayoutManager(this);
        mRecyclerViewBorrower.setHasFixedSize(true);
        mRecyclerViewBorrower.setLayoutManager(mLayoutManagerBorrower);
        mRecyclerViewBorrower.setItemAnimator(new DefaultItemAnimator());
        //endregion

        //region Attach Adapters
        groupArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.card_autocomplete_item, mSelGroupNames);
        mSelGroup.setAdapter(groupArrayAdapter);
        mSelGroup.setThreshold(0);

        mSingleRecyclerViewAdapterLender = new SingleRecyclerViewAdapter(mUserLenders, this, mSelUserNames);
        mRecyclerViewLender.setAdapter(mSingleRecyclerViewAdapterLender);
        mSingleRecyclerViewAdapterBorrower = new SingleRecyclerViewAdapter(mUserBorrowers, this, mSelUserNames);
        mRecyclerViewBorrower.setAdapter(mSingleRecyclerViewAdapterBorrower);

//        mAddBillRecyclerViewAdapter = new AddBillRecyclerViewAdapter(mItems, this);
//        mRecyclerView.setAdapter(mAddBillRecyclerViewAdapter);
        //endregion

        //region Attach Listeners
        mDate.setOnClickListener(selectDateListener);
        mAddLenderButton.setOnClickListener(addMemberLenderListener);
        mAddBorrowerButton.setOnClickListener(addMemberBorrowerListener);
        mSelGroup.setOnItemSelectedListener(selectGroupListener);
        //endregion
    }
    //endregion

    //region Menu Items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_add_bill, menu);
        return true;
    }

    //Action Bar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                Map<String, Object> newTrans = new HashMap<>();

                getLendersAndBorrowers();
                mDesc = String.valueOf(mDescription.getText());
                Log.d(TAG, mDesc);
                mMop = (String) mMode.getSelectedItem();
//                mAmt = 0.0;
//                for(TransactionHolder tH : mItems)
//                    mAmt+=tH.getAmount();
                mAmt = Double.parseDouble(String.valueOf(mValue.getText()));
                Log.d(TAG, String.valueOf(mAmt));
                newTrans.put("description", mDesc);
                newTrans.put("groupId", mGroupId);
                newTrans.put("amount", mAmt);
                newTrans.put("mop", mMop);
                newTrans.put("lender", lender);
                newTrans.put("borrower", borrower);
                newTrans.put("dot", mDot);
                JSONObject transaction = new JSONObject(newTrans);
                Log.d(TAG, String.valueOf(transaction));
                VolleyRequests.getInstance(getApplicationContext()).save(transaction, 3);
                Toast.makeText(this, "Transactions Added", Toast.LENGTH_SHORT).show();
                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //region Date Lookup
    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_DATE) {
            return new DatePickerDialog(this, dpickerlistener, year, month, day);
        }
        return null;
    }
    //endregion

    //region private Volley Requests
    private void getUsersByGroupId() {
        String extension = getResources().getString(R.string.url_group_findAllUsersByGroupId);
        String param = getResources().getString(R.string.url_group_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mUrl = preferences.getString("Hostname", "") + extension + "?"
                + param + "=" + mGroupId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject);
                        String userName = user.getName();
                        mUsers.add(user);
                        mSelUserNames.add(userName);
//                        TransactionHolder transactionHolder = new TransactionHolder(jsonObject);
//                        mItems.add(transactionHolder);
                        realm.insertOrUpdate(new User(jsonObject));
                        Log.d(TAG, String.valueOf(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                realm.commitTransaction();
                realm.close();
                mSingleRecyclerViewAdapterLender.notifyDataSetChanged();
                mSingleRecyclerViewAdapterBorrower.notifyDataSetChanged();
//                mAddBillRecyclerViewAdapter.notifyDataSetChanged() ;
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

    private void getGroupsByUserId() {
        String extension = getResources().getString(R.string.url_group_findByUserId);
        String param = getResources().getString(R.string.url_user_id);
        String mUrl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Hostname", "") + extension
                + "?" + param + "=" + mUserId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Group group = new Group(jsonObject);
                        mGroups.add(group);
                        realm.insertOrUpdate(group);
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                realm.commitTransaction();
                realm.close();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

            }
        };
        JsonArrayRequest userJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(userJsonObject);
    }
    //endregion

    //region Private Methods
    private void getLendersAndBorrowers() {
//        for (TransactionHolder transactionHolder : mItems) {
//            Long userId = transactionHolder.getUserId();
//            String name = transactionHolder.getName();
//            Double amt = transactionHolder.getAmount();
//            if (amt <= 0)
//                borrower.add(userId);
//            else
//                lender.add(userId);
//
//        }


        for (User user : mUserLenders) {
            try {
                lender.add(user.getUserId());
            } catch (Exception e) {
                lender.add(-1L);
            }
        }
        for (User user : mUserBorrowers) {
            try {
                borrower.add(user.getUserId());
            } catch (Exception e) {
                borrower.add(-1L);
            }
        }
    }
    //endregion
}
