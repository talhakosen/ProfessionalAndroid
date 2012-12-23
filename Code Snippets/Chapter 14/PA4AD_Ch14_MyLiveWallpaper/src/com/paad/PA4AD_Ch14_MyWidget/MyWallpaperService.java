package com.paad.PA4AD_Ch14_MyWidget;

/** Listing 14-34: Creating a Wallpaper Service */
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;

public class MyWallpaperService extends WallpaperService {
  @Override
  public Engine onCreateEngine() {
    return new MyWallpaperServiceEngine();
  }

  public class MyWallpaperServiceEngine extends WallpaperService.Engine {
  }

}