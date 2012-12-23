/** 
 * Create a new UI layout entirely in code rather than using
 * an external XML layout resource. Creates an in-code equivalent
 * to {@link clearable_edit_text_activity} which is used in 
 * {@link A3AdvancedLayoutResource}
 */

package com.paad.chapter4;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class A4AdvancedLayoutCode extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);

	  // Create the Linear Layout object that will contain
	  // the Views being used in the UI
	  LinearLayout ll = new LinearLayout(this);
	  ll.setOrientation(LinearLayout.VERTICAL);
	      
	  // Create the TextView and EditText controls being
	  // used in this UI.
	  TextView myTextView = new TextView(this);
	  EditText myEditText = new EditText(this);

	  myTextView.setText("Enter Text Below");
	  myEditText.setText("Text Goes Here!");

	  // Create and use the layout parameters that control how each
	  // child control will be laid out within the Linear Layout.
	  int lHeight = LinearLayout.LayoutParams.FILL_PARENT;
	  int lWidth = LinearLayout.LayoutParams.WRAP_CONTENT;	  

	  ll.addView(myTextView, new LinearLayout.LayoutParams(lHeight, lWidth));
	  ll.addView(myEditText, new LinearLayout.LayoutParams(lHeight, lWidth));
	  
	  // Set the Linear Layout to be used as the Activity's UI.
	  setContentView(ll);
	}
	
}