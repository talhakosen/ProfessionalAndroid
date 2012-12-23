package com.paad.gforcemeter;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class ForceMeter extends Activity {

  private SensorManager sensorManager;
  private TextView accelerationTextView;
  private TextView maxAccelerationTextView;
  private float currentAcceleration = 0;
  private float maxAcceleration = 0;
  
  private final double calibration = SensorManager.STANDARD_GRAVITY;
  
  private final SensorEventListener sensorEventListener = new SensorEventListener() {

    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void onSensorChanged(SensorEvent event) {
      double x = event.values[0];
      double y = event.values[1];
      double z = event.values[2];

      double a = Math.round(Math.sqrt(Math.pow(x, 2) +
                                      Math.pow(y, 2) +
                                      Math.pow(z, 2)));
      currentAcceleration = Math.abs((float)(a-calibration));

      if (currentAcceleration > maxAcceleration)
        maxAcceleration = currentAcceleration;
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    accelerationTextView = (TextView)findViewById(R.id.acceleration);
    maxAccelerationTextView = (TextView)findViewById(R.id.maxAcceleration);
    sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

    Sensor accelerometer =
      sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorManager.registerListener(sensorEventListener,
                                   accelerometer,
                                   SensorManager.SENSOR_DELAY_FASTEST);

    Timer updateTimer = new Timer("gForceUpdate");
    updateTimer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        updateGUI();
      }
    }, 0, 100);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorManager.registerListener(sensorEventListener,
                                   accelerometer,
                                   SensorManager.SENSOR_DELAY_FASTEST);
  }

  @Override
  protected void onPause() {
    sensorManager.unregisterListener(sensorEventListener);
    super.onPause();
  }

  private void updateGUI() {
    runOnUiThread(new Runnable() {
      public void run() {
        String currentG = currentAcceleration/SensorManager.STANDARD_GRAVITY
                          + "Gs";
        accelerationTextView.setText(currentG);
        accelerationTextView.invalidate();
        String maxG = maxAcceleration/SensorManager.STANDARD_GRAVITY + "Gs";
        maxAccelerationTextView.setText(maxG);
        maxAccelerationTextView.invalidate();
      }
    });
  };
}