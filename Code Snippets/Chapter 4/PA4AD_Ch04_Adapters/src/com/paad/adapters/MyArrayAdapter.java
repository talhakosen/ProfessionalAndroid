package com.paad.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;


/**
 * Listing 4-21: Customizing the Array Adapter
 */
public class MyArrayAdapter extends ArrayAdapter<MyClass> {

  int resource;

  public MyArrayAdapter(Context context,
                         int _resource,
                         List<MyClass> items) {
    super(context, _resource, items);
    resource = _resource;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Create and inflate the View to display
    LinearLayout newView;

    if (convertView == null) {
      // Inflate a new view if this is not an update.
      newView = new LinearLayout(getContext());
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater li;
      li = (LayoutInflater)getContext().getSystemService(inflater);
      li.inflate(resource, newView, true);
    } else {
      // Otherwise we'll update the existing View
      newView = (LinearLayout)convertView;
    }

    MyClass classInstance = getItem(position);

    // TODO Retrieve values to display from the
    // classInstance variable.

    // TODO Get references to the Views to populate from the layout.
    // TODO Populate the Views with object property values.

    return newView;
  }
}