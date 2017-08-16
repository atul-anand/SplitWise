package com.zemoso.atul.splitwise.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.fragments.BillCategory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddBill extends AppCompatActivity {

    private static final String TAG  = AddBill.class.getSimpleName();

    private ImageButton mCategory;
    private EditText mDescription;
    private EditText mValue;
    private Button mCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        getSupportActionBar().setTitle(getResources().getString(R.string.bill_title));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mCategory = (ImageButton) findViewById(R.id.bill_category);
        mCurrency = (Button) findViewById(R.id.bill_currency);

        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillCategory billCategory = new BillCategory();
                billCategory.show(getSupportFragmentManager(),"Choose a category");
            }
        });

        mDescription = (EditText) findViewById(R.id.bill_description);
        mValue = (EditText) findViewById(R.id.bill_value);
        mCurrency = (Button) findViewById(R.id.bill_currency);


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
                Map newTrans = new HashMap();
                newTrans.put("description",mDescription.getText());
                newTrans.put("amount",mValue.getText());
                newTrans.put("mop","Cash");
                newTrans.put("lender","");
                newTrans.put("borrower","");
                JSONObject transaction = new JSONObject(newTrans);
//                VolleyRequests.getInstance(getApplicationContext()).save(transaction,3);
                Toast.makeText(this, "Transactions Added", Toast.LENGTH_SHORT).show();
                mDescription.setText("");
                mValue.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
