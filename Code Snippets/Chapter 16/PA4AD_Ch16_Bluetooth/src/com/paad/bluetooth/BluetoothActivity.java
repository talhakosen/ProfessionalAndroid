package com.paad.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class BluetoothActivity extends Activity {
  
    protected static final String TAG = "BLUETOOTH";
    protected static final int DISCOVERY_REQUEST = 1;
    BluetoothAdapter bluetooth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      
      /**
       * Listing 16-1: Accessing the default Bluetooth Adapter
       */
      BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();   
      
      //
      this.bluetooth = bluetooth;
    }
    
    /**
     * Listing 16-2: Enabling Bluetooth
     */
    private static final int ENABLE_BLUETOOTH = 1;

    private void initBluetooth() {
      if (!bluetooth.isEnabled()) { 
        // Bluetooth isn't enabled, prompt the user to turn it on.
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, ENABLE_BLUETOOTH);
      } else {
        // Bluetooth is enabled, initialize the UI.
        initBluetoothUI();
      }
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
      if (requestCode == ENABLE_BLUETOOTH)
        if (resultCode == RESULT_OK) {
          // Bluetooth has been enabled, initialize the UI.
          initBluetoothUI();
        }
      
      /**
       * Listing 16-4: Monitoring discoverability request approval
       */
      if (requestCode == DISCOVERY_REQUEST) {
        if (resultCode == RESULT_CANCELED) {
          Log.d(TAG, "Discovery cancelled by user");
        }
      }

    }
    
    private void makeDiscoverable() {
      /**
       * Listing 16-3: Enabling discoverability
       */
      startActivityForResult(
        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),
                   DISCOVERY_REQUEST);
    }
    
    /**
     * Listing 16-5: Discovering remote Bluetooth Devices
     */
    private ArrayList<BluetoothDevice> deviceList = 
      new ArrayList<BluetoothDevice>();

    private void startDiscovery() {
      registerReceiver(discoveryResult,
                       new IntentFilter(BluetoothDevice.ACTION_FOUND));

      if (bluetooth.isEnabled() && !bluetooth.isDiscovering())
        deviceList.clear();
        bluetooth.startDiscovery();
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String remoteDeviceName = 
          intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

        BluetoothDevice remoteDevice =  
          intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        deviceList.add(remoteDevice);

        Log.d(TAG, "Discovered " + remoteDeviceName);
      }
    };
    
    /**
     * Listing 16-6: Listening for Bluetooth Socket connection requests
     */
    private BluetoothSocket transferSocket;

    private UUID startServerSocket(BluetoothAdapter bluetooth) {
      UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
      String name = "bluetoothserver";

      try {
        final BluetoothServerSocket btserver = 
          bluetooth.listenUsingRfcommWithServiceRecord(name, uuid);

        Thread acceptThread = new Thread(new Runnable() {
          public void run() {
            try {
              // Block until client connection established.
              BluetoothSocket serverSocket = btserver.accept();
              // Start listening for messages.
              StringBuilder incoming = new StringBuilder();
              listenForMessages(serverSocket, incoming);
              // Add a reference to the socket used to send messages.
              transferSocket = serverSocket;
            } catch (IOException e) {
              Log.e("BLUETOOTH", "Server connection IO Exception", e);
            }
          }
        });
        acceptThread.start();
      } catch (IOException e) {
        Log.e("BLUETOOTH", "Socket listener IO Exception", e);
      }
      return uuid;
    }

    /**
     * Listing 16-7: Creating a Bluetooth client socket
     */
    private void connectToServerSocket(BluetoothDevice device, UUID uuid) {
      try{
        BluetoothSocket clientSocket 
          = device.createRfcommSocketToServiceRecord(uuid);

        // Block until server connection accepted.
        clientSocket.connect();

        // Start listening for messages.
        StringBuilder incoming = new StringBuilder();
        listenForMessages(clientSocket, incoming);

        // Add a reference to the socket used to send messages.
        transferSocket = clientSocket;

      } catch (IOException e) {
        Log.e("BLUETOOTH", "Blueooth client I/O Exception", e);
      }
    }

    /**
     * Listing 16-8: Sending and receiving strings using Bluetooth Sockets
     */
    private void sendMessage(BluetoothSocket socket, String message) {
      OutputStream outStream;
      try {
        outStream = socket.getOutputStream();

        // Add a stop character.
        byte[] byteArray = (message + " ").getBytes();
        byteArray[byteArray.length - 1] = 0;

        outStream.write(byteArray);
      } catch (IOException e) { 
        Log.e(TAG, "Message send failed.", e);
      }
    }

    private boolean listening = false;
     
    private void listenForMessages(BluetoothSocket socket, 
                                   StringBuilder incoming) {
      listening = true;


      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];

      try {
        InputStream instream = socket.getInputStream();
        int bytesRead = -1;

        while (listening) {
          bytesRead = instream.read(buffer);
          if (bytesRead != -1) {
            String result = "";
            while ((bytesRead == bufferSize) &&
                   (buffer[bufferSize-1] != 0)){
              result = result + new String(buffer, 0, bytesRead - 1);
              bytesRead = instream.read(buffer);
            }
            result = result + new String(buffer, 0, bytesRead - 1);
            incoming.append(result);
          }
          socket.close();
        }
      } catch (IOException e) {
        Log.e(TAG, "Message received failed.", e);
      }
      finally {
      }
    }
    
    private void initBluetoothUI() {
      // TODO Update the UI now that Bluetooth is enabled. 
    }
}