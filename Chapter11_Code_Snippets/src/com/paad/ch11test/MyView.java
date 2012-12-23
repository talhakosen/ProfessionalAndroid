/**
 * Stub View containing code snippets for some of the advanced Canvas drawing
 * techniques and user / View interaction handling available in 
 * Android.
 */

package com.paad.ch11test;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

  // Constructor required for in-code creation
  public MyView(Context context) {
    super(context);
  }
 
  //Constructor required for inflation from resource file
  public MyView (Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  // Constructor required for inflation from resource file
  public MyView (Context context, AttributeSet attrs, int defaultStyle) {
    super(context, attrs, defaultStyle );
  }

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
      // Calculate the ideal size of your control within this maximum size.
      // If your control fills the available space return the outer bound.
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
      // Calculate the ideal size of your control within this maximum size.
      // If your control fills the available space return the outer bound.
      result = specSize;
    } else if (specMode == MeasureSpec.EXACTLY) {
      // If your control can fit within these bounds return that value.
      result = specSize;
    }
    return result;
  }
   
  @Override 
  protected void onDraw(Canvas canvas) {
    // Get the size of the control based on the last call to onMeasure.
    int height = getMeasuredHeight();
    int width = getMeasuredWidth();
     
    // Find the center 	
    int px = width/2;
    int py = height/2;
    
    RectF rectf = new RectF(0, 0, width, height);
    
    // ---- Advanced Canvas Drawing 
    Paint myPaint = new Paint();
    myPaint.setColor(Color.RED);
    
    // Translucency
    // Make color red and 50% transparent
    int opacity = 127;
    int intColor = Color.argb(opacity, 255, 0, 0);
    int parsedColor = Color.parseColor("#7FFF0000");
   
    // Make color 50% transparent
    opacity = 127;
    myPaint.setAlpha(opacity);
    
    // Shaders
    Paint shaderPaint = new Paint();
    
    int colorFrom = Color.BLACK;
    int colorTo = Color.WHITE;

    LinearGradient linearGradientShader = new LinearGradient(0, 0, width, height, 
                                                             colorFrom, 
                                                             colorTo, 
                                                             TileMode.MIRROR);

    int[] gradientColors = new int[3];
    gradientColors[0] = Color.GREEN;
    gradientColors[1] = Color.YELLOW;
    gradientColors[2] = Color.RED;

    float[] gradientPositions = new float[3];
    gradientPositions[0] = 0.0f;
    gradientPositions[1] = 0.5f;
    gradientPositions[2] = 1.0f;

    RadialGradient radialGradientShader = new RadialGradient(px, py, 50,
                                                             gradientColors, 
                                                             gradientPositions,
                                                             TileMode.CLAMP);
   
    // Mask Filters
    // Set the direction of the light source
    float[] direction = new float[]{ 1, 1, 1 };
    // Set the ambient light level
    float light = 0.4f;
    // Choose a level of specularity to apply
    float specular = 6;
    // Apply a level of blur to apply to the mask
    float blur = 3.5f;
    EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);

    myPaint.setMaskFilter(emboss);
    
    // Color Filters
    myPaint.setColorFilter(new LightingColorFilter(Color.BLUE, Color.RED));

    // Path Effects
    Paint borderPaint = new Paint();
    borderPaint.setPathEffect(new CornerPathEffect(5));
   
    // XFerMode
    AvoidXfermode avoid = new AvoidXfermode(Color.RED, 10, AvoidXfermode.Mode.AVOID);
    borderPaint.setXfermode(avoid);

    // Antialiasing
    myPaint.setSubpixelText(true);
    myPaint.setAntiAlias(true); 
  }
  
  @Override
  public boolean onKeyDown(int _keyCode, KeyEvent _event) {
    // Perform on key pressed handling
	  return false;
  }

  @Override
  public boolean onKeyUp(int _keyCode, KeyEvent _event) {
    // Perform on key released handling
	  _event.isAltPressed();
	  _event.isShiftPressed();
	  _event.isSymPressed(); 
	  KeyEvent.isModifierKey(_keyCode); 

	  return false;
  }

  @Override
  public boolean onTrackballEvent(MotionEvent _event) {
    float vertical = _event.getY();
    float horizontal = _event.getX();

    // TODO Process trackball movement.

    return false;
  }

  @Override 
  public boolean onTouchEvent(MotionEvent event) {

    int action = event.getAction();

    switch (action) {
      case (MotionEvent.ACTION_MOVE)   : 
      {
        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
          float x = event.getHistoricalX(i);
          float y = event.getHistoricalY(i);
          processMovement(x, y);
        }

        float x = event.getX();
        float y = event.getY();
        processMovement(x, y);

        return true;
      }
    }

    return super.onTouchEvent(event);
  }

  private void processMovement(float _x, float _y) {
    // TODO Do something on movement.
  }
} 