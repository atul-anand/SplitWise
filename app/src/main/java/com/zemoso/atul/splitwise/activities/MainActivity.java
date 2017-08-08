package com.zemoso.atul.splitwise.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.adapters.HomePagerAdapter;
import com.zemoso.atul.splitwise.fragments.Activities;
import com.zemoso.atul.splitwise.fragments.Friends;
import com.zemoso.atul.splitwise.fragments.Groups;

import java.util.ArrayList;
import java.util.List;


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
    private LayoutInflater mLayoutInflater;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ArrayAdapter mArrayAdapter;
    private String[] mNavBarTitles;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    //endregion
    //endregion

    //region Inherited Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //endregion

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        //region Navigation Bar
////        mNavBarTitles = getResources().getStringArray(R.array.nav_bar);
//        mTitle = mDrawerTitle = getSupportActionBar().getTitle();
//
//        mLayoutInflater = getLayoutInflater();
//        mDrawerLayout = (DrawerLayout) mLayoutInflater.inflate(R.layout.component_navigation,null);
//        mDrawerList = mDrawerLayout.findViewById(R.id.nav_bar_menu);
//
//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
//
//        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.nav_bar_list_item,mNavBarTitles);
//
//        mDrawerList.setAdapter(mArrayAdapter);
//
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
//        {
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//        };
//
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        //endregion

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Action Bar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
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
                Toast.makeText(this,"Action Settings",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle component_navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home: Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting: Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rating: Toast.makeText(this,"Rating",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact: Toast.makeText(this,"Contact",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout: Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
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
        fragmentList.add(Activities.newInstance());
        mPagerAdapter.notifyDataSetChanged();
    }

    //endregion

}
