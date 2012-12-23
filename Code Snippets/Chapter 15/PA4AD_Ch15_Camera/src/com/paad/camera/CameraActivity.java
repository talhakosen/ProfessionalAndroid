package com.paad.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Listing 15-26: Previewing a real-time camera stream
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    SurfaceView surface = (SurfaceView)findViewById(R.id.surfaceView);
    SurfaceHolder holder = surface.getHolder();
    holder.addCallback(this);
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    holder.setFixedSize(400, 300);
    
    Button snap = (Button)findViewById(R.id.buttonTakePicture);
    Button exif = (Button)findViewById(R.id.buttonExif);
    Button distances = (Button)findViewById(R.id.buttonFocusDistances);
    
    snap.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        takePicture(); 
      }
    });
    
    exif.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        modifyExif(); 
      }
    });
    
    distances.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        findFocusDistances(); 
      }
    });
  }
  
  public void surfaceCreated(SurfaceHolder holder) { 
    try {
      camera.setPreviewDisplay(holder);
      camera.startPreview();
      camera.startFaceDetection();
      // TODO Draw over the preview if required.
    } catch (IOException e) {
      Log.d(TAG, e.getMessage());
    }
  }
  
  public void surfaceDestroyed(SurfaceHolder holder) {
    camera.stopFaceDetection();
    camera.stopPreview();
  }
  
  public void surfaceChanged(SurfaceHolder holder, int format, 
                             int width, int height) {
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    camera.release();
  }

  @Override
  protected void onResume() {
    super.onResume();
    camera = Camera.open();        
  }

  private static final String TAG = "CameraActivity";
  private Camera camera;
  
  private void findFocusDistances() {
    Camera.Parameters parameters = camera.getParameters();
    
    /**
     * Listing 15-25: Finding the distance to focused objects
     */
    float[] focusDistances = new float[3];

    parameters.getFocusDistances(focusDistances);

    float near =
      focusDistances[Camera.Parameters.FOCUS_DISTANCE_NEAR_INDEX];
    float far = 
      focusDistances[Camera.Parameters.FOCUS_DISTANCE_FAR_INDEX];
    float optimal = 
      focusDistances[Camera.Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX];
    
    Log.d(TAG, "Focus Distances: " + near + ", " + far + ", " + optimal);
  }
  
  /**
   * Listing 15-27: Taking a picture
   */
  private void takePicture() {
    camera.takePicture(shutterCallback, rawCallback, jpegCallback);
  }

  ShutterCallback shutterCallback = new ShutterCallback() {
    public void onShutter() {
      // TODO Do something when the shutter closes.
    }
  };

  PictureCallback rawCallback = new PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      // TODO Do something with the image RAW data.
    }
  };

  PictureCallback jpegCallback = new PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      // Save the image JPEG data to the SD card
      FileOutputStream outStream = null;
      try {
        String path = Environment.getExternalStorageDirectory() + 
                      "\test.jpg";

        outStream = new FileOutputStream(path);
        outStream.write(data);
        outStream.close();
      } catch (FileNotFoundException e) {
        Log.e(TAG, "File Note Found", e);
      } catch (IOException e) {
        Log.e(TAG, "IO Exception", e);
      }
    }
  };
  
  private void modifyExif() {
    /**
     * Listing 15-28: Reading and modifying EXIF data
     */
    File file = new File(Environment.getExternalStorageDirectory(),
                         "test.jpg");

    try {
      ExifInterface exif = new ExifInterface(file.getCanonicalPath());
      // Read the camera model and location attributes
      String model = exif.getAttribute(ExifInterface.TAG_MODEL);
      Log.d(TAG, "Model: " + model);
      // Set the camera make
      exif.setAttribute(ExifInterface.TAG_MAKE, "My Phone");
    } catch (IOException e) {
      Log.e(TAG, "IO Exception", e);
    }
  }
}