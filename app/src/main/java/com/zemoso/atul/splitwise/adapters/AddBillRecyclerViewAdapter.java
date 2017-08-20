package com.zemoso.atul.splitwise.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.zemoso.atul.splitwise.R;
import com.zemoso.atul.splitwise.javaBeans.TransactionHolder;

import java.util.List;

/**
 * Created by zemoso on 19/8/17.
 */

public class AddBillRecyclerViewAdapter extends RecyclerView.Adapter<AddBillRecyclerViewAdapter.RecyclerViewViewHolder> {

    //region Variable Declaration
    private static final String TAG = AddBillRecyclerViewAdapter.class.getSimpleName();

    private List<TransactionHolder> mItems;
    private Context mContext;

    public AddBillRecyclerViewAdapter(List<TransactionHolder> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }
    //endregion

    //region Inherited Methods
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_add_bill, parent, false);
        return new AddBillRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {

        final RecyclerViewViewHolder hold = holder;
        final int pos = holder.getAdapterPosition();

        TransactionHolder transactionHolder = mItems.get(pos);

        final Long id = transactionHolder.getUserId();

        String name = transactionHolder.getName();
        String amount = transactionHolder.getAmount().toString();

        holder.textView.setText(name);
        holder.editText.setText(amount);

        holder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nam = String.valueOf(hold.textView.getText());
                Double amt = Double.parseDouble(String.valueOf(hold.editText.getText()));
                TransactionHolder tH = new TransactionHolder(id, nam, amt);
                mItems.set(pos, tH);
                mItems.add(new TransactionHolder(-1L, "", 0.0));
                notifyDataSetChanged();
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
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

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView textView;
        EditText editText;
        Button button;
        Button btnCheck;

        RecyclerViewViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bill_card_name);
            editText = itemView.findViewById(R.id.bill_card_amt);
            button = itemView.findViewById(R.id.add_bill_button_subtract);
            btnCheck = itemView.findViewById(R.id.add_bill_button_check);
        }
    }
}
