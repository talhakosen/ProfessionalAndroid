/**
 * Creating a new compound view be extending LinearLayout.
 * In this example the layout is defined in code
 * within the constructor.
 */

package com.paad.chapter4;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ClearableEditTextInCode extends LinearLayout {

  EditText editText;
  Button clearButton; 

  public ClearableEditTextInCode(Context context) {
    super(context);
    
    // Set orientation of layout to vertical
    setOrientation(LinearLayout.VERTICAL);

    // Create the child controls.
    editText = new EditText(getContext());
    clearButton = new Button(getContext());
    clearButton.setText("Clear");

    // Lay them out in the compound control.
    int lHeight = LayoutParams.WRAP_CONTENT;
    int lWidth = LayoutParams.FILL_PARENT; 

    addView(editText, new LinearLayout.LayoutParams(lWidth, lHeight));
    addView(clearButton, new LinearLayout.LayoutParams(lWidth, lHeight));

    // Hook up the functionality
    hookupButton();
  }
  
  private void hookupButton() {
	  clearButton.setOnClickListener(new Button.OnClickListener() {
	    
	    public void onClick(View v) {
	      editText.setText("");
	    }
	  });
	}

}