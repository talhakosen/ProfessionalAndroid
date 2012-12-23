package com.paad.preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyFragment extends Fragment {
  
  private static String USER_SELECTION = "USER_SELECTION";
  private int userSelection = 0;
  private TextView tv;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    if (savedInstanceState != null)
      userSelection = savedInstanceState.getInt(USER_SELECTION);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, 
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.mainfragment, container, false);
    
    tv = (TextView)v.findViewById(R.id.text);
    setSelection(userSelection);
    
    Button b1 = (Button)v.findViewById(R.id.button1);
    Button b2 = (Button)v.findViewById(R.id.button2);
    Button b3 = (Button)v.findViewById(R.id.button3);
    
    b1.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        setSelection(1);
      }
    });
   
    b2.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        setSelection(2);
      }
    });
    
    b3.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        setSelection(3);
      }
    });
    
    return v;
  }
  
  private void setSelection(int selection) {
    userSelection = selection;
    tv.setText("Selected: " + selection);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt(USER_SELECTION, userSelection);
    super.onSaveInstanceState(outState);
  }
}