/**
 * Create a user interface in code.
 */

package com.paad.chapter4;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

// Demonstrates creating a new View in code
// to use as the Activity UI.
public class A2LayoutInCode extends Activity {
	
  @Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);

	  // Create a new TextView object 
	  TextView myTextView = new TextView(this);
	  
	  // Assign the TextView to be the Activity's UI
	  setContentView(myTextView);
	  
	  // Update the TextView properties
	  myTextView.setText("Hello, Android");
	}

}