/**
 * Create the user interface by inflating a layout 
 * resource.
 */

package com.paad.chapter4;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

// Demonstrates inflating a layout resource to use
// as the Activity UI.
public class A1LayoutResource extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);

	  // Inflate the layout resource to use as the UI
	  setContentView(R.layout.main);
	  
	  // Extract a reference to the TextView used in the layout
	  TextView myTextView = (TextView)findViewById(R.id.myTextView);
	}
	
}