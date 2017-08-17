package com.zemoso.atul.splitwise.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.HomePagerAdapter;
import com.zemoso.atul.splitwise.fragments.Friends;
import com.zemoso.atul.splitwise.fragments.Groups;
import com.zemoso.atul.splitwise.fragments.Transactions;
import com.zemoso.atul.splitwise.modules.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region Variable Declaration
    private static final String TAG = MainActivity.class.getSimpleName();

    //region Sliding TabLayout (Fragment Components)
    private HomePagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TabLayout tabLayout;
    //endregion

    private FloatingActionButton fab;
    private Toolbar toolbar;

    //region Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private TextView mUserName;
    private TextView mEmail;

    private ActionBarDrawerToggle mDrawerToggle;

    //endregion

    private SharedPreferences preferences;
    private Long mUserId;
    private User mUser;
    private String mUsername;
    private String mEmailId;
    private JSONObject jsonObject;

    //endregion

    //region Inherited Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(),AddBill.class));
            }
        });
        //endregion

        //region Action Bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        mDrawerToggle.syncState();

        mUserName = (TextView) findViewById(R.id.nav_user_name);
        mEmail = (TextView) findViewById(R.id.nav_user_email);
        preferences = getSharedPreferences("Settings", 0);
        mUserId = preferences.getLong("UserId", 0);
        Realm realm = Realm.getDefaultInstance();
        mUser = realm.where(User.class).equalTo("userId", mUserId).findFirst();

        mUsername = mUser.getName();
        mEmailId = mUser.getEmailId();
        mUserName.setText(mUsername);
        mEmail.setText(mEmailId);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //endregion

    }



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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return onNavigationItemSelected(item);
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
//                // catch event that there's no activity to handle intent
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
                    Toast.makeText(this, "Search not implemented yet.", Toast.LENGTH_LONG).show();
//                }
                return true;
            case R.id.action_settings:
//                startActivity(new Intent(this,SettingsActivity.class));
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
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed");
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    //Navigation Drawer menu items
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG,"onNavigationItemSelected");
        // Handle component_navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home: Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting: Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rating: Toast.makeText(getApplicationContext(),"Rating",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact: Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout: Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    //region Private Methods

    private void updateFragments(){
        fragmentList.add(Friends.newInstance());
        fragmentList.add(Groups.newInstance());
        fragmentList.add(Transactions.newInstance());
        mPagerAdapter.notifyDataSetChanged();
    }

    //endregion

}
