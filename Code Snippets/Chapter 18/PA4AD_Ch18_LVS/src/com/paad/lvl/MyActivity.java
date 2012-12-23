package com.paad.lvl;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
  
  private LicenseChecker licenseChecker;
  String deviceID = UUID.randomUUID().toString();
  private String PUBLIC_KEY = ""; // TODO Get My Public Key
  
  //Generate 20 random bytes, and put them here.
  private static final byte[] SALT = new byte[] {
   -56, 42, 12, -18, -10, -34, 78, -75, 54, 88, 
   -13, -12, 36, 17, -34, 114, 77, 12, -23, -20};
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
  
   // Construct the LicenseChecker with a Policy.
   licenseChecker = 
     new LicenseChecker(this, new ServerManagedPolicy(this,
       new AESObfuscator(SALT, getPackageName(), deviceID)),
     PUBLIC_KEY);
  }

  private void checkLicense() {
    /**
     * Listing 18-9: Performing a license check
     */
    licenseChecker.checkAccess(new LicenseCheckerCallback(){
      public void allow() {
        // License verified.
      }

      public void dontAllow() {
        // License verification failed.
      }

      public void applicationError(ApplicationErrorCode errorCode) {
        // Handle associated error code.
      }
    });

  }
}