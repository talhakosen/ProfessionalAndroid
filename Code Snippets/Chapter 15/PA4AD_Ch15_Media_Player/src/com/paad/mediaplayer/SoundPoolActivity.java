package com.paad.mediaplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SoundPoolActivity extends Activity {

  static final String TAG = "SoundPoolActivity";
  SoundPool sp;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);    
    setContentView(R.layout.soundpool);
  }

  @Override
  protected void onResume() {
    super.onResume();
    
    /**
     * Listing 15-20: Creating a Sound Pool
     */
    int maxStreams = 10;
    sp = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);

    final int track1 = sp.load(this, R.raw.track1, 0);
    final int track2 = sp.load(this, R.raw.track2, 0);
    final int track3 = sp.load(this, R.raw.track3, 0);

       
    Button track1Button = (Button)findViewById(R.id.buttonTrack1);
    Button track2Button = (Button)findViewById(R.id.buttonTrack2);
    Button track3Button = (Button)findViewById(R.id.buttonTrack3);
    Button stopButton = (Button)findViewById(R.id.buttonStop);
    Button chipmunkButton = (Button)findViewById(R.id.buttonChipmunk);
    
    /**
     * Listing 15-21: Controlling playback of Sound Pool audio
     */
    track1Button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        sp.play(track1, 1, 1, 0, -1, 1);
      }
    });

    track2Button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        sp.play(track2, 1, 1, 0, 0, 1);
      }
    });

    track3Button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        sp.play(track3, 1, 1, 0, 0, 0.5f);
      }
    });

    stopButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        sp.stop(track1);
        sp.stop(track2);
        sp.stop(track3);
      }
    });

    chipmunkButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        sp.setRate(track1, 2f);
      }
    });
  }
  
  @Override
  protected void onPause() {
    super.onPause(); 
    sp.release();
  }
}