package com.paad.chapter3;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setContentView(R.layout.main);
        
        //--
        
        String rString = getString(R.string.stop_message);
        String fString = String.format(rString, "Collaborate and listen.");
        CharSequence styledString = Html.fromHtml(fString);
        // TEST
        TextView tv = (TextView)findViewById(R.id.myTextView);
        tv.setText(styledString);
        
        //--
        
        // Inflate a layout resource.
        setContentView(R.layout.main);
        
        //--

        Resources myResources = getResources();
        CharSequence styledText = myResources.getText(R.string.stop_message);
        Drawable icon = myResources.getDrawable(R.drawable.icon);
        int opaqueBlue = myResources.getColor(R.color.opaque_blue);
        float borderWidth = myResources.getDimension(R.dimen.standard_border);
        Animation tranOut = AnimationUtils.loadAnimation(this, R.anim.spin_shrink_fade);
        String[] stringArray = myResources.getStringArray(R.array.default_todo_items);
        int[] intArray = myResources.getIntArray(R.array.countdown);

        AnimationDrawable rocket = (AnimationDrawable)myResources.getDrawable(R.drawable.frame_by_frame);
        
        
        //--

        // Display a transient dialog box that displays the error message string resource.
        Toast.makeText(this, R.string.app_error, Toast.LENGTH_LONG).show();
        
        
        CharSequence httpError = getString(android.R.string.httpErrorBadUrl);
    }
    
    @Override 
    public void onConfigurationChanged(Configuration _newConfig) {
      super.onConfigurationChanged(_newConfig);  

      //[ ... Update any UI based on resource values ... ]

      if (_newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //[ ... React to different orientation ... ]
      }

      if (_newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
        //[ ... React to changed keyboard visibility ... ]
      }
    }

    // Called after onCreate has finished, use to restore UI state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState. This bundle has also 
      // been passed to onCreate.
    }

    // Called before subsequent visible lifetimes for an activity process.
    @Override
    public void onRestart(){
      super.onRestart();
      // Load changes knowing that the activity has already
      // been visible within this process.
    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart(){
      super.onStart();
      // Apply any required UI change now that the Activity is visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume(){
      super.onResume();
      // Resume any paused UI updates, threads, or processes required
      // by the activity but suspended when it was inactive.
    }

    // Called to save UI state changes at the end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {    
      // Save UI state changes to the savedInstanceState. This bundle will be 
      // passed to onCreate if the process is killed and restarted.
      super.onSaveInstanceState(savedInstanceState);
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause(){
      // Suspend UI updates, threads, or CPU intensive processes that don’t
      // need to be updated when the Activity isn’t the active foreground activity.
      super.onPause();
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop(){    
      // Suspend remaining UI updates, threads, or processing that aren’t required
      // when the Activity isn’t visible. Persist all edits or state changes 
      // as after this call the process is likely to be killed.
      super.onStop();
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy(){
      // Clean up any resources including ending threads, 
      //closing database connections etc.
      super.onDestroy();
    }
}