package com.paad.videocamera;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoCameraActivity extends Activity implements SurfaceHolder.Callback {
  
  private static final String TAG = "CameraActivity";
 
  private Camera camera;
  private SurfaceHolder holder;
  private MediaRecorder mediaRecorder;
  
  private void prepareVideoCamera() throws IllegalStateException, IOException {
    // Create a new Media Recorder.
    mediaRecorder = new MediaRecorder();
    
    /**
     * Listing 15-30: Preparing to record audio and video using the Media Recorder
     */
    // Unlock the Camera to allow the Media Recorder to own it.
    camera.unlock();

    // Assign the Camera to the Media Recorder.
    mediaRecorder.setCamera(camera);

    // Configure the input sources.
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

    // Set the recording profile.
    CamcorderProfile profile = null;

    if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P))
      profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
    else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P))
      profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
    else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P))
      profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
    else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_HIGH))
      profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
      
    if (profile != null)
      mediaRecorder.setProfile(profile); 

    // Specify the output file
    mediaRecorder.setOutputFile("/sdcard/myvideorecording.mp4");

    // Prepare to record
    mediaRecorder.prepare();
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    SurfaceView surface = (SurfaceView)findViewById(R.id.surfaceView);
    SurfaceHolder holder = surface.getHolder();
    holder.addCallback(this);
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    holder.setFixedSize(400, 300);
    
    Button record = (Button)findViewById(R.id.buttonRecord);
    Button stabilize = (Button)findViewById(R.id.buttonStabilize);
    Button scanner = (Button)findViewById(R.id.buttonScanner);
    
    record.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startRecording();
      }    
    });
    
    stabilize.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        enableVideoStabilization();
      }    
    });
    
    scanner.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        stopRecording();
      }    
    });
  }
  
  public void surfaceCreated(SurfaceHolder holder) { 
    this.holder = holder;
    try {
      prepareVideoCamera();
    } catch (IllegalStateException e) {
      Log.e(TAG, "Illegal State Exception", e);
    } catch (IOException e) {
      Log.e(TAG, "I/O Exception", e);
    }
  }
  
  public void surfaceDestroyed(SurfaceHolder holder) {
    this.holder = null;
  }
  
  public void surfaceChanged(SurfaceHolder holder, int format, 
                             int width, int height) {
  }

  @Override
  protected void onResume() {
    super.onResume();
    camera = Camera.open();        
    
    /**
     * Listing 15-31: Using the Camera recording hint
     */
    Camera.Parameters parameters = camera.getParameters();
    parameters.setRecordingHint(true);
    camera.setParameters(parameters);
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    // Reset and release the media recorder.
    mediaRecorder.reset();
    mediaRecorder.release();
    camera.lock();
    
    // Release the camera.
    camera.release();
  }
  
  private void stopRecording() {
    /**
     * Listing 15-32: Stopping a video recording
     */
    mediaRecorder.stop();
        
    // Reset and release the media recorder.
    mediaRecorder.reset();
    mediaRecorder.release();
    camera.lock();
    
    try {
      prepareVideoCamera();
    } catch (IllegalStateException e) {
      Log.e(TAG, "Illegal State Exception", e);
    } catch (IOException e) {
      Log.e(TAG, "I/O Exception", e);
    }
  }
  
  private void enableVideoStabilization() {
    /**
     * Listing 15-33: Image stabilization
     */
    Camera.Parameters parameters = camera.getParameters();
    if (parameters.isVideoStabilizationSupported())
      parameters.setVideoStabilization(true);
    camera.setParameters(parameters);
  }

  /**
   * Listing 15-34: Adding files to the Media Store using the Media Scanner
   */
  private void mediaScan(final String filePath) {
    
    MediaScannerConnectionClient mediaScannerClient = new
      MediaScannerConnectionClient() {
    
      private MediaScannerConnection msc = null;

      {
        msc = new MediaScannerConnection(
          VideoCameraActivity.this, this); 
        msc.connect();
      }

      public void onMediaScannerConnected() {
        // Optionally specify a MIME Type, or
        // have the Media Scanner imply one based
        // on the filename.
        String mimeType = null;
        msc.scanFile(filePath, mimeType);
      }

      public void onScanCompleted(String path, Uri uri) {
        msc.disconnect();
        Log.d(TAG, "File Added at: " + uri.toString());
      }
    };
  }
  
  private void startRecording() {
    try {
      mediaRecorder.start();
    } catch (IllegalStateException e) {
      mediaRecorder.release();
      camera.lock();
      Log.d(TAG, "Illegal State Exception", e);
    }
  }
}