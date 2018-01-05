package com.example.tony.consoleapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   // private Button mAdd_Buttom;
   private EditText editText_name;
    private EditText editText_calorie;
    private EditText editText_store;

    // 変数を用意する

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Button btn = new Button(getContext());
        btn.setText("Add data");
        btn = (Button)view.findViewById(R.id.add);
        btn.setOnClickListener(this);
        editText_name = view.findViewById(R.id.add_name_text);
        editText_calorie = view.findViewById(R.id.add_calorie_text);
        editText_store = view.findViewById(R.id.add_store_text);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String s) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(s);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name , String calorie, String store);
    }
    @Override
    public void onClick(View v) {
        // TODO 自動生成されたメソッド・スタブ
        //Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
        // エディットテキストのテキストを取得
        String name = editText_name.getText().toString();
        String calorie = editText_calorie.getText().toString();
        String store = editText_store.getText().toString();
//      String name = "GreenTea";
//       String calorie = "100";
//      String store = "7-11";
        Bundle bundle = getArguments();
        int lastID = bundle.getInt("lastID");
//        // DBへの登録処理
        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.openDB();
        dbAdapter.saveDB(String.valueOf(lastID + 1),name, calorie, store);
        dbAdapter.closeDB();
        mListener.onFragmentInteraction(name, calorie, store);
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack ("fragB", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return true;
    }
}
