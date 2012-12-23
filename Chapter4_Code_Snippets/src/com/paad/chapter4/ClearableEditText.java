/**
 * Creating a new compound view by extending LinearLayout.
 * In this example the layout itself is defined as an external
 * resource {@link clearable_edit_text_view} which is inflated
 * within the constructor.
 */

package com.paad.chapter4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ClearableEditText extends LinearLayout {

  EditText editText;
  Button clearButton; 

  public ClearableEditText(Context context) {
    super(context);

    // Get a reference to the LayoutInflater Service
    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);

    // Inflate the view from the layout resource
    li.inflate(R.layout.clearable_edit_text_view, this, true);

    // Get references to the child controls
    editText = (EditText)findViewById(R.id.editText);
    clearButton = (Button)findViewById(R.id.clearButton);

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
