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
import android.widget.TextView;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;

import java.util.List;

/**
 * Created by zemoso on 19/8/17.
 */

public class AddBillRecyclerViewAdapter extends RecyclerView.Adapter<AddBillRecyclerViewAdapter.RecyclerViewViewHolder> {

    private static final String TAG = AddBillRecyclerViewAdapter.class.getSimpleName();

    private List<TransactionHolder> mItems;
    private Context mContext;

    public AddBillRecyclerViewAdapter(List<TransactionHolder> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_add_bill, parent, false);
        return new AddBillRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, final int position) {
        final TransactionHolder transactionHolder = mItems.get(position);
        String name = transactionHolder.getName();
        Double amount = transactionHolder.getAmount();
        holder.textView.setText(name);
        holder.editText.setText(amount.toString());
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                transactionHolder.setAmount(Double.parseDouble(String.valueOf(charSequence)));
                notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        EditText editText;
        Button button;

        public RecyclerViewViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bill_card_name);
            editText = itemView.findViewById(R.id.bill_card_amt);
            button = itemView.findViewById(R.id.add_bill_button_subtract);
        }
    }
}
