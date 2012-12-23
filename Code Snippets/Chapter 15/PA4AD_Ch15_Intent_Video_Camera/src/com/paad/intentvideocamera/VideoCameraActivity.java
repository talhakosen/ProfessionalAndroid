package com.paad.intentvideocamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;

import com.paad.intentcamera.R;

public class VideoCameraActivity extends Activity {
  
  /**
   * Listing 15-29: Recording video using an Intent
   */
  private static final int RECORD_VIDEO = 0;
    
  private void startRecording() {
    // Generate the Intent.
    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    
    // Launch the camera app.
    startActivityForResult(intent, RECORD_VIDEO);
  }
  
  /**
   * The following code snippets:
   * 
   * Listing 15-30: Preparing to record audio and video using the Media Recorder
   * Listing 15-31: Using the Camera recording hint
   * Listing 15-32: Stopping a video recording
   * Listing 15-33: Image stabilization
   * Listing 15-34: Adding files to the Media Store using the Media Scanner
   * 
   * Have been moved to the VideoCameraActivity in the PA4AD_Ch15_Video_Camera Project
   */

  @Override
  protected void onActivityResult(int requestCode,
                                  int resultCode, Intent data) {
    if (requestCode == RECORD_VIDEO) {
      VideoView videoView = (VideoView)findViewById(R.id.videoView);
      videoView.setVideoURI(data.getData()); 
      videoView.start();
    }
  } 
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
        
    Button videoButton = (Button)findViewById(R.id.buttonVideo);
    videoButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startRecording();
      }
    });
  }
}