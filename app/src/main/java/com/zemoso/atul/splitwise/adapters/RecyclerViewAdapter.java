package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
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
import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.activities.MainActivity;
import com.zemoso.atul.splitwise.javaBeans.RecyclerViewHolder;

import java.util.List;

/**
 * Created by zemoso on 8/8/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private List<RecyclerViewHolder> mItems;
    private Context mContext;

    public RecyclerViewAdapter(List<RecyclerViewHolder> mItems, Context mContext) {
        Log.d(TAG,"Constructor");
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"RecyclerViewViewHolder");
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.component_card_view,parent,false);
        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");
        final RecyclerViewHolder mCardData = mItems.get(position);
        if(mCardData.isImageDownloaded())
            Glide.with(mContext)
                    .load(mCardData.getmAvatarFilePath())
                    .into(holder.mAvatar);
        else{
            Glide.with(mContext)
                    .load(mCardData.getmAvatarUrl())
                    .into(holder.mAvatar);
//            TODO: Set File Path
        }
        holder.mHeading.setText(mCardData.getmHeading());
        holder.mStatus.setText(mCardData.getmStatus());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,mCardData.getmHeading(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mHeading;
        TextView mStatus;
        CardView mCardView;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.card_avatar);
            mHeading = itemView.findViewById(R.id.card_heading);
            mStatus = itemView.findViewById(R.id.card_status);
            mCardView = itemView.findViewById(R.id.card_holder);
        }
    }
}