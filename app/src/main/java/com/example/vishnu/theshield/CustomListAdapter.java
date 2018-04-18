package com.example.vishnu.theshield;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] infoArray;
    public CustomListAdapter(Activity context, String[] infoArrayParam){

        super(context,R.layout.lisview_row , infoArrayParam);
        this.context=context;
        this.infoArray = infoArrayParam;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.lisview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.lvtv);


        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(infoArray[position]);


        return rowView;

    }

}

