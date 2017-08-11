package com.zemoso.atul.splitwise.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.services.LaunchDownloads;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;
import com.zemoso.atul.splitwise.utils.SplitWise;

public class SplashActivity extends AppCompatActivity{

    private static final String TAG = SplashActivity.class.getSimpleName();

    private EditText mEditText;
    private Button mButton;
    private Long mUserId;
    private String mFriendsUrl;
    private String mGroupsUrl;
    private Intent mIntent;
    private Bundle mBundle;
    private Context mContext;
    private VolleyRequests requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d(TAG,"Started");

        mContext = getApplication();
        requests = VolleyRequests.getInstance(mContext);
        requests.userFindAll();
        requests.userFindById(1);
        requests.groupFindAll();
        requests.groupFindById(2);
        requests.groupFindByUserId(1);
        requests.transactionFindAll();
        requests.transactionFindById(1);
        requests.transactionFindByUserId(1);
        requests.transactionFindByGroupId(2);
        requests.transactionFindGroupByUserId(1);
        requests.transactionFindNonGroupByUserId(1);

        mEditText = (EditText) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mUserId = Long.parseLong(String.valueOf(mEditText.getText()));
                } catch (Exception e) {
                    mUserId = 0L;
                }
                ((SplitWise)mContext).setUserId(mUserId);
                Toast.makeText(getApplicationContext(),"Application Starting for user #"+mUserId,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();

//        finish();
    }
}
