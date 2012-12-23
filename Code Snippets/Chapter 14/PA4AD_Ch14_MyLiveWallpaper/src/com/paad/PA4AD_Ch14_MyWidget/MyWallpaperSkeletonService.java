package com.paad.PA4AD_Ch14_MyWidget;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MyWallpaperSkeletonService extends WallpaperService {
    
  @Override
  public Engine onCreateEngine() {  
    return new MyWallpaperServiceEngine();
  }
  
  /**
   * Listing 14-36: Wallpaper Service Engine skeleton code
   **/
  public class MyWallpaperServiceEngine extends WallpaperService.Engine {

    private static final int FPS = 30;
    private final Handler handler = new Handler();

    @Override
    public void onCreate(SurfaceHolder surfaceHolder) {
      super.onCreate(surfaceHolder);
      // TODO Handle initialization.
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset,
                                 float xOffsetStep, float yOffsetStep,
                                 int xPixelOffset, int yPixelOffset) {
      super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
                             xPixelOffset, yPixelOffset);
      // Triggered whenever the user swipes between multiple 
      // home-screen panels. 
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
      super.onTouchEvent(event);
      // Triggered when the Live Wallpaper receives a touch event
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder) {
      super.onSurfaceCreated(holder);
      // TODO Surface has been created, begin the update loop that will
      // update the Live Wallpaper.
      drawFrame();
    }

    private void drawFrame() {
      final SurfaceHolder holder = getSurfaceHolder();

      Canvas canvas = null;
      try {
        canvas = holder.lockCanvas();
        if (canvas != null) {
          // Draw on the Canvas!
        }
      } finally {
        if (canvas != null) 
          holder.unlockCanvasAndPost(canvas);
      }

      // Schedule the next frame
      handler.removeCallbacks(drawSurface);
      handler.postDelayed(drawSurface, 1000 / FPS);
    }

    // Runnable used to allow you to schedule frame draws.
    private final Runnable drawSurface = new Runnable() {
      public void run() {
        drawFrame();
      }
    };
  }
}