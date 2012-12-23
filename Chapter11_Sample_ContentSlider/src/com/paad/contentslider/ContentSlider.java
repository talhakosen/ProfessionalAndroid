package com.paad.contentslider;

import android.app.Activity;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ContentSlider extends Activity {

  enum TextPosition { UpperLeft, Top, UpperRight,
    Left, Center, Right, 
    LowerLeft, Bottom, LowerRight};

	Animation slideInLeft;
	Animation slideOutLeft;
	Animation slideInRight;
	Animation slideOutRight;
	Animation slideInTop;
	Animation slideOutTop;
	Animation slideInBottom;
	Animation slideOutBottom;
	
	TextView myTextView;                    
  TextPosition textPosition = TextPosition.Center;
                      
	@Override
	public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
	  setContentView(R.layout.main);

	  myTextView = (TextView)findViewById(R.id.myTextView);
	  
	  slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
	  slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
	  slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
	  slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
	  slideInTop = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
	  slideOutTop = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
	  slideInBottom = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
	  slideOutBottom = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out); 
	}

	private void applyAnimation(Animation _out, Animation _in, String _newText) {
	  final String text = _newText;
	  final Animation in = _in;

	  // Ensure the text stays out of screen when the slide-out 
	  // animation has completed.
	  _out.setFillAfter(true);

	  // Create a listener to wait for the slide-out animation to complete.
	  _out.setAnimationListener(new AnimationListener() {
	    public void onAnimationEnd(Animation _animation) {
	      // Change the text
	      myTextView.setText(text);
	      // Slide it back in to view
	      myTextView.startAnimation(in);
	    }

	    public void onAnimationRepeat(Animation _animation) {}

	    public void onAnimationStart(Animation _animation) {}
	  });

	  // Apply the slide-out animation
	  myTextView.startAnimation(_out);
	}

	private void movePosition(TextPosition _current, TextPosition _directionPressed) {
	  Animation in;
	  Animation out;
	  TextPosition newPosition;

	  if (_directionPressed == TextPosition.Left){
	    in = slideInLeft;
	    out = slideOutLeft;
	  }
	  else if (_directionPressed == TextPosition.Right){
	    in = slideInRight;
	    out = slideOutRight;
	  }
	  else if (_directionPressed == TextPosition.Top){
	    in = slideInTop;
	    out = slideOutTop;
	  }
	  else {//if (_directionPressed == TextPosition.Bottom){
	    in = slideInBottom;
	    out = slideOutBottom;
	  }

	  int newPosValue = _current.ordinal();
	  int currentValue = _current.ordinal();

	  // To simulate the effect of 'tilting' the device moving in one
	  // direction should make text for the opposite direction appear.
	  // Ie. Tilting right should make left appear.
	  if (_directionPressed == TextPosition.Bottom)
	    newPosValue = currentValue - 3;
	  else if (_directionPressed == TextPosition.Top)
	    newPosValue = currentValue + 3;
	  else if (_directionPressed == TextPosition.Right) {
	    if (currentValue % 3 != 0)
	      newPosValue = currentValue - 1;
	  }
	  else if (_directionPressed == TextPosition.Left) {
	    if ((currentValue+1) % 3 != 0)
	      newPosValue = currentValue + 1;
	  }

	  if (newPosValue != currentValue && 
	      newPosValue > -1 &&
	      newPosValue < 9){
	      newPosition = TextPosition.values()[newPosValue];

	    applyAnimation(out, in, newPosition.toString());
	    textPosition = newPosition;
	  }
	}

  @Override 
	public boolean onKeyDown(int _keyCode, KeyEvent _event) {
	  if (super.onKeyDown(_keyCode, _event))
	    return true;

	  if (_event.getAction() == KeyEvent.ACTION_DOWN){
	    switch (_keyCode) {
	      case (KeyEvent.KEYCODE_DPAD_LEFT): 
	        movePosition(textPosition, TextPosition.Left); return true;
	      case (KeyEvent.KEYCODE_DPAD_RIGHT): 
	        movePosition(textPosition, TextPosition.Right); return true;
	      case (KeyEvent.KEYCODE_DPAD_UP): 
	        movePosition(textPosition, TextPosition.Top); return true;
	      case (KeyEvent.KEYCODE_DPAD_DOWN): 
	        movePosition(textPosition, TextPosition.Bottom); return true;
	    }
	  }
	  return false;
	}
}