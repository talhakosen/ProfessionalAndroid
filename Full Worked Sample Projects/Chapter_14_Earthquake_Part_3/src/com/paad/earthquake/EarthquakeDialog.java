package com.paad.earthquake;

import java.text.SimpleDateFormat;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EarthquakeDialog extends DialogFragment {
  
  private static String DIALOG_STRING = "DIALOG_STRING";
  
  public static EarthquakeDialog newInstance(Context context, Quake quake) {
    // Create a new Fragment instance with the specified 
    // parameters.
    EarthquakeDialog fragment = new EarthquakeDialog();
    Bundle args = new Bundle();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String dateString = sdf.format(quake.getDate());
    String quakeText = dateString + "\n" + "Magnitude " + quake.getMagnitude() +
                       "\n" + quake.getDetails() + "\n" + 
                       quake.getLink();

    
    args.putString(DIALOG_STRING, quakeText);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    
    View view = inflater.inflate(R.layout.quake_details, container, false); 

    String title = getArguments().getString(DIALOG_STRING);
    TextView tv = (TextView)view.findViewById(R.id.quakeDetailsTextView);
    tv.setText(title);
  
    return view;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.setTitle("Earthquake Details");
    return dialog;
  }
}