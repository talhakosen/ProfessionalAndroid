package com.paad.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Listing 10-24: Using the On Create Dialog event handler
 */
public class MyDialogFragment extends DialogFragment {
  
  private static String CURRENT_TIME = "CURRENT_TIME";

  public static MyDialogFragment newInstance(String currentTime) {
    // Create a new Fragment instance with the specified 
    // parameters.
    MyDialogFragment fragment = new MyDialogFragment();
    Bundle args = new Bundle();
    args.putString(CURRENT_TIME, currentTime);
    fragment.setArguments(args);

    return fragment;
  }
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Create the new Dialog using the AlertBuilder.
    AlertDialog.Builder timeDialog = 
      new AlertDialog.Builder(getActivity());

    // Configure the Dialog UI.
    timeDialog.setTitle("The Current Time Is...");
    timeDialog.setMessage(getArguments().getString(CURRENT_TIME));

    // Return the configured Dialog.
    return timeDialog.create();
  }
  
//  /**
//   * Listing 10-26: Using the On Create View handler
//   */
//  @Override
//  public View onCreateView(LayoutInflater inflater, ViewGroup container,
//    Bundle savedInstanceState) {
//    
//    // Inflate the Dialog's UI.
//    View view = inflater.inflate(R.layout.dialog_view, container, false);
//
//    // Update the Dialog's contents.
//    TextView text = (TextView)view.findViewById(R.id.dialog_text_view);
//    text.setText("This is the text in my dialog");
//
//    return view;
//  }
}