package com.zemoso.atul.splitwise.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.fragments.AddUser;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.models.Transaction;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    //region Variable Declaration
    private static final String TAG = LoginActivity.class.getSimpleName();

    //region System
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    //endregion

    //region Views
    private LinearLayout mLoginLayout;
    private EditText mEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;
    //endregion

    //region Booleans
    private Boolean mUsersDownloaded;
    private Boolean mGroupsDownloaded;
    private Boolean mTransactionsDownloaded;
    //endregion
    //region Data
    private Long mUserId;
    private User mUser;
    private String mUserName;
    //endregion

    //endregion

    //region Listeners
    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getSupportFragmentManager().beginTransaction().add(AddUser.newInstance(), "Add User").commit();
        }
    };

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mUserName = String.valueOf(mEditText.getText());
            Log.d(TAG, mUserName);
            checkUserNamePresent();
        }
    };
    //endregion

    //region Inherited Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region System Data
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editor.putString("Hostname", getResources().getString(R.string.url_address));
        editor.apply();
        //endregion

        //region Views
        mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEditText = (EditText) findViewById(R.id.auto_complete);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mRegisterButton = (Button) findViewById(R.id.button_register);
        //endregion

        //region Data

        mUsersDownloaded = false;
        mGroupsDownloaded = false;
        mTransactionsDownloaded = false;
        Log.d(TAG, "Started");
        //endregion

        //region Attach Listeners
        mLoginButton.setOnClickListener(loginListener);
        mRegisterButton.setOnClickListener(registerListener);
        //endregion

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Private Methods
    private void checkUserNamePresent() {

        String mUrl = preferences.getString("Hostname", "");
        String extension = getResources().getString(R.string.url_user_string);
        String param = getResources().getString(R.string.url_user_name);
        mUrl = mUrl + extension + "?" + param + "=" + mUserName;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, String.valueOf(response));
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                mUser = new User(response);
                mUserId = mUser.getUserId();
                Log.d(TAG, mUserId + mUser.getName());
                editor.putLong("userId", mUserId);
                editor.apply();
                realm.insertOrUpdate(mUser);
                realm.commitTransaction();
                realm.close();
                getData();
                Toast.makeText(getApplicationContext(), "Application Starting for user #" + mUserId, Toast.LENGTH_SHORT).show();

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, String.valueOf(error));
                if (mUserName.equals("master")) {
                    mUserId = -1L;
                    editor.putLong("userId", mUserId);
                    editor.apply();
                    Realm realm = Realm.getDefaultInstance();
                    User mUser = new User();
                    realm.beginTransaction();
                    realm.insertOrUpdate(mUser);
                    realm.commitTransaction();
                    realm.close();
                    getData();
                    Toast.makeText(getApplicationContext(), "Application Starting for user #" + mUserId, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "User not present\nRegister first", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void getData() {
        mLoginLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        getUsers();
        getGroups();
        getTransaction();
    }

    private void getTransaction() {
        String mUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("Hostname", "");
        String extension = getResources().getString(R.string.url_transaction_findByUserId);
        String param = getResources().getString(R.string.url_user_id);
        mUrl = mUrl + extension + "?" + param + "=" + mUserId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Transaction transaction = new Transaction(jsonObject);
                        realm.insertOrUpdate(transaction);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
                mTransactionsDownloaded = true;
                checkDownloaded();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                mTransactionsDownloaded = true;
                checkDownloaded();
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void getGroups() {
        String mUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("Hostname", "");
        String extension = getResources().getString(R.string.url_group_findByUserId);
        String param = getResources().getString(R.string.url_user_id);
        mUrl = mUrl + extension + "?" + param + "=" + mUserId;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Group group = new Group(jsonObject);
                        realm.insertOrUpdate(group);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
                mGroupsDownloaded = true;
                checkDownloaded();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                mGroupsDownloaded = true;
                checkDownloaded();
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void getUsers() {
        String mUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("Hostname", "");
        String extension = getResources().getString(R.string.url_user_findAll);
        mUrl = mUrl + extension;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject);
                        realm.insertOrUpdate(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                realm.commitTransaction();
                realm.close();
                mUsersDownloaded = true;
                checkDownloaded();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                mUsersDownloaded = true;
                checkDownloaded();
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(mUrl, listener, errorListener);
        VolleyRequests.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void checkDownloaded() {
        if (mUsersDownloaded && mGroupsDownloaded && mTransactionsDownloaded)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    //endregion
}
