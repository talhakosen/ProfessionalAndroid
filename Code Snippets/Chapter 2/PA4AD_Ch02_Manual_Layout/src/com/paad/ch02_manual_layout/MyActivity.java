package com.paad.ch02_manual_layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyActivity extends Activity {

  /**
   * Listing 2-3: Creating layouts in code
   */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout.LayoutParams lp;
    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
                                       LinearLayout.LayoutParams.FILL_PARENT);

    LinearLayout.LayoutParams textViewLP;
    textViewLP = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.FILL_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);

    TextView myTextView = new TextView(this);
    myTextView.setText(getString(R.string.hello));

    ll.addView(myTextView, textViewLP);
    this.addContentView(ll, lp);
  }

}
