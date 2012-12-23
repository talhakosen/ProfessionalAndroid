package com.paad.chapter2;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelloWorld extends Activity {

  // TextView that will be assigned to the 
  // TextView resource when it's inflated or
  // created in code.
  TextView myTextView;
  
  // Variable used to determine if the layout
  // should be inflated from XML or constructed
  // in code.
  private static boolean inflate = true;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    if (inflate)
      inflateXMLLayout();
    else
      constructLayout();    
  }
  
  // Use the 'main.xml' layout resource to create
  // the UI for this Activity.
  private void inflateXMLLayout() {
    setContentView(R.layout.main);
    myTextView = (TextView)findViewById(R.id.myTextView);    
  }
  
  // Create the Activity's UI layout by creating
  // and populating the layout in code.
  private void constructLayout() {
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    LinearLayout.LayoutParams textViewLP = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);
    myTextView = new TextView(this);
    myTextView.setText("Hello World, HelloWorld");
    ll.addView(myTextView, textViewLP);
    addContentView(ll, lp);
  }
}