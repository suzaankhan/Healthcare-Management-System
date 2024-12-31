package com.sem7project.sehatmitr.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private String[] items;

    public CustomSpinnerAdapter(Context context, String[] items) {
        super(context, android.R.layout.simple_spinner_dropdown_item, items);
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;

        // Disable the first item (the prompt)
        if (position == 0) {
            textView.setEnabled(false); // Disable the text view
            textView.setTextColor(Color.GRAY); // Set text color to gray
        } else {
            textView.setEnabled(true); // Enable other items
            textView.setTextColor(Color.BLACK); // Set text color to black
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        // Disable the first item (the prompt)
        return position != 0;
    }
}




//----------------------------------------------------------------------------------------------
//package com.sem7project.sehatmitr.utils;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//public class CustomSpinnerAdapter extends ArrayAdapter<String> {
//
//    private String[] items;
//
//    public CustomSpinnerAdapter(Context context, String[] items) {
//        super(context, android.R.layout.simple_spinner_item, items);
//        this.items = items;
//    }
//
//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View view = super.getDropDownView(position, convertView, parent);
//
//        // Disable the first item
//        if (position == 0) {
//            TextView textView = (TextView) view;
//            textView.setEnabled(false); // Disable the text view
//            textView.setTextColor(Color.GRAY);
////            textView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray)); // Change color to gray
//        } else {
//            TextView textView = (TextView) view;
//            textView.setEnabled(true); // Enable other items
//            textView.setTextColor(Color.BLACK);
////            textView.setTextColor(getContext().getResources().getColor(android.R.color.black)); // Change color back to black
//        }
//
//        return view;
//    }
//}
