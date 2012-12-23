package com.paad.preferences;

import java.util.List;

import android.preference.PreferenceActivity;

public class MyFragmentPreferenceActivity extends PreferenceActivity {

  @Override
  public void onBuildHeaders(List<Header> target) {
    loadHeadersFromResource(R.xml.preferenceheaders, target);
  }
}