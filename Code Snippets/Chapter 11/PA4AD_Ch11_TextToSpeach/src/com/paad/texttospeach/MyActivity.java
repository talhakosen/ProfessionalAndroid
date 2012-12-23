package com.paad.texttospeach;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MyActivity extends Activity {
  
  /**
   * Listing 11-1: Using Text-to-Speech
   */
  private static int TTS_DATA_CHECK = 1;

  private TextToSpeech tts = null;
  private boolean ttsIsInit = false;

  private void initTextToSpeech() {
    Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA);
    startActivityForResult(intent, TTS_DATA_CHECK);
  }

  protected void onActivityResult(int requestCode,
                                  int resultCode, Intent data) {
    if (requestCode == TTS_DATA_CHECK) {
      if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
        tts = new TextToSpeech(this, new OnInitListener() {
         public void onInit(int status) {
           if (status == TextToSpeech.SUCCESS) {
             ttsIsInit = true;
             if (tts.isLanguageAvailable(Locale.UK) >= 0)
               tts.setLanguage(Locale.UK);
             tts.setPitch(0.8f);
             tts.setSpeechRate(1.1f);
             speak(); 
           }
         }
        });
      } else {
        Intent installVoice = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installVoice);
      }
    }
  }

  private void speak() {
    if (tts != null && ttsIsInit) {
      tts.speak("Hello, Android", TextToSpeech.QUEUE_ADD, null);
    }
  }

  @Override
  public void onDestroy() {
    if (tts != null) {
      tts.stop();
      tts.shutdown();
    }
    super.onDestroy();
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initTextToSpeech();
  }
}