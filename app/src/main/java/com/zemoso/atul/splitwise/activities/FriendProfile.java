package com.zemoso.atul.splitwise.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.modules.User;

import io.realm.Realm;

public class FriendProfile extends AppCompatActivity {

    private static final String TAG = FriendProfile.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.rgb(0, 0, 0)));
//        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(0, 0, 0));

        Bundle mBundle = getIntent().getExtras();
        int id = mBundle.getInt("id");
        Realm realm = Realm.getDefaultInstance();
        User mUser = realm.where(User.class).equalTo("userId", id).findFirst();

        ImageView mImageView = (ImageView) findViewById(R.id.profile_image);
        TextView mHeading = (TextView) findViewById(R.id.profile_heading);
        TextView mEmail = (TextView) findViewById(R.id.profile_email);
        TextView mStatus = (TextView) findViewById(R.id.profile_status);
        Button mButton = (Button) findViewById(R.id.profile_settleUp);

        getSupportActionBar().setTitle(mUser.getName());

        Glide.with(this)
                .load(mUser.getImageFilePath())
                .into(mImageView);
        mHeading.setText(mUser.getName());
        mEmail.setText(mUser.getEmailId());
        mStatus.setText(String.valueOf(mUser.getDebt()));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Settled", Toast.LENGTH_SHORT).show();

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
