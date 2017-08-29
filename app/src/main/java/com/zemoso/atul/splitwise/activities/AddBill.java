package com.zemoso.atul.splitwise.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.AddBillRecyclerViewAdapter;
import com.zemoso.atul.splitwise.adapters.SingleRecyclerViewAdapter;
import com.zemoso.atul.splitwise.fragments.MultiSelectionDialog;
import com.zemoso.atul.splitwise.fragments.Transactions;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.models.Transaction;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;
import com.zemoso.atul.splitwise.utils.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

public class AddBill extends AppCompatActivity implements MultiSelectionDialog.dataCallback {

    //region Variable Declaration

    private static final String TAG  = AddBill.class.getSimpleName();
    private static final int DIALOG_DATE = 0;

    //region Data
    private Long mUserId;
    private List<Group> mGroups;
    private List<String> mSelUserNames;
    private List<User> mUsers;
    private List<String> mSelGroupNames;
    private boolean[] lenderIndices;
    private boolean[] borrowerIndices;
    private int year, month, day;

    //endregion

    //region Views
    private EditText mDescription;
    private EditText mDate;

    private EditText mValue;
    private LinearLayout mLenderLayout;
    private LinearLayout mBorrowerLayout;

    //region Auto Complete Text View
    private Spinner mSelGroup;
    private ArrayAdapter<String> groupArrayAdapter;

    private Spinner mMode;
    private String[] modes;
    private ArrayAdapter<String> modeArrayAdapter;

    //endregion

    //region Recycler View
    private Button mAddLenderButton;
    private Button mAddBorrowerButton;
    private MultiSpinner mLenderSpinner;
    private MultiSpinner mBorrowerSpinner;

    private RecyclerView mRecyclerViewLender;
    private RecyclerView.LayoutManager mLayoutManagerLender;
    private RecyclerView mRecyclerViewBorrower;
    private RecyclerView.LayoutManager mLayoutManagerBorrower;

    private List<TransactionHolder> mItems;
    private AddBillRecyclerViewAdapter mAddBillRecyclerViewAdapter;
    private SingleRecyclerViewAdapter mSingleRecyclerViewAdapterLender;
    private SingleRecyclerViewAdapter mSingleRecyclerViewAdapterBorrower;
    //endregion

    private List<User> mUserLenders;
    private List<User> mUserBorrowers;


    private EditText mLenderText;
    private EditText mBorrowerText;
    private MultiSelectionDialog mLendersDialog;
    private MultiSelectionDialog mBorrowersDialog;

    //endregion

    //region Final Data
    private Long mGroupId;
    private List<Long> lender;
    private List<Long> borrower;
    private String mDesc;
    private String mDot;
    private String mMop;
    private Double mAmt;
    private String mUrl;

    //endregion

    //endregion
    //region Listeners
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
    //endregion
    private View.OnClickListener mLenderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLenderText.setText("");
            mLendersDialog.show(getSupportFragmentManager(), "Lenders");
        }
    };
    private View.OnClickListener mBorrowerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBorrowerText.setText("");
            mBorrowersDialog.show(getSupportFragmentManager(), "Borrowers");

        }
    };
    //endregion
    private AdapterView.OnItemSelectedListener selectGroupListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            String item = adapterView.getSelectedItem().toString();
            if (item.equals(getResources().getString(R.string.bill_select_group))) {
                mGroupId = -1L;
                mLenderLayout.setVisibility(View.GONE);
                mBorrowerLayout.setVisibility(View.GONE);
//                TODO: Find Users List
            } else {
                Realm realm = Realm.getDefaultInstance();
                mGroupId = realm.where(Group.class).equalTo("groupName", item).findFirst().getGroupId();
                getUsersByGroupId(mGroupId);
                mLenderLayout.setVisibility(View.VISIBLE);
                mBorrowerLayout.setVisibility(View.VISIBLE);
            }
            Log.d(TAG, String.valueOf(mGroupId));
            Log.d(TAG, item);

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

    //region Inherited Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        //region User Data
        mUserId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("userId", 0);
        //endregion

        //region Action Bar
        getSupportActionBar().setTitle(getResources().getString(R.string.bill_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //endregion

        //region View Lookup
        mLenderLayout = (LinearLayout) findViewById(R.id.lender_layout);
        mBorrowerLayout = (LinearLayout) findViewById(R.id.borrower_layout);
        mDescription = (EditText) findViewById(R.id.bill_description);
        mMode = (Spinner) findViewById(R.id.bill_value);
        mDate = (EditText) findViewById(R.id.bill_date);
        mSelGroup = (Spinner) findViewById(R.id.bill_select_group);
//        mRecyclerViewLender = (RecyclerView) findViewById(R.id.add_bill_recycler_lender);
//        mRecyclerViewBorrower = (RecyclerView) findViewById(R.id.add_bill_recycler_borrower);
//        mAddLenderButton = (Button) findViewById(R.id.add_member_button_add_lender);
//        mAddBorrowerButton = (Button) findViewById(R.id.add_member_button_add_borrower);
//        mLenderSpinner = (MultiSpinner) findViewById(R.id.bill_spinner_lender);
//        mBorrowerSpinner = (MultiSpinner) findViewById(R.id.bill_spinner_borrower);
        mValue = (EditText) findViewById(R.id.bill_single_value_amt);

        mLenderText = (EditText) findViewById(R.id.lb_select_lenders);
        mBorrowerText = (EditText) findViewById(R.id.lb_select_borrowers);

        mLendersDialog = MultiSelectionDialog.newInstance(this);
        mBorrowersDialog = MultiSelectionDialog.newInstance(this);
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
        mUsers = new ArrayList<>();
        mUserLenders = new ArrayList<>();
        mUserBorrowers = new ArrayList<>();

        mItems = new ArrayList<>();
        lender = new ArrayList<>();
        borrower = new ArrayList<>();
//        mGroups = new ArrayList<>();
        getGroupsByUserId();
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
//        mSelGroupNames.add("Non Group Expenses");
        //endregion


        //region Recycler View Attributes
//        mLayoutManagerLender = new LinearLayoutManager(this);
//        mRecyclerViewLender.setHasFixedSize(true);
//        mRecyclerViewLender.setLayoutManager(mLayoutManagerLender);
//        mRecyclerViewLender.setItemAnimator(new DefaultItemAnimator());
//
//        mLayoutManagerBorrower = new LinearLayoutManager(this);
//        mRecyclerViewBorrower.setHasFixedSize(true);
//        mRecyclerViewBorrower.setLayoutManager(mLayoutManagerBorrower);
//        mRecyclerViewBorrower.setItemAnimator(new DefaultItemAnimator());
        //endregion

        //region Attach Adapters
        groupArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.card_autocomplete_item, mSelGroupNames);
        mSelGroup.setAdapter(groupArrayAdapter);

        modes = getResources().getStringArray(R.array.bill_mop_list);
        modeArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.card_autocomplete_item, modes);
        mMode.setAdapter(modeArrayAdapter);


//        mLenderSpinner.setItems(mSelUserNames,"Lenders: ",lenderListener);
//        mBorrowerSpinner.setItems(mSelUserNames,"Borrowers: ",borrowerListener);


//        mSingleRecyclerViewAdapterLender = new SingleRecyclerViewAdapter(mUserLenders, mUsers, this, mSelUserNames);
//        mRecyclerViewLender.setAdapter(mSingleRecyclerViewAdapterLender);
//        mSingleRecyclerViewAdapterBorrower = new SingleRecyclerViewAdapter(mUserBorrowers, mUsers, this, mSelUserNames);
//        mRecyclerViewBorrower.setAdapter(mSingleRecyclerViewAdapterBorrower);

//        mAddBillRecyclerViewAdapter = new AddBillRecyclerViewAdapter(mItems, this);
//        mRecyclerView.setAdapter(mAddBillRecyclerViewAdapter);
        //endregion

        //region Attach Listeners
        mDate.setOnClickListener(selectDateListener);
//        mAddLenderButton.setOnClickListener(addMemberLenderListener);
//        mAddBorrowerButton.setOnClickListener(addMemberBorrowerListener);
        mSelGroup.setOnItemSelectedListener(selectGroupListener);
        //endregion
    }

    //region Menu Items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_add_bill, menu);
        return true;
    }
    //endregion

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
                mUrl = getResources().getString(R.string.image_url_trans);
                Log.d(TAG, String.valueOf(mAmt));
                newTrans.put("description", mDesc);
                newTrans.put("groupId", mGroupId);
                newTrans.put("amount", mAmt);
                newTrans.put("mop", mMop);
                newTrans.put("lender", lender);
                newTrans.put("borrower", borrower);
                newTrans.put("dot", mDot);
                newTrans.put("url", mUrl);
                JSONObject transaction = new JSONObject(newTrans);
                Log.d(TAG, String.valueOf(transaction));
                save(transaction);
                Toast.makeText(this, "Transactions Added", Toast.LENGTH_SHORT).show();
                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Item Listener

    //region Date Lookup
    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_DATE) {
            return new DatePickerDialog(this, dpickerlistener, year, month, day);
        }
        return null;
    }

    //region Volley Requests
    public void save(JSONObject jsonObject) {
        String mHostName = getResources().getString(R.string.url_address);
        final String tag = getResources().getString(R.string.url_transaction_save);
        final String mUrl = mHostName + tag;
        Log.d(TAG, mUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, mUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, String.valueOf(response));
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        Transaction transaction = new Transaction(response);
                        realm.insertOrUpdate(transaction);
                        realm.commitTransaction();
                        realm.close();
                        Transactions.getInstance().updateTransactionData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
                Toast.makeText(getApplicationContext(), "Data not saved", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getUsersByGroupId(Long groupId) {
        String extension = getResources().getString(R.string.url_group_findAllUsersByGroupId);
        String param = getResources().getString(R.string.url_group_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String mUrl = preferences.getString("Hostname", "") + extension + "?"
                + param + "=" + groupId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                mUsers.clear();
//                Realm realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.d(TAG, String.valueOf(jsonObject));
                        User user = new User(jsonObject);
                        Log.d(TAG, user.toString());
                        mUsers.add(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                mLendersDialog.init(getResources().getString(R.string.lender_heading), mUsers, mUserLenders, false);
                mBorrowersDialog.init(getResources().getString(R.string.borrower_heading), mUsers, mUserBorrowers, true);

                mLenderText.setOnClickListener(mLenderListener);
                mBorrowerText.setOnClickListener(mBorrowerListener);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
//                Toast.makeText(getApplicationContext(),"No user for group #" + mGroupId,Toast.LENGTH_SHORT).show();

            }
        };
        JsonArrayRequest usersFromGroupRequest = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(usersFromGroupRequest);
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
                mSelGroupNames.clear();
                mSelGroupNames.add(getResources().getString(R.string.bill_select_group));
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Group group = new Group(jsonObject);
                        Log.d(TAG, String.valueOf(group));
                        mSelGroupNames.add(group.getGroupName());
                        Log.d(TAG, String.valueOf(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                groupArrayAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
//                Toast.makeText(getApplicationContext(),"No groups available",Toast.LENGTH_SHORT).show();

            }
        };
        JsonArrayRequest userJsonObject = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(userJsonObject);
    }
    //endregion

    //region Private Methods
    private void getLendersAndBorrowers() {
//
        Log.d(TAG, "Lenders");
        for (User user : mUserLenders) {
            Long userId = user.getUserId();
            Log.d(TAG, String.valueOf(userId));
            lender.add(userId);
        }
        Log.d(TAG, "Borrowers");
        for (User user : mUserBorrowers) {
            Long userId = user.getUserId();
            Log.d(TAG, String.valueOf(userId));
            borrower.add(userId);
        }

    }

    @Override
    public void getSelectedData(List<User> mUserLBs, Boolean isBorrower) {
        String heading = "";
        if (mUserLBs.size() == 0)
            if (isBorrower)
                heading = getResources().getString(R.string.borrower_heading);
            else
                heading = getResources().getString(R.string.lender_heading);


        for (User user : mUserLBs) {
            String userName = user.getName();
            heading += userName + ", ";
        }
        if (mUserLBs.size() != 0)
            heading = heading.substring(0, heading.lastIndexOf(','));


        if (isBorrower) {
            mBorrowerText.setText(heading);
            mBorrowersDialog.notifyDataSetChanged();
        } else {
            mLenderText.setText(heading);
            mLendersDialog.notifyDataSetChanged();
        }

    }
    //endregion


}
