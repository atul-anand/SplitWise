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
import com.zemoso.atul.splitwise.models.User;

import io.realm.Realm;

public class FriendDetail extends AppCompatActivity {

    private static final String TAG = FriendDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.rgb(0, 0, 0)));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(0, 0, 0));

        Bundle mBundle = getIntent().getExtras();
        long id = mBundle.getLong("userId");
        Realm realm = Realm.getDefaultInstance();
        User mUser = realm.where(User.class).equalTo("userId", id).findFirst();

        ImageView mImageView = (ImageView) findViewById(R.id.profile_image);
        TextView mHeading = (TextView) findViewById(R.id.profile_heading);
        TextView mEmail = (TextView) findViewById(R.id.profile_email);
        TextView mStatus = (TextView) findViewById(R.id.profile_status);
        Button mButton = (Button) findViewById(R.id.profile_settleUp);

        String name = mUser.getName();
        String imageUrl = mUser.getImageFilePath();
        String email = mUser.getEmailId();
        Double debt = mUser.getDebt();

        getSupportActionBar().setTitle(name);

        Glide.with(this)
                .load(imageUrl)
                .into(mImageView);
        mHeading.setText(name);
        mEmail.setText(email);
        mStatus.setText(String.valueOf(debt));
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
