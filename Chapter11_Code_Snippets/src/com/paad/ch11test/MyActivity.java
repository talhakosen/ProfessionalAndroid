package com.paad.ch11test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends Activity {
  
  private static String MY_ACTION = "com.paad.MY_ACTION";
  private static String REQUIRED_PERMISSION = "com.paad.REQUIRED_PERMISSION";
  
  AnimationSet animationSet;
  TextView myView;
  ViewGroup viewGroup;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Setting the theme in code
    setTheme(android.R.style.Theme_Translucent);
    
    setContentView(R.layout.main);
    myView = (TextView)findViewById(R.id.myView);

    // ---- Broadcast Intent Permissions
    Intent myIntent = new Intent(MY_ACTION);
    sendBroadcast(myIntent, REQUIRED_PERMISSION);
      
    // ---- Tweened animations
    // Create the AnimationSet
    animationSet = new AnimationSet(true);

    // Create a rotate animation.
    RotateAnimation rotate = new RotateAnimation(0, 360, 
                                                 RotateAnimation.RELATIVE_TO_SELF,0.5f, 
                                                 RotateAnimation.RELATIVE_TO_SELF,0.5f);
    rotate.setFillAfter(true);
    rotate.setDuration(1000);

    // Create a scale animation
    ScaleAnimation scale = new ScaleAnimation(1, 0,
                                              1, 0,
                                              ScaleAnimation.RELATIVE_TO_SELF, 0.5f, 
                                              ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
    scale.setFillAfter(true);
    scale.setDuration(500);
    scale.setStartOffset(500);

    // Create an alpha animation
    AlphaAnimation alpha = new AlphaAnimation(1, 0);
    scale.setFillAfter(true);
    scale.setDuration(500);
    scale.setStartOffset(500);

    // Add each animation to the set
    animationSet.addAnimation(rotate);
    animationSet.addAnimation(scale);
    animationSet.addAnimation(alpha);
    
    animationSet.setRepeatMode(Animation.RESTART);
    animationSet.setRepeatCount(Animation.INFINITE);

    animationSet.setAnimationListener(new AnimationListener() {
      public void onAnimationEnd(Animation _animation) {
        // TODO Do something after animation is complete.
      }

      public void onAnimationRepeat(Animation _animation) {
        // TODO Do something when the animation repeats.
      }

      public void onAnimationStart(Animation _animation) {
        // TODO Do something when the animation starts.
      }
    });

    viewGroup = (ViewGroup)findViewById(R.id.layout);    
    Button button = (Button)findViewById(R.id.button);
    
    button.setOnClickListener(new OnClickListener() {    	
      public void onClick(View view) {
			  viewGroup.setLayoutAnimationListener(new AnimationListener() {
				  public void onAnimationEnd(Animation _animation) {
  				  MyActivity.this.myView.startAnimation(MyActivity.this.animationSet);
	  		  }
				  
		  	  public void onAnimationRepeat(Animation _animation) {}
				  
			  	public void onAnimationStart(Animation _animation) {}
		    });
			  viewGroup.scheduleLayoutAnimation();		
		  }      
    });                 

    ImageView image = (ImageView)findViewById(R.id.my_animation_frame);
    image.setBackgroundResource(R.drawable.animated_color);    
    AnimationDrawable animation = (AnimationDrawable)image.getBackground();
    animation.start();

    // ---- User interaction
    
    myView.setOnTouchListener(new OnTouchListener() {
  	  public boolean onTouch(View _view, MotionEvent _event) {
  	    // TODO Respond to motion events
      	return false;
      }
   	}); 
 
    myView.setOnKeyListener(new OnKeyListener() {
  	  public boolean onKey(View _v, int _keyCode, KeyEvent _event)
  	  {
  	    // TODO Process key press event
        return false;
	    }
	  });
  }
}