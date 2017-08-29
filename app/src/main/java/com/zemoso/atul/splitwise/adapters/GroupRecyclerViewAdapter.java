package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.activities.GroupDetail;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;

import java.util.List;

/**
 * Created by zemoso on 16/8/17.
 */

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = FriendRecyclerViewAdapter.class.getSimpleName();

    private List<RecyclerViewHolder> mItems;
    private Context mContext;
    //endregion

    public GroupRecyclerViewAdapter(List<RecyclerViewHolder> mItems, Context mContext) {
        Log.d(TAG, "Constructor");
        this.mItems = mItems;
        this.mContext = mContext;
    }

    //region Inherited Methods
    @Override
    public GroupRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "RecyclerViewViewHolder");
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_friend_group, parent, false);
        return new GroupRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroupRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        int pos = holder.getAdapterPosition();
        final RecyclerViewHolder mCardData = mItems.get(pos);

        if (mCardData.isImageDownloaded())
            Glide.with(mContext)
                    .load(mCardData.getmAvatarFilePath())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.mAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.mAvatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        else {
            Glide.with(mContext)
                    .load(mCardData.getmAvatarUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.mAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.mAvatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
//            TODO: Set File Path
        }

        holder.mHeading.setText(mCardData.getmHeading());
//        holder.mStatus.setText(mCardData.getmStatus());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mCardData.getId() + " " + mCardData.getmHeading(), Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(mContext, GroupDetail.class);
                Bundle mBundle = new Bundle();
                mBundle.putLong("groupId", mCardData.getId());
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mHeading;
        //        TextView mStatus;
        CardView mCardView;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.card_avatar);
            mHeading = itemView.findViewById(R.id.card_heading);
//            mStatus = itemView.findViewById(R.id.card_status);
            mCardView = itemView.findViewById(R.id.card_holder);
        }
    }
}
