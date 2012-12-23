package com.paad.ch11test;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

  private SurfaceHolder holder;
  private MySurfaceViewThread mySurfaceViewThread;
  private boolean hasSurface;

  MySurfaceView(Context context) {
    super(context);
    init();
  }
 
  private void init() {
    // Create a new SurfaceHolder and assign this class as its callback.
    holder = getHolder();
    holder.addCallback(this);
    hasSurface = false;
  }

  public void resume() {
    // Create and start the graphics update thread.
    if (mySurfaceViewThread == null) {
      mySurfaceViewThread = new MySurfaceViewThread();

      if (hasSurface == true)
        mySurfaceViewThread.start();
    }
  }

  public void pause() {
    // Kill the graphics update thread
    if (mySurfaceViewThread != null) {
      mySurfaceViewThread.requestExitAndWait();
      mySurfaceViewThread = null;
    }
  }

  public void surfaceCreated(SurfaceHolder holder) {
    hasSurface = true;
    if (mySurfaceViewThread != null)
      mySurfaceViewThread.start();
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;
    pause();
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    if (mySurfaceViewThread != null)
      mySurfaceViewThread.onWindowResize(w, h);
  }

  //--------------------------------------------------------------------------

  class MySurfaceViewThread extends Thread {
    private boolean done;

    MySurfaceViewThread() {
      super();
      done = false;
    }

    @Override
    public void run() {
      SurfaceHolder surfaceHolder = holder;
      
      // Repeat the drawing loop until the thread is stopped.
      while (!done) {
        // Lock the surface and return the canvas to draw onto.
        Canvas canvas = surfaceHolder.lockCanvas();

        // TODO: Draw on the canvas!

        // Unlock the canvas and render the current image.
        surfaceHolder.unlockCanvasAndPost(canvas);
      }
    }

    public void requestExitAndWait() {
      // Mark this thread as complete and combine into
      // the main application thread.
      done = true;
      try {
        join();
      } catch (InterruptedException ex) { }
    }

    public void onWindowResize(int w, int h) {
      // Deal with a change in the available surface size.
    }
  }
}