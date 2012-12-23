package com.paad.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyActivity extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    ListView myListView = (ListView)findViewById(R.id.my_list_view);
    
    /**
     * Listing 4-22: Creating and applying an Adapter
     */
    ArrayList<String> myStringArray = new ArrayList<String>();
    int layoutID = android.R.layout.simple_list_item_1;

    ArrayAdapter<String> myAdapterInstance;
    myAdapterInstance = 
      new ArrayAdapter<String>(this, layoutID, myStringArray);

    myListView.setAdapter(myAdapterInstance);

    //**
    myStringArray.add("The");
    myStringArray.add("quick");    
    myStringArray.add("green");
    myStringArray.add("Android");
    myStringArray.add("jumped");
    myStringArray.add("over");
    myStringArray.add("the");
    myStringArray.add("lazy");
    myAdapterInstance.notifyDataSetChanged();
  }
}