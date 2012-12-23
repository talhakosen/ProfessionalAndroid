package com.paad.actionbar;

import android.app.Activity;
import android.os.Bundle;

public class NonActionBarActivity extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.no_action_bar);
  }
}