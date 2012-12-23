package com.paad.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class MyActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
   
    String service_name = Context.SENSOR_SERVICE;
    SensorManager sensorManager = (SensorManager)getSystemService(service_name);

    registerProximitySensor(sensorManager);
    findScreenOrientation();
    registerAccelerometer();
    registerAccelerometerAndMagnetometer(sensorManager);
  }
  
  /**
   * Listing 12-1: Sensor Event Listener skeleton code 
   */
  final SensorEventListener mySensorEventListener = new SensorEventListener() {
    public void onSensorChanged(SensorEvent sensorEvent) {
      // TODO Monitor Sensor changes.
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
      // TODO React to a change in Sensor accuracy.
    }
  };
  
  private void registerProximitySensor(SensorManager sensorManager) {
    /**
     * Listing 12-2: Registering a Sensor Event Listener
     */
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    sensorManager.registerListener(mySensorEventListener,
                                   sensor,
                                   SensorManager.SENSOR_DELAY_NORMAL);

  }
  
  private void findScreenOrientation() {
    /**
     * Listing 12-3: Finding the screen orientation relative to the natural orientation 
     */
    String windowSrvc = Context.WINDOW_SERVICE;
    WindowManager wm = ((WindowManager) getSystemService(windowSrvc));
    Display display = wm.getDefaultDisplay();
    int rotation = display.getRotation();
    switch (rotation) {
      case (Surface.ROTATION_0) : break; // Natural
      case (Surface.ROTATION_90) : break; // On its left side
      case (Surface.ROTATION_180) : break; // Updside down
      case (Surface.ROTATION_270) : break; // On its right side
      default: break;
    }
  }
  
  private void registerAccelerometer() {
    /**
     * Listing 12-4: Listening to changes to the default accelerometer 
     */
    SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    int sensorType = Sensor.TYPE_ACCELEROMETER;
    sm.registerListener(mySensorEventListener,
                        sm.getDefaultSensor(sensorType),
                        SensorManager.SENSOR_DELAY_NORMAL);
  }
  
  /**
   * Listing 12-5: Monitoring the accelerometer and magnetometer
   */
  private float[] accelerometerValues;
  private float[] magneticFieldValues;

  final SensorEventListener myAccelerometerListener = new SensorEventListener() {
    public void onSensorChanged(SensorEvent sensorEvent) {
      if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        accelerometerValues = sensorEvent.values;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
  };

  final SensorEventListener myMagneticFieldListener = new SensorEventListener() {
    public void onSensorChanged(SensorEvent sensorEvent) {
      if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        magneticFieldValues = sensorEvent.values;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
  };
  
  private void registerAccelerometerAndMagnetometer(SensorManager sm) {
    Sensor aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    Sensor mfSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    sm.registerListener(myAccelerometerListener,
                        aSensor,
                        SensorManager.SENSOR_DELAY_UI);

    sm.registerListener(myMagneticFieldListener,
                        mfSensor,
                        SensorManager.SENSOR_DELAY_UI);
  }
  
  private void calculateOrientation() {
    /**
     * Listing 12-6: Finding the current orientation using the accelerometer and magnetometer 
     */
    float[] values = new float[3];
    float[] R = new float[9];
    SensorManager.getRotationMatrix(R, null,
                                    accelerometerValues,
                                    magneticFieldValues);
    SensorManager.getOrientation(R, values);

    // Convert from radians to degrees if preferred.
    values[0] = (float) Math.toDegrees(values[0]); // Azimuth
    values[1] = (float) Math.toDegrees(values[1]); // Pitch
    values[2] = (float) Math.toDegrees(values[2]); // Roll
  }
  
  private void calculateRemappedOrientation() {
    float[] inR = new float[9];
    float[] outR = new float[9];
    float[] values = new float[3];

    /**
     * Listing 12-7: Remapping the orientation reference frame based on the natural orientation of the device 
     */
    // Determine the current orientation relative to the natural orientation
    String windoSrvc = Context.WINDOW_SERVICE;
    WindowManager wm = ((WindowManager) getSystemService(windoSrvc));
    Display display = wm.getDefaultDisplay();
    int rotation = display.getRotation();

    int x_axis = SensorManager.AXIS_X; 
    int y_axis = SensorManager.AXIS_Y;

    switch (rotation) {
      case (Surface.ROTATION_0): break;
      case (Surface.ROTATION_90):  
        x_axis = SensorManager.AXIS_Y; 
        y_axis = SensorManager.AXIS_MINUS_X; 
        break;
      case (Surface.ROTATION_180): 
        y_axis = SensorManager.AXIS_MINUS_Y; 
        break;
      case (Surface.ROTATION_270): 
        x_axis = SensorManager.AXIS_MINUS_Y; 
        y_axis = SensorManager.AXIS_X; 
        break;
      default: break;
    }

    SensorManager.remapCoordinateSystem(inR, x_axis, y_axis, outR);    

    // Obtain the new, remapped, orientation values.
    SensorManager.getOrientation(outR, values);
  }
  
  private void deprecatedSensorListener() {
    /**
     * Listing 12-8: Determining orientation using the deprecated orientation Sensor 
     */
    SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    int sensorType = Sensor.TYPE_ORIENTATION;
    sm.registerListener(myOrientationListener,
                        sm.getDefaultSensor(sensorType),
                        SensorManager.SENSOR_DELAY_NORMAL);
  }
  
  final SensorEventListener myOrientationListener = new SensorEventListener() {
    public void onSensorChanged(SensorEvent sensorEvent) {
      if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
        float headingAngle = sensorEvent.values[0];
        float pitchAngle =  sensorEvent.values[1];
        float rollAngle = sensorEvent.values[2];
        // TODO Apply the orientation changes to your application.
      }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
  };

  /**
   * Listing 12-9: Calculating an orientation change using the gyroscope Sensor 
   */
  final float nanosecondsPerSecond = 1.0f / 1000000000.0f;
  private long lastTime = 0;
  final float[] angle = new float[3];
    
  SensorEventListener myGyroListener = new SensorEventListener() {
    public void onSensorChanged(SensorEvent sensorEvent) {
      if (lastTime != 0) {
        final float dT = (sensorEvent.timestamp - lastTime) *
                         nanosecondsPerSecond;
        angle[0] += sensorEvent.values[0] * dT;
        angle[1] += sensorEvent.values[1] * dT;
        angle[2] += sensorEvent.values[2] * dT;
      }
      lastTime = sensorEvent.timestamp;
    }
   
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
  };
      
  private void registerGyro() {
    SensorManager sm 
      = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    int sensorType = Sensor.TYPE_GYROSCOPE;
    sm.registerListener(myGyroListener,
                        sm.getDefaultSensor(sensorType),
                        SensorManager.SENSOR_DELAY_NORMAL);
  }
  
  private void calculatingAltitude() {
    /**
     * Listing 12-10: Finding the current altitude using the barometer Sensor 
     */
    final SensorEventListener myPressureListener = new SensorEventListener() {
      public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
          float currentPressure = sensorEvent.values[0];
              
          // Calculate altitude
          float altitude = SensorManager.getAltitude(
            SensorManager.PRESSURE_STANDARD_ATMOSPHERE, 
            currentPressure);
        }
      }

      public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    SensorManager sm 
      = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    int sensorType = Sensor.TYPE_PRESSURE;
    sm.registerListener(myPressureListener,
                        sm.getDefaultSensor(sensorType),
                        SensorManager.SENSOR_DELAY_NORMAL);

  }
}