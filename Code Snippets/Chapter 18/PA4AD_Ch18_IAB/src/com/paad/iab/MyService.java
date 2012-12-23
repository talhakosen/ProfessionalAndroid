package com.paad.iab;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  
  Context context = this;
  
  /**
   * Listing 18-10: Binding to the Market Billing Service
   */
  IMarketBillingService billingService;

  private void bindService() {
    try {
      String bindString = 
        "com.android.vending.billing.MarketBillingService.BIND";

      boolean result = context.bindService(new Intent(bindString), 
        serviceConnection, Context.BIND_AUTO_CREATE);

    } catch (SecurityException e) {
      Log.e(TAG, "Security Exception.", e);
    }
  }

  private ServiceConnection serviceConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className,
                                   IBinder service) {
      billingService = IMarketBillingService.Stub.asInterface(service);
    }

    public void onServiceDisconnected(ComponentName className) {
      billingService = null;
    }
  };

  /**
   * Listing 18-11: Creating a billing request
   */
  protected Bundle makeRequestBundle(String transactionType, 
                                     String itemId) {
    Bundle request = new Bundle();
    request.putString("BILLING_REQUEST", transactionType);
    request.putInt("API_VERSION", 1);
    request.putString("PACKAGE_NAME", getPackageName());
    if (itemId != null)
      request.putString("ITEM_ID", itemId);
    return request;
  }
}
