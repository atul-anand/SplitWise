package com.zemoso.atul.splitwise.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.AddBillRecyclerViewAdapter;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBill extends AppCompatActivity {

    static final int DIALOG_ID = 0;
    private static final String TAG  = AddBill.class.getSimpleName();
    int year, month, day;

    private ImageButton mCategory;
    private EditText mDescription;
    private EditText mValue;
    private Button mDate;
    private Button mAddButton;


    private List<Long> lender;
    private List<Long> borrower;
    private String mDesc;
    private Double mAmt;
    private String dot;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddBillRecyclerViewAdapter mAddBillRecyclerViewAdapter;

    private List<TransactionHolder> mItems;
    private DatePickerDialog.OnDateSetListener dpickerlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1;
            day = i2;
            dot = day + "/" + month + "/" + year;
            mDate.setText(dot);
            Toast.makeText(getApplicationContext(), year + month + day, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        getSupportActionBar().setTitle(getResources().getString(R.string.bill_title));

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        mCategory = (ImageButton) findViewById(R.id.bill_category);
        mDate = (Button) findViewById(R.id.bill_date);
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        month++;
        day = cal.get(Calendar.DAY_OF_MONTH);
        day++;
        dot = day + "/" + month + "/" + year;
        mDate.setText(dot);
//        mCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModeOfPayment modeOfPayment = new ModeOfPayment();
//                modeOfPayment.show(getSupportFragmentManager(),"Choose a category");
//            }
//        });

        showDatePickerDialog();

        mDescription = (EditText) findViewById(R.id.bill_description);
        mValue = (EditText) findViewById(R.id.bill_value);

        mItems = new ArrayList<>();
        lender = new ArrayList<>();
        borrower = new ArrayList<>();
        mItems.add(new TransactionHolder(-1L, "", 0.0));
        mRecyclerView = (RecyclerView) findViewById(R.id.add_bill_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mAddBillRecyclerViewAdapter = new AddBillRecyclerViewAdapter(mItems, getApplicationContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAddBillRecyclerViewAdapter);

        mAddButton = (Button) findViewById(R.id.add_member_button_add);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.add(new TransactionHolder(-1L, "", 0.0));
                mAddBillRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

    }

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
            case R.id.action_save:
                Map<String, Object> newTrans = new HashMap<>();

                getLendersAndBorrowers();
                mDesc = String.valueOf(mDescription.getText());
                Log.d(TAG, mDesc);

                mAmt = Double.parseDouble(String.valueOf(mValue.getText()));
                Log.d(TAG, String.valueOf(mAmt));
                newTrans.put("description", mDesc);
                newTrans.put("groupId", 21);
                newTrans.put("amount", mAmt);
                newTrans.put("mop", "Paytm");
                newTrans.put("lender", lender);
                newTrans.put("borrower", borrower);
                newTrans.put("dot", dot);
                JSONObject transaction = new JSONObject(newTrans);
                Log.d(TAG, String.valueOf(transaction));
                VolleyRequests.getInstance(getApplicationContext()).save(transaction, 3);
                Toast.makeText(this, "Transactions Added", Toast.LENGTH_SHORT).show();
                this.finish();
//                mDescription.setText("");
//                mValue.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLendersAndBorrowers() {
        for (TransactionHolder transactionHolder : mItems) {
            Long userId = transactionHolder.getUserId();
            String name = transactionHolder.getName();
            Double amt = transactionHolder.getAmount();
            if (amt < 0)
                borrower.add(userId);
            else
                lender.add(userId);
        }
    }

    public void showDatePickerDialog() {
//        DialogFragment newFragment = new TimePickerFragment();
//        getSupportFragmentManager().beginTransaction().add(newFragment,"timePicker").commit();
//        newFragment.show(getSupportFragmentManager(), "timePicker");

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerlistener, year, month, day);
        }
        return null;
    }
}
