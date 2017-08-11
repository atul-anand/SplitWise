package com.zemoso.atul.splitwise.activities;

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

public class SplashActivity extends AppCompatActivity{

    private static final String TAG = SplashActivity.class.getSimpleName();

    private EditText mEditText;
    private Button mButton;
    private Long mUserId;
    private String mFriendsUrl;
    private String mGroupsUrl;
    private Intent mIntent;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d(TAG,"Started");
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
                mFriendsUrl = getResources().getString(R.string.server_address)
                        +getResources().getString(R.string.server_users);
                mGroupsUrl = getResources().getString(R.string.server_address)
                        +getResources().getString(R.string.server_users);
                Log.d(TAG,mFriendsUrl);
                Log.d(TAG,mGroupsUrl);
                mIntent = new Intent(getApplicationContext(), LaunchDownloads.class);
                mBundle = new Bundle();
                mBundle.putLong("mUserId",mUserId);
                mBundle.putString("mFriendsUrl",mFriendsUrl);
                mBundle.putString("mGroupsUrl",mGroupsUrl);
                mIntent.putExtras(mBundle);
                startService(mIntent);
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
