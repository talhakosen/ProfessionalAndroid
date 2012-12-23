package com.paad.chapter10;

import java.io.File;
import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.provider.MediaStore.Audio;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

public class MyActivity extends Activity {

  private String MEDIA_FILE_PATH = Settings.System.DEFAULT_RINGTONE_URI.toString();
	Camera camera;
	
  @Override    
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // --- Media Player
    MediaPlayer mpRes = MediaPlayer.create(getApplicationContext(), R.raw.my_sound);
    mpRes.start();
    mpRes.stop();
    
    MediaPlayer mpFile = new MediaPlayer();
    try {
      mpFile.setDataSource(MEDIA_FILE_PATH);
      mpFile.setLooping(false);
      mpFile.prepare();
      mpFile.start();
      mpFile.stop();
    } 
    catch (IllegalArgumentException e) {} 
    catch (IllegalStateException e) {}
    catch (IOException e) {}	

    // ---- Media Recording
    File audioFile = null;

    File sampleDir = Environment.getExternalStorageDirectory();
    if (!sampleDir.canWrite()) // Workaround for broken sdcard support on the device.
      sampleDir = new File("/sdcard/sdcard"); 
    
    try {
        audioFile = File.createTempFile("myoutputfile", ".amr", sampleDir);
    } catch (IOException e) { 
  	  Log.i("File creation failed", e.getMessage());
    }
	
    MediaRecorder mediaRecorder = new MediaRecorder();

    // Set the audio source.
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// Set the output format.
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		// Set the audio encoders to use.
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		
		mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
		mediaRecorder.prepare();
		try {
		  mediaRecorder.start();	
		  mediaRecorder.stop();
		}
		catch (Exception ex) {
			Log.i("Audio Recording Failed", ex.getMessage());
		}

    //  -- Record to Media Store
		ContentValues content = new ContentValues();
		content.put(Audio.AudioColumns.TITLE, "TheSoundandtheFury");
		content.put(Audio.AudioColumns.DATE_ADDED, System.currentTimeMillis() / 1000);		
		content.put(Audio.Media.MIME_TYPE, "audio/amr");
		content.put(Audio.Media.DATA, audioFile.getAbsolutePath());

		ContentResolver resolver = getContentResolver();
		Uri uri = resolver.insert(Audio.Media.INTERNAL_CONTENT_URI, content);

		if (uri != null) {
		  mediaRecorder.setOutputFile(uri.toString());
		  mediaRecorder.prepare();
		  mediaRecorder.start();
		  mediaRecorder.stop();
		}
		
    // Notify those applications such as Music listening to the 
    // scanner events that a recorded audio file just created. 
    // After this braodcast is received by the Music app, you can play the 
    // newly recorded file from the Playlists.
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

    // ---- Using the Camera
		
		camera = Camera.open();
	  //[ ... Do things with the camera ... ]
			
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		  //[ ... Apply changes to the parameters ... ]
		
		camera.setParameters(parameters);
		
		//camera.setPreviewDisplay(mySurface);
		camera.startPreview();
		//[ ... ]
		camera.stopPreview();

		camera.setPreviewCallback(new PreviewCallback() {		  
		  public void onPreviewFrame(byte[] _data, Camera _camera) {
        // TODO Do something with the preview image.
      }
    });

		takePicture();
		
		camera.release();
		
		// ---- Using the Sensors

		// Generic Sensor Monitoring
		String service_name = Context.SENSOR_SERVICE;
		SensorManager sensorManager = (SensorManager)getSystemService(service_name);
		
		SensorListener mySensorListener = new SensorListener() {
  	  public void onSensorChanged(int sensor, float[] values) {
        // TODO Deal with sensor value changes
      }

      public void onAccuracyChanged(int sensor, int accuracy) {
        // TODO React to sensor accuracy change
      }
    };

    sensorManager.registerListener(mySensorListener, SensorManager.SENSOR_TRICORDER);

		// Accelerometer
		SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);		

		SensorListener myAccelSensorListener = new SensorListener() {
		  public void onSensorChanged(int sensor, float[] values) {
		    if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
		      float xAxis_lateralA = values[SensorManager.DATA_X];
		      float yAxis_longitudinalA = values[SensorManager.DATA_Y];
		      float zAxis_verticalA = values[SensorManager.DATA_Z];
		      
		      float raw_xAxis_lateralA = values[SensorManager.RAW_DATA_X];
		      float raw_yAxis_longitudinalA= values[SensorManager.RAW_DATA_Y];
		      float raw_zAxis_verticalA = values[SensorManager.RAW_DATA_Z];

		      // TODO Apply the acceleration changes to your application.
		    }
		  }

      public void onAccuracyChanged(int sensor, int accuracy) { }
		};
		
    sm.registerListener(myAccelSensorListener, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_UI);

		// Orientation	
		SensorListener myOrientationListener = new SensorListener() {
		  public void onSensorChanged(int sensor, float[] values) {
		    if (sensor == SensorManager.SENSOR_ORIENTATION) {
		      float headingAngle = values[0];
		      float pitchAngle = values[1];
		      float rollAngle =  values[2];
		      // TODO Apply the orientation changes to your application.
		    }
		  }

      public void onAccuracyChanged(int sensor, int accuracy) { }
		};

		sm.registerListener(myOrientationListener, SensorManager.SENSOR_ORIENTATION);
				
		// ---- Using Telephony
				
		String action;
		action = Intent.ACTION_CALL;
		action = Intent.ACTION_DIAL;
		
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1234567"));
		startActivity(intent);
				
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

		PhoneStateListener phoneStateListener = new PhoneStateListener() {
		  public void onCallForwardingIndicatorChanged(boolean cfi) {}
		  public void onCallStateChanged(int state, String incomingNumber) {}
		  public void onCellLocationChanged(CellLocation location) {}
		  public void onDataActivity(int direction) {}
		  public void onDataConnectionStateChanged(int state) {}
		  public void onMessageWaitingIndicatorChanged(boolean mwi) {}
		  public void onServiceStateChanged(ServiceState serviceState) {}
		  public void onSignalStrengthChanged(int asu) {}
		};

		telephonyManager.listen(phoneStateListener,
                            PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                            PhoneStateListener.LISTEN_CALL_STATE |
                            PhoneStateListener.LISTEN_CELL_LOCATION |
                            PhoneStateListener.LISTEN_DATA_ACTIVITY |
                            PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                            PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                            PhoneStateListener.LISTEN_SERVICE_STATE |
                            PhoneStateListener.LISTEN_SIGNAL_STRENGTH);

		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
				
		PhoneStateListener callStateListener = new PhoneStateListener() {
		  public void onCallStateChanged(int state, String incomingNumber) {
			  switch (state) {
			    case TelephonyManager.CALL_STATE_IDLE : break;
			    case TelephonyManager.CALL_STATE_RINGING : break;
			    case TelephonyManager.CALL_STATE_OFFHOOK : break;
			    default: break;
			  }
		    // TODO React to call
		  }
		};

		telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		PhoneStateListener cellLocationListener = new PhoneStateListener() {
		  public void onCellLocationChanged(CellLocation location) {
		    GsmCellLocation gsmLocation = (GsmCellLocation)location;
		    Toast.makeText(getApplicationContext(), String.valueOf(gsmLocation.getCid()), Toast.LENGTH_LONG).show();
		  }
		};

		telephonyManager.listen(cellLocationListener, PhoneStateListener.LISTEN_CELL_LOCATION);
						
		PhoneStateListener serviceStateListener = new PhoneStateListener() {
		  public void onServiceStateChanged(ServiceState serviceState) {
			    if (serviceState.getState() == ServiceState.STATE_IN_SERVICE) {
		      String toastText = serviceState.getOperatorAlphaLong();
		      Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
		    }
		  }
		};

    telephonyManager.listen(serviceStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);

		PhoneStateListener dataStateListener = new PhoneStateListener() {
		  public void onDataActivity(int direction) {
		    switch (direction) {
		      case TelephonyManager.DATA_ACTIVITY_IN : break;
		      case TelephonyManager.DATA_ACTIVITY_OUT : break;
		      case TelephonyManager.DATA_ACTIVITY_INOUT : break;
		      case TelephonyManager.DATA_ACTIVITY_NONE : break;
		    }
		  }

		  public void onDataConnectionStateChanged(int state) {
		    switch (state) {
		      case TelephonyManager.DATA_CONNECTED : break;
		      case TelephonyManager.DATA_CONNECTING : break;
		      case TelephonyManager.DATA_DISCONNECTED : break;
		      case TelephonyManager.DATA_SUSPENDED : break;
		    }
		  }
		};

		telephonyManager.listen(dataStateListener,
		                        PhoneStateListener.LISTEN_DATA_ACTIVITY |
		                        PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

		String networkCountry = telephonyManager.getNetworkCountryIso();
		String networkOperatorId = telephonyManager.getNetworkOperator();
		String networkName = telephonyManager.getNetworkOperatorName();
		int networkType = telephonyManager.getNetworkType();

		String incomingCall = null;
		if (telephonyManager.getCallState() == TelephonyManager.CALL_STATE_RINGING)
			incomingCall = telephonyManager.getLine1Number();
						
    // ---- Network Connectivity and Management
			
    ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    connectivity.setNetworkPreference(ConnectivityManager.TYPE_WIFI); 

    // ---- Managing and Monitoring WIFI
    
		final WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		if (!wifi.isWifiEnabled())
		  if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
		    wifi.setWifiEnabled(true);
					
		// Get a list of available configurations
		List<WifiConfiguration> configurations = wifi.getConfiguredNetworks();
		// Get the network ID for the first one.
		if (configurations.size() > 0) {
		  int netID = configurations.get(0).networkId;
		  // Enable that network.
		  wifi.enableNetwork(netID, true);
		}

		final WifiInfo info = wifi.getConnectionInfo();
		if (info.getBSSID() != null) {
		  int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
		  int speed = info.getLinkSpeed();
		  String units = WifiInfo.LINK_SPEED_UNITS;
		  String ssid = info.getSSID();
		          
		  String toastText = String.format("Connected to {0} at {1}{2}. Strength {3}/5", ssid, speed, units, strength);
		  Toast.makeText(this, toastText, Toast.LENGTH_LONG);
		} 
		
		// Snanning for Access Points
		// Register a broadcast receiver that listens for scan results.
		registerReceiver(new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    List<ScanResult> results = wifi.getScanResults();
		    ScanResult bestSignal = null;
		    for (ScanResult result : results) {
		      if (bestSignal == null || 
		          WifiManager.compareSignalLevel(bestSignal.level, result.level) < 0)
		        bestSignal = result;
		    }

		    String toastText = String.format("{0} networks found. {1} is the strongest.", results.size(), bestSignal.SSID);
		    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG);
		  }
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		// Initiate a scan.
		wifi.startScan();
						
		List<WifiConfiguration> networks = wifi.getConfiguredNetworks();
		if (networks.size() > 0) {
		  WifiConfiguration first = networks.get(0);
		  String bssid = first.BSSID;
		  bssid = first.SSID;
		  int id = first.networkId;
		  int p = first.priority;
		  int status = first.status;
		  switch (status) {
		    case WifiConfiguration.Status.CURRENT: break;
		    case WifiConfiguration.Status.DISABLED: break;
		    case WifiConfiguration.Status.ENABLED: break;
        default : break;
		  }
		}

		// ---- Vibration
										
		String vibratorService = Context.VIBRATOR_SERVICE;
		Vibrator vibrator = (Vibrator)getSystemService(vibratorService);
    long[] pattern = {1000,  2000, 4000, 8000, 16000 };
    vibrator.vibrate(pattern, 0);
    vibrator.vibrate(1000); // 1 second
    vibrator.cancel();
  }
    
  PhoneStateListener phoneStateListener = new PhoneStateListener() {
    public void onCallForwardingIndicatorChanged(boolean cfi) {}
	  public void onCallStateChanged(int state, String incomingNumber) {}
	  public void onCellLocationChanged(CellLocation location) {}
	  public void onDataActivity(int direction) {}
	  public void onDataConnectionStateChanged(int state) {}
	  public void onMessageWaitingIndicatorChanged(boolean mwi) {}
	  public void onServiceStateChanged(ServiceState serviceState) {}
	  public void onSignalStrengthChanged(int asu) {}
  };
    
  private void takePicture() {
    camera.takePicture(shutterCallback, rawCallback, jpegCallback); 
  }

  ShutterCallback shutterCallback = new ShutterCallback() {
    public void onShutter() {
      // Shutter has closed
    }
  };
    
  PictureCallback rawCallback = new PictureCallback() {
    public void onPictureTaken(byte[] _data, Camera _camera) {
      // TODO Handle RAW image data
    }
  };

  PictureCallback jpegCallback = new PictureCallback() {
    public void onPictureTaken(byte[] _data, Camera _camera) {
      // TODO Handle JPEG image data
    }
  };    
}