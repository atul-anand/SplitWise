package com.zemoso.atul.splitwise.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    //region Variable Declaration
    private static final String TAG = LoginActivity.class.getSimpleName();
    String[] da = {"Android", "Bluetooth", "Party"};
    //region System
    private Context mContext;
    //endregion
    private SharedPreferences.Editor editor;
    //region Views
    private AutoCompleteTextView mAutoCompleteTextView;
    private Button mButton;
    //endregion
    private ProgressBar mProgressBar;
    //region Data
    private Long mUserId;
    private List<User> mUsers;
    private List<String> userName;
    //endregion

    private ArrayAdapter<String> arrayAdapter;
    //endregion
    private TextWatcher userNameTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mText = String.valueOf(mAutoCompleteTextView.getText());
            userString(mText);
            findDataSet();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    //region Listeners
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mUserId = Long.valueOf(mAutoCompleteTextView.getText().toString());
            Log.d(TAG, mUserId + "111");
            editor.putLong("userId", mUserId);
            VolleyRequests.getInstance(getApplicationContext()).groupFindByUserId(mUserId);
            editor.apply();
            Toast.makeText(getApplicationContext(), "Application Starting for user #" + mUserId, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region System Data
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContext = getApplication();

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("Hostname", getResources().getString(R.string.url_address));
        editor.apply();
        //endregion

        //region Views
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete);
        mButton = (Button) findViewById(R.id.button);
        //endregion

//        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "Started");

        //region Data
        mUsers = new ArrayList<>();
        userName = new ArrayList<>();
        userString("");
        findDataSet();
        //endregion

        //region Adapter
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.card_autocomplete_item, userName);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAutoCompleteTextView.setAdapter(arrayAdapter);
        mAutoCompleteTextView.setThreshold(1);
        //endregion

        //region Attach Listeners
        mAutoCompleteTextView.addTextChangedListener(userNameTextListener);
        mButton.setOnClickListener(loginListener);
        //endregion

    }
    //endregion

    //region Private Methods
    private void userString(String param) {
        final String extension = mContext.getResources().getString(R.string.url_user_string);
        String mUrl = getSharedPreferences("Settings", 0).getString("Hostname", "") + extension + "?s=" + param;
        Log.d(TAG, mUrl);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mUsers.clear();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < response.length(); i++)
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User(jsonObject);
                        mUsers.add(user);
                        realm.insertOrUpdate(user);
                        Log.d(TAG, String.valueOf(jsonObject));
//                        Log.d(TAG, String.valueOf(userName.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                realm.commitTransaction();
                realm.close();
//                mProgressBar.setVisibility(View.GONE);
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
//        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void findDataSet() {
        userName.clear();
        for (User user : mUsers) {
            userName.add(user.getName());
            Log.d(TAG, userName.get(userName.size() - 1));
        }
    }
    //endregion
}
