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
    EditText mStore;

    private TestDialogFragmentListener mListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final String ID = bundle.getString("id");
        final String name = bundle.getString("name");
        String calorie = bundle.getString("calorie");
        final String store = bundle.getString("store");
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.dialog_setting, null);
        mName = content.findViewById(R.id.name);
        mCalorie = content.findViewById(R.id.calorie);
        mStore = content.findViewById(R.id.store);
        mName.setText(name);
        mCalorie.setText(calorie);
        mStore.setText(store);
        builder.setView(content);

        builder.setMessage("更新")
                .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.TestDialogFragmentInteraction(ID, name, mName.getText().toString(), mCalorie.getText().toString(), mStore.getText().toString());
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
        void TestDialogFragmentInteraction(String id, String originName, String name , String calorie, String store);
    }
}