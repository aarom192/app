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
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tony on 2018/1/9.
 */

public class TestDialogFragment extends DialogFragment {
    EditText mName;
    EditText mCalorie;

    private TestDialogFragmentListener mListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_setting, null);
        mName = content.findViewById(R.id.name);
        mCalorie = content.findViewById(R.id.calorie);
        builder.setView(content);

        builder.setMessage("更新")
                .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.TestDialogFragmentInteraction(mName.getText().toString(), mCalorie.getText().toString(), "7-11");
                        //Toast.makeText(getActivity(), "Name:" +mName.getText().toString()+ "  and Calories:" + mCalorie.getText().toString()+ "kcal",Toast.LENGTH_SHORT).show();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (TestDialogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface TestDialogFragmentListener {
        // TODO: Update argument type and name
        void TestDialogFragmentInteraction(String name , String calorie, String store);
    }
}