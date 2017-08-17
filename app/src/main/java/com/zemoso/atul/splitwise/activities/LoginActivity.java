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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private AutoCompleteTextView mAutoCompleteTextView;
    //    private Spinner mSpinner;
    private Button mButton;
    private Long mUserId;
    private Context mContext;
//    private VolleyRequests requests;

    private List<User> mUsers;
    private List<String> userName;
    private ProgressBar mProgressBar;
    private ArrayAdapter<String> arrayAdapter;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContext = getApplication();
        Log.d(TAG, "Started");

        mContext = getApplication();

        editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("Hostname", getResources().getString(R.string.url_address));
        editor.apply();

//        mSpinner = (Spinner) findViewById(R.id.spinner);
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.button);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mUsers = new ArrayList<>();
        userName = new ArrayList<>();

        userString("");
        findDataSet();


        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, userName);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAutoCompleteTextView.setAdapter(arrayAdapter);
        mAutoCompleteTextView.setThreshold(0);

//
//        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String mText = String.valueOf(mAutoCompleteTextView.getText());
//                userString(mText);
//                findDataSet();
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int pos = userName.indexOf(mAutoCompleteTextView.getText().toString());
//                mUserId = mUsers.get(pos).getUserId();
                mUserId = Long.valueOf(mAutoCompleteTextView.getText().toString());
                Log.d(TAG, mUserId + "111");
                editor.putLong("userId", mUserId);

                editor.apply();
                Toast.makeText(getApplicationContext(), "Application Starting for user #" + mUserId, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

    }

    private void userString(String param) {
        final String extension = mContext.getResources().getString(R.string.url_user_string);
        String mUrl = getSharedPreferences("Settings", 0).getString("Hostname", "") + extension + "?s=" + param;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mUsers = new ArrayList<User>();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject);
                        mUsers.add(user);
                        Log.d(TAG, String.valueOf(jsonObject));
//                        Log.d(TAG, String.valueOf(userName.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                mProgressBar.setVisibility(View.GONE);
                arrayAdapter.notifyDataSetChanged();

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                mProgressBar.setVisibility(View.GONE);
            }
        };
        JsonArrayRequest userJsonArray = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(userJsonArray);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void findDataSet() {
        userName.clear();
        for (User user : mUsers)
            userName.add(user.getName());
    }
}
