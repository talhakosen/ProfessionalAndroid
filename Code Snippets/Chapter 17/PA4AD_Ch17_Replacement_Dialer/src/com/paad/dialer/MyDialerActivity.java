package com.paad.dialer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MyDialerActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /**
     * Listing 17-2: Initiating a call using the system telephony stack
     */
    Intent whoyougonnacall = new Intent(Intent.ACTION_CALL, 
                                        Uri.parse("tel:555-2368"));
    startActivity(whoyougonnacall);
  }
}