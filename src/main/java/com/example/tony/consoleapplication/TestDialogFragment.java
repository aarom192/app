package com.example.tony.consoleapplication;

/**
 * Created by Tony on 2018/1/9.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Tony on 2018/1/9.
 */

public class TestDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_setting, null);

        builder.setView(content);

        builder.setMessage("設定")
                .setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}