package com.zemoso.atul.splitwise.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.modules.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity{

    private static final String TAG = SplashActivity.class.getSimpleName();

    private Spinner mSpinner;
    private Button mButton;
    private Long mUserId;
    private Context mContext;
    private VolleyRequests requests;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d(TAG,"Started");

        mContext = getApplication();

        editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("Hostname", getResources().getString(R.string.url_address));
        editor.apply();
        requests = VolleyRequests.getInstance(mContext);


//        requests.userFindAll();
//        requests.userFindById(1);
//        requests.groupFindAll();
//        requests.groupFindById(2);
//        requests.groupFindByUserId(1);
//        requests.transactionFindAll();
//        requests.transactionFindById(1);
//        requests.transactionFindByUserId(1);
//        requests.transactionFindByGroupId(2);
//        requests.transactionFindGroupByUserId(1);
//        requests.transactionFindNonGroupByUserId(1);


        mSpinner = (Spinner) findViewById(R.id.editText);
        requests.userFindAll();

        List<String> userName = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<User> users = realm.where(User.class).findAll();
        for (User user : users) {
            try {
                JSONObject jsonObject = new JSONObject(user.getJSON());
                userName.add((String) jsonObject.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setSelection(0);
        mButton = (Button) findViewById(R.id.button);
        final List<User> finalUsers = users;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = mSpinner.getSelectedItemPosition();
                mUserId = finalUsers.get(pos).getId();

                editor.putLong("UserId", mUserId);

                editor.apply();
                Toast.makeText(getApplicationContext(),"Application Starting for user #"+mUserId,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();

    }

}
