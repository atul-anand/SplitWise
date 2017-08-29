package com.zemoso.atul.splitwise.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.HomePagerAdapter;
import com.zemoso.atul.splitwise.fragments.AddGroup;
import com.zemoso.atul.splitwise.fragments.AddUser;
import com.zemoso.atul.splitwise.fragments.Friends;
import com.zemoso.atul.splitwise.fragments.Groups;
import com.zemoso.atul.splitwise.fragments.Transactions;
import com.zemoso.atul.splitwise.models.Group;
import com.zemoso.atul.splitwise.models.User;
import com.zemoso.atul.splitwise.singletons.VolleyRequests;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddUser.FriendDataCallback, AddGroup.GroupDataCallback {

    //region Variable Declaration
    private static final String TAG = MainActivity.class.getSimpleName();

    //region Views
    private Toolbar toolbar;

    //region Sliding TabLayout (Fragment Components)
    private HomePagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TabLayout tabLayout;
    private Friends friends;
    private Groups groups;
    private Transactions transactions;
    //endregion

    private FloatingActionButton fab;
    //endregion

    //region Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mUserNameView;
    private TextView mUserEmailView;
    private ImageView mImageView;
    //endregion

    //region Data
    private SharedPreferences preferences;
    private String mHostname;
    private Long mUserId;
    private User mUser;
    private String mUsername;
    private String mEmailId;
    private String mImageUrl;
    //endregion

    //endregion

    //region Listener
    private View.OnClickListener floatingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AddBill.class));
        }
    };
    //endregion

    //region Inherited Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mHostname = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Hostname", "");
        mUserId = preferences.getLong("userId", 0L);
        Realm realm = Realm.getDefaultInstance();
        mUser = realm.where(User.class).equalTo("userId", mUserId).findFirst();
        mUsername = mUser.getName();
        mEmailId = mUser.getEmailId();
        mImageUrl = mUser.getImageFilePath();

        //region Sliding Layout
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        updateFragments();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //endregion

        //region Floating Action Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(floatingListener);
        //endregion

        //region Action Bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //endregion

        //region Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);


        View view = mNavigationView.getHeaderView(0);
        mImageView = view.findViewById(R.id.imageView);
        mUserNameView = view.findViewById(R.id.nav_user_name);
        mUserEmailView = view.findViewById(R.id.nav_user_email);
        Glide.with(this)
                .load(mImageUrl)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(mImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mImageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

        mUserNameView.setText(mUsername);
        mUserEmailView.setText(mEmailId);
        Log.d(TAG, mUsername);

        mDrawerToggle.syncState();
        mNavigationView.bringToFront();
        mDrawerLayout.requestLayout();


        //endregion

    }


    //region Menu Items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Action Bar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected");
        if (onNavigationItemSelected(item))
            return true;
        // Handle action buttons
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_websearch:
//                // create intent to perform web search for this planet
////                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
////                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
////                // catch event that there's no activity to handle intent
////                if (intent.resolveActivity(getPackageManager()) != null) {
////                    startActivity(intent);
////                } else {
//                    Toast.makeText(this, "Search not implemented yet.", Toast.LENGTH_LONG).show();
////                }
//                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG,"onPrepareOptionsMenu");
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavigationView);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    //endregion

    //region Navigation Drawer
    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed");
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
//        else
//            super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG,"onNavigationItemSelected");
        // Handle component_navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_add_user:
                getSupportFragmentManager().beginTransaction().add(AddUser.newInstance(this), "Add User").commit();
                break;
            case R.id.nav_add_group:
                getSupportFragmentManager().beginTransaction().add(AddGroup.newInstance(this), "Add Group").commit();
                break;
            case R.id.nav_logout:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putLong("userId", -1L);
                editor.apply();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
//                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    //endregion

    //region Private Methods

    private void updateFragments(){
        friends = Friends.getInstance();
        groups = Groups.getInstance();
        transactions = Transactions.getInstance();
        fragmentList.add(friends);
        fragmentList.add(groups);
        fragmentList.add(transactions);
        mPagerAdapter.notifyDataSetChanged();
    }


    //region Update Fragment Data
    //region Friend
    @Override
    public void updateFriendData(JSONObject jsonObject) {
        saveUser(jsonObject);
    }

    private void saveUser(JSONObject jsonObject) {
        String mHostName = getResources().getString(R.string.url_address);
        final String tag = getResources().getString(R.string.url_user_save);
        final String mUrl = mHostName + tag;
        Log.d(TAG, mUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, mUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, String.valueOf(response));
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        User user = new User(response);
                        realm.insertOrUpdate(user);
                        realm.commitTransaction();
                        realm.close();
                        friends.updateFriendData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.toString());
                Toast.makeText(getApplicationContext(), "Data not saved", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRequests.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void updateGroupData(JSONObject jsonObject, String mGroupName) {
        addGroup(jsonObject, mGroupName);
    }

    //endregion


    //region Group
    private void addCurrentUserToNewGroup(Long mGroupId) {

        String extension = getResources().getString(R.string.url_group_add);
        String param = getResources().getString(R.string.url_group_id);
        String param2 = getResources().getString(R.string.url_user_id);
        final String mUrl = mHostname + extension + "?" + param + "=" + mGroupId + "&" + param2 + "=" + mUserId;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Groups.getInstance().updateGroupData();
                Log.d(TAG, mUrl);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getGroup(String mGroupNam) {

        String extension = getResources().getString(R.string.url_group_name);
        final String mUrl = mHostname + extension + "?s=" + mGroupNam;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Group group = new Group(response);
                Long mGroupId = group.getGroupId();
                addCurrentUserToNewGroup(mGroupId);
                Log.d(TAG, mUrl);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mUrl, null, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void addGroup(JSONObject jsonObject, final String mGroupName) {
        String extension = getResources().getString(R.string.url_group_save);
        String mUrl = mHostname + extension;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getGroup(mGroupName);
                Log.d(TAG, String.valueOf(response));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mUrl, jsonObject, listener, errorListener);
        VolleyRequests.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
    //endregion

    //region Transaction
//    TODO: Update
    
    //endregion

    //endregion

}
