/**
 * Skeleton code for extending a Layout to create
 * a compound view. 
 * Add code to the constructors to add new Views to
 * the layout as seen in {@link ClearableEditText}.
 */

package com.paad.chapter4;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyCompoundView extends LinearLayout {

  public MyCompoundView(Context context) {
    super(context);
  }

  public MyCompoundView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

}
