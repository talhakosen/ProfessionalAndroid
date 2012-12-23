package com.paad.iab;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
  
  private static final String TAG = "IAB";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }
}