package com.example.vishnu.theshield;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class Checker extends Fragment {


    public Checker() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_checker,
                container, false);

        final Spinner spinner2 =  rootView.findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.safety_window_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment

        final Spinner spinner =  rootView.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.time_interval_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
      Switch gswitch = rootView.findViewById(R.id.gswitch);
        final MainActivity mainActivity ;
        mainActivity = (MainActivity) getActivity();
      final Button setbtn = rootView.findViewById(R.id.set);
        final TextView tv1 = rootView.findViewById(R.id.textView2);
        final TextView tv2 = rootView.findViewById(R.id.textView3);
        gswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    spinner.setEnabled(true);
                    spinner.setClickable(true);
                    spinner2.setEnabled(true);
                    spinner2.setClickable(true);
                    setbtn.setClickable(true);
                    setbtn.setEnabled(true);
                    tv1.setEnabled(true);
                    tv2.setEnabled(true);
                    tv1.setClickable(true);
                    tv2.setClickable(true);

                }
                else
                {
                   
                   try {
                       mainActivity.myTimer.cancel();
                   }
                   catch(Exception e)
                   {
                       ;
                   }
                    spinner.setEnabled(false);
                    spinner.setClickable(false);
                    spinner2.setEnabled(false);
                    spinner2.setClickable(false);
                    setbtn.setClickable(false);
                    setbtn.setEnabled(false);
                    tv1.setEnabled(false);
                    tv2.setEnabled(false);
                    tv1.setClickable(false);
                    tv2.setClickable(false);
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });



        return rootView;

    }

}
