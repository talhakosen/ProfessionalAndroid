package com.paad.layouts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LayoutsActivity extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    Button linearButton = (Button)findViewById(R.id.linearButton);
    Button relativeButton = (Button)findViewById(R.id.relativeButton);
    Button gridButton = (Button)findViewById(R.id.gridbutton);
    
    linearButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(LayoutsActivity.this, LinearLayoutActivity.class));
      }      
    });
    
    relativeButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(LayoutsActivity.this, RelativeLayoutActivity.class));
      }      
    });
    
    gridButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(LayoutsActivity.this, GridLayoutActivity.class));
      }      
    });
  }
}