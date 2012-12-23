/**
 * Demonstrate the custom View created in {@link MyView}.
 * */

package com.paad.chapter4;

import android.app.Activity;
import android.os.Bundle;

public class A6CustomView extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);

	  MyView view = new MyView(this);
	  
	  setContentView(view);	  
	}
	
}