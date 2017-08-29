package com.zemoso.atul.splitwise.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by zemoso on 23/8/17.
 */

public class MultiSpinner extends android.support.v7.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    public static final String TAG = MultiSpinner.class.getSimpleName();

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

    private ArrayAdapter<String> adapter;

    public MultiSpinner(Context context) {
        super(context);
        Log.d(TAG, "first");
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        Log.d(TAG, "second");
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        Log.d(TAG, "third");
    }

    @Override
    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
        selected[position] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer stringBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                stringBuffer.append(items.get(i));
                stringBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = stringBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
//        adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_spinner_item,
//                items);
//        setAdapter(adapter);
        this.setPrompt(spinnerText);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all de-selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;
        Log.d(TAG, String.valueOf(items.size()));
        Log.d(TAG, allText);
        Log.d(TAG, String.valueOf(selected.length));
        // all text on the spinner

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_spinner_item, this.items);
//        this.setPrompt(allText);
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, items);
        setAdapter(adapter);
        this.setPrompt(defaultText);
    }

    public boolean[] getSelected() {
        return selected;
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public interface MultiSpinnerListener {
        void onItemsSelected(boolean[] selected);
    }
}
