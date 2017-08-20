package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zemoso.atul.splitwise.R;

import java.util.List;

/**
 * Created by zemoso on 18/8/17.
 */

public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = MembersRecyclerViewAdapter.class.getSimpleName();

    private List<String> mItems;
    private Context mContext;
    //endregion

    public MembersRecyclerViewAdapter(List<String> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    //region Inherited Methods
    @Override
    public MembersRecyclerViewAdapter.RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_add_member_item, parent, false);
        return new MembersRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MembersRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {


        final String memberId = String.valueOf(holder.mEditText.getText());
        final int pos = holder.getAdapterPosition();

        mItems.set(pos, memberId);
        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItems.set(pos, (String.valueOf(charSequence)));
                notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    //endregion

    static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        EditText mEditText;
        Button mButton;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mEditText = itemView.findViewById(R.id.add_member_edit_text);
            mButton = itemView.findViewById(R.id.add_member_button_subtract);
        }
    }
}
