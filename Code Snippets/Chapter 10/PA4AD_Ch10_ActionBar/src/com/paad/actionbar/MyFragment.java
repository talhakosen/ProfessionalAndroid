package com.paad.actionbar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {
  
  public void setFragmentText(CharSequence text) {
    fragmentTextString = text;
    if (fragmentText != null)
      fragmentText.setText(text);
  }
  
  private CharSequence fragmentTextString = "";
  private TextView fragmentText;
  
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
     
    View view = inflater.inflate(R.layout.fragment, container, false);
    
    fragmentText = (TextView)view.findViewById(R.id.fragmentText);
    fragmentText.setText(fragmentTextString);
    
    return view;
  }
}