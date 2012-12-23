package com.paad.mediaplayer;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioPlayerActivity extends Activity {

  static final String TAG = "PlayerActivity";
  
  private ActivityMediaControlReceiver activityMediaControlReceiver;
  private MediaPlayer mediaPlayer;
  private RemoteControlClient myRemoteControlClient;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.audioplayer);

    /**
     * Listing 15-6: Setting the volume control stream for an Activity
     */
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

    registerRemoteControlClient();
    configureAudio();
    
    Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
    buttonPlay.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        play();
      }
    });
    
    Button buttonBass = (Button)findViewById(R.id.buttonBassBoost);
    buttonBass.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        bassBoost();
      }
    });
  }
  
  /**
   * Listing 15-9: Media button press Broadcast Receiver implementation
   */
  public class ActivityMediaControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (MediaControlReceiver.ACTION_MEDIA_BUTTON.equals(
          intent.getAction())) {
        KeyEvent event =
          (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
      
        switch (event.getKeyCode()) {
          case (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) : 
            if (mediaPlayer.isPlaying())
              pause();
            else
              play();
            break;
          case (KeyEvent.KEYCODE_MEDIA_PLAY) : 
            play(); break;
          case (KeyEvent.KEYCODE_MEDIA_PAUSE) : 
            pause(); break;
          case (KeyEvent.KEYCODE_MEDIA_NEXT) : 
            skip(); break;
          case (KeyEvent.KEYCODE_MEDIA_PREVIOUS) : 
            previous(); break;
          case (KeyEvent.KEYCODE_MEDIA_STOP) : 
            stop(); break;
          default: break;
        }
      }
    }
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    
    /**
     * Listing 15-10: Media button press Receiver manifest declaration
     */
    // Register the Media Button Event Receiver to 
    // listen for media button presses.
    AudioManager am =
      (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    ComponentName component = 
      new ComponentName(this, MediaControlReceiver.class);

    am.registerMediaButtonEventReceiver(component);

    // Register a local Intent Receiver that receives media button
    // presses from the Receiver registered in the manifest.
    activityMediaControlReceiver = new ActivityMediaControlReceiver();
    IntentFilter filter = 
      new IntentFilter(MediaControlReceiver.ACTION_MEDIA_BUTTON);

    registerReceiver(activityMediaControlReceiver, filter);
    //
    
    IntentFilter noiseFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    registerReceiver(new NoisyAudioStreamReceiver(), noiseFilter);
  }
  
  public void play() {    
    /**
     * Listing 15-11: Requesting the audio focus
     */
    AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    // Request audio focus for playback
    int result = am.requestAudioFocus(focusChangeListener,
                   // Use the music stream.
                   AudioManager.STREAM_MUSIC,
                   // Request permanent focus.
                   AudioManager.AUDIOFOCUS_GAIN);
       
    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
      mediaPlayer.start();
    }
    
    //
    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
      myRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
  }
  
  /**
   * Listing 15-12: Responding to the loss of audio focus
   */
  private OnAudioFocusChangeListener focusChangeListener = 
    new OnAudioFocusChangeListener() {
    
    public void onAudioFocusChange(int focusChange) {
      AudioManager am = 
        (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      
      switch (focusChange) {
        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
          // Lower the volume while ducking.
          mediaPlayer.setVolume(0.2f, 0.2f);
          break;

        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) : 
          pause();
          break;

        case (AudioManager.AUDIOFOCUS_LOSS) :
          stop();
          ComponentName component = 
            new ComponentName(AudioPlayerActivity.this,
              MediaControlReceiver.class);
          am.unregisterMediaButtonEventReceiver(component);
          break;

        case (AudioManager.AUDIOFOCUS_GAIN) : 
          // Return the volume to normal and resume if paused.
          mediaPlayer.setVolume(1f, 1f);
          mediaPlayer.start();
          break;

        default: break;
      }
    }
  };
  
  public void stop() {
    mediaPlayer.stop();
    myRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
    
    /**
     * Listing 15-13: Abandoning audio focus
     */
    AudioManager am = 
      (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    am.abandonAudioFocus(focusChangeListener);
  }
  
  /**
   * Listing 15-14: Pausing output when the headset is disconnected
   */
  private class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals
        (intent.getAction())) {
        pause();
      }
    }
  }
  
  private void registerRemoteControlClient() {
    /**
     * Listing 15-15: Registering a Remote Control Client
     */
    AudioManager am = 
      (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    // Create a Pending Intent that will broadcast the 
    // media button press action. Set the target component 
    // to your Broadcast Receiver. 
    Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
    ComponentName component = 
      new ComponentName(this, MediaControlReceiver.class);

    mediaButtonIntent.setComponent(component);
    PendingIntent mediaPendingIntent =
       PendingIntent.getBroadcast(getApplicationContext(), 0,    
                                  mediaButtonIntent, 0);

    // Create a new Remote Control Client using the
    // Pending Intent and register it with the
    // Audio Manager
    myRemoteControlClient = 
      new RemoteControlClient(mediaPendingIntent);

    am.registerRemoteControlClient(myRemoteControlClient);
    
    /**
     * Listing 15-16: Configuring the Remote Control Client playback controls
     */
    myRemoteControlClient.setTransportControlFlags(
      RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE|
      RemoteControlClient.FLAG_KEY_MEDIA_STOP);

  }

  private void setRemoteControlMetadata(Bitmap artwork, 
      String album, String artist, long trackNumber) {
    /**
     * Listing 15-17: Applying changes to the Remote Control Client metadata
     */
    MetadataEditor editor = myRemoteControlClient.editMetadata(false);

    editor.putBitmap(MetadataEditor.BITMAP_KEY_ARTWORK, artwork);
    editor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, album);
    editor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, artist);
    editor.putLong(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER, 
                   trackNumber);

    editor.apply();
  }
  
  private void bassBoost() {
    /**
     * Listing 15-22: Applying audio effects
     */
    int sessionId = mediaPlayer.getAudioSessionId();
    short boostStrength = 500;
    int priority = 0;

    BassBoost bassBoost = new BassBoost (priority, sessionId);
    bassBoost.setStrength(boostStrength);
    bassBoost.setEnabled(true);
  }
  
  private void configureAudio() {
    try {      
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setDataSource("/sdcard/test.mp3");
      mediaPlayer.prepare();
      
      // TODO setRemoteControlMetadata();
      
      mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
          AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
          am.abandonAudioFocus(focusChangeListener);
        }
      });
    } catch (IllegalArgumentException e) {
      Log.d(TAG, "Illegal Argument Exception: " + e.getMessage());
    } catch (SecurityException e) {
      Log.d(TAG, "Security Exception: " + e.getMessage());
    } catch (IllegalStateException e) {
      Log.d(TAG, "Illegal State Exception: " + e.getMessage());
    } catch (IOException e) {
      Log.d(TAG, "IO Exception: " + e.getMessage());
    }
  }
  
  public void pause() {
    mediaPlayer.pause();
    myRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
  }
  
  public void skip() {
    // TODO Move to the next audio file.
    // TODO Use setRemoteControlMetadata to update the remote control metadata.
  }
  
  public void previous() {
    // TODO Move to the previous audio file.
    // TODO Use setRemoteControlMetadata to update the remote control metadata.
  }
}