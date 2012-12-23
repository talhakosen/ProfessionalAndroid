package com.paad.speech;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

public class MyActivity extends Activity {
  
  private static final int VOICE_RECOGNITION = 1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    speechInput();
    speechWebSearch();
  }
  
  private void speechInput() {
    /**
     * Listing 11-2: Initiating a speech recognition request
     */
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    // Specify free form input
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "or forever hold your peace");
    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
    startActivityForResult(intent, VOICE_RECOGNITION);
  }
  
  /**
   * Listing 11-3: Finding the results of a speech recognition request
   */
  @Override
  protected void onActivityResult(int requestCode,
                                  int resultCode,
                                  Intent data) {
    if (requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK) {
      ArrayList<String> results;
        
      results = 
        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

      float[] confidence;

      String confidenceExtra = RecognizerIntent.EXTRA_CONFIDENCE_SCORES;
      confidence =
        data.getFloatArrayExtra(confidenceExtra);

      // TODO Do something with the recognized voice strings
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  
  private void speechWebSearch() {
    /**
     * Listing 11-4: Finding the results of a speech recognition request
     */
    Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
    startActivityForResult(intent, 0);

  }

}