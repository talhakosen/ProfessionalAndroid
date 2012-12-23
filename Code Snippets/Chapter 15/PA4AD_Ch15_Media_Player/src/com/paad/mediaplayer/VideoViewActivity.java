package com.paad.mediaplayer;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {

  static final String TAG = "PlayerActivity";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.videoviewer);
     
    configureVideoView();
  }
  
  private void prepareMediaPlayer() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
    /**
     * Listing 15-1: Audio playback using the Media Player
     */
    MediaPlayer mediaPlayer = new MediaPlayer();
    mediaPlayer.setDataSource("/sdcard/mydopetunes.mp3");
    mediaPlayer.prepare();
  }
  
  private void configureVideoView() {
    /**
     * Listing 15-2: Video playback using a Video View
     */
     // Get a reference to the Video View.
     final VideoView videoView = (VideoView)findViewById(R.id.videoView);
    
     // Configure the video view and assign a source video.
     videoView.setKeepScreenOn(true);
     videoView.setVideoPath("/sdcard/mycatvideo.3gp");
    
     // Attach a Media Controller
     MediaController mediaController = new MediaController(this);
     videoView.setMediaController(mediaController); 
  }
}