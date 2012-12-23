package com.paad.config_changes;

/**
 * Listing 3-7: Skeleton Application class
 * Listing 3-8: Overriding the Application Lifecycle Handlers
 */
import android.app.Application;
import android.content.res.Configuration;

public class MyApplication extends Application {

  private static MyApplication singleton;

  // Returns the application instance
  public static MyApplication getInstance() {
    return singleton;
  }

  @Override
  public final void onCreate() {
    super.onCreate();
    singleton = this;
  }

  @Override
  public final void onLowMemory() {
    super.onLowMemory();
  }

  @Override
  public final void onTrimMemory(int level) {
    super.onTrimMemory(level);
  }

  @Override
  public final void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }
}
