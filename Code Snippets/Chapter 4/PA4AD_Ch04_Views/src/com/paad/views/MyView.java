package com.paad.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Listing 4-15: Creating a new View
 */
public class MyView extends View {

  // Constructor required for in-code creation
  public MyView(Context context) {
    super(context);
  }

  // Constructor required for inflation from resource file
  public MyView (Context context, AttributeSet ats, int defaultStyle) {
    super(context, ats, defaultStyle );
  }

  //Constructor required for inflation from resource file
  public MyView (Context context, AttributeSet attrs) {
    super(context, attrs);
  }

//  @Override
//  protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
//    int measuredHeight = measureHeight(hMeasureSpec);
//    int measuredWidth = measureWidth(wMeasureSpec);
//
//    // MUST make this call to setMeasuredDimension
//    // or you will cause a runtime exception when
//    // the control is laid out.
//    setMeasuredDimension(measuredHeight, measuredWidth);
//  }
//
//  private int measureHeight(int measureSpec) {
//    int specMode = MeasureSpec.getMode(measureSpec);
//    int specSize = MeasureSpec.getSize(measureSpec);
//
//     // [ ... Calculate the view height ... ]
//
//     return specSize;
//  }
//
//  private int measureWidth(int measureSpec) {
//    int specMode = MeasureSpec.getMode(measureSpec);
//    int specSize = MeasureSpec.getSize(measureSpec);
//
//     // [ ... Calculate the view width ... ]
//
//     return specSize;
//  }
//
//  @Override
//  protected void onDraw(Canvas canvas) {
//    // [ ... Draw your visual interface ... ]
//  }
  
  /**
   * Listing 4-16: Drawing a custom View
   */
  @Override
  protected void onDraw(Canvas canvas) {
    // Get the size of the control based on the last call to onMeasure.
    int height = getMeasuredHeight();
    int width = getMeasuredWidth();

    // Find the center
    int px = width/2;
    int py = height/2;

    // Create the new paint brushes.
    // NOTE: For efficiency this should be done in
    // the views's constructor
    Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setColor(Color.WHITE);

    // Define the string.
    String displayText = "Hello World!";

    // Measure the width of the text string.
    float textWidth = mTextPaint.measureText(displayText);

    // Draw the text string in the center of the control.
    canvas.drawText(displayText, px-textWidth/2, py, mTextPaint);
  }
  
  /**
   * Listing 4-17: A typical View measurement implementation
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredHeight = measureHeight(heightMeasureSpec);
    int measuredWidth = measureWidth(widthMeasureSpec);

    setMeasuredDimension(measuredHeight, measuredWidth);
  }

  private int measureHeight(int measureSpec) {
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    //  Default size if no limits are specified.
    int result = 500;

    if (specMode == MeasureSpec.AT_MOST) {
      // Calculate the ideal size of your
      // control within this maximum size.
      // If your control fills the available
      // space return the outer bound.
      result = specSize;
    } else if (specMode == MeasureSpec.EXACTLY) {
      // If your control can fit within these bounds return that value.
      result = specSize;
    }
    return result;
  }

  private int measureWidth(int measureSpec) {
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    //  Default size if no limits are specified.
    int result = 500;

    if (specMode == MeasureSpec.AT_MOST) {
      // Calculate the ideal size of your control
      // within this maximum size.
      // If your control fills the available space
      // return the outer bound.
      result = specSize;
    } else if (specMode == MeasureSpec.EXACTLY) {
      // If your control can fit within these bounds return that value.
      result = specSize;
    }
    return result;
  }
  
  /**
   * Listing 4-18: Input event handling for Views
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
    // Return true if the event was handled.
    return true;
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
    // Return true if the event was handled.
    return true;
  }

  @Override
  public boolean onTrackballEvent(MotionEvent event ) {
    // Get the type of action this event represents
    int actionPerformed = event.getAction();
    // Return true if the event was handled.
    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    // Get the type of action this event represents
    int actionPerformed = event.getAction();
    // Return true if the event was handled.
    return true;
  }

}