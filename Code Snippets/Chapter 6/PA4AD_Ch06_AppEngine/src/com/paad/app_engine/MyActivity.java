package com.paad.app_engine;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    listing608();
  }
  
  private GetAuthTokenCB myAccountManagerCallback = new GetAuthTokenCB();
  
  private void listing608() {
    /**
     */
    String acctSvc = Context.ACCOUNT_SERVICE;
    AccountManager accountManager = (AccountManager)getSystemService(acctSvc);

    Account[] accounts = accountManager.getAccountsByType("com.google");

    if (accounts.length > 0)
      accountManager.getAuthToken(accounts[0], "ah", false, 
                                  myAccountManagerCallback, null);

  }
  
  private static int ASK_PERMISSION = 1;

  private class GetAuthTokenCB implements AccountManagerCallback<Bundle> {
    public void run(AccountManagerFuture<Bundle> result) {
      try { 
        Bundle bundle = result.getResult();
        Intent launch = (Intent)bundle.get(AccountManager.KEY_INTENT);
        if (launch != null) 
          startActivityForResult(launch, ASK_PERMISSION);
        else {
          String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
          executeHttp(auth_token);
        }
      }
      catch (Exception ex) {}
    }
  };
  
  private boolean executeHttp(String auth_token) throws ClientProtocolException, IOException {
    DefaultHttpClient http_client = new DefaultHttpClient();
    http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

    String getString = "https://[yourappsubdomain].appspot.com/_ah/login?" +
                       "continue=http://localhost/&auth=" + 
                       auth_token;
    HttpGet get = new HttpGet(getString);

    HttpResponse response = http_client.execute(get);
    
    if (response.getStatusLine().getStatusCode() != 302)
      return false;
    else {
      for (Cookie cookie : http_client.getCookieStore().getCookies())
        if (cookie.getName().equals("ACSID")) {
          // Make authenticated requests to your Google App Engine server.
          return true;
        }
    }
    return false;
  }
}