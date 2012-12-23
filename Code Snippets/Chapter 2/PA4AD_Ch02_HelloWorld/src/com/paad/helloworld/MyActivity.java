/**
 * Listing 2-1: Hello World
 */

package com.paad.helloworld;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {

  /** Called when the Activity is first created. **/
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
  }
}
