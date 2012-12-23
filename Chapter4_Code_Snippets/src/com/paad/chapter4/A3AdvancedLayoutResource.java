/**
 * Use the layout resource 
 * {@link clearable_edit_text_activity}
 * to set the Activity's UI.
 */

package com.paad.chapter4;

import android.app.Activity;
import android.os.Bundle;

// Use a custom
public class A3AdvancedLayoutResource extends Activity {
	
  @Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);

	  setContentView(R.layout.clearable_edit_text_activity);	  
	}
	
}