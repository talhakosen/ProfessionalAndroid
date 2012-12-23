package com.paad.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MyFragmentActivity extends Activity {

  @SuppressLint("NewApi")
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate the layout containing the Fragment containers
    setContentView(R.layout.fragment_container_layout);
    
    FragmentManager fm = getFragmentManager();

    // Check to see if the Fragment back stack has been populated
    // If not, create and populate the layout.
    DetailsFragment detailsFragment = (DetailsFragment)fm.findFragmentById(R.id.details_container);
    
    if (detailsFragment == null) {
       FragmentTransaction ft = fm.beginTransaction(); 
       ft.add(R.id.details_container, new DetailsFragment());
       ft.add(R.id.ui_container, new MyListFragment());
       ft.commit();
     }
  }
}
