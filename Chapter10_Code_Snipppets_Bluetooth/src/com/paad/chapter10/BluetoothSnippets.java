package com.paad.chapter10;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BluetoothSnippets extends Activity {

  @Override    
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final BluetoothDevice bluetooth = (BluetoothDevice)getSystemService(Context.BLUETOOTH_SERVICE);

    bluetooth.enable(new IBluetoothDeviceCallback.Stub() {
      @Override
      public void onCreateBondingResult(String _address, int _result) throws RemoteException 
      {
        BluetoothDevice.RESULT_SUCCESS
      }

      @Override
      public void onEnableResult(int _result) throws RemoteException {
        if (_result == BluetoothDevice.RESULT_SUCCESS) {
          bluetooth.setName("LORE");
          bluetooth.setMode(BluetoothDevice.MODE_DISCOVERABLE);
        }
      }
    });

    bluetooth.startPeriodicDiscovery();
    bluetooth.startDiscovery(true);
    
    bluetooth.listRemoteDevices();
    bluetooth.getRemoteName("address");
    
    String[] devices = bluetooth.listRemoteDevices();

    for (String device : devices) {
      if (!bluetooth.hasBonding(device)) {
        // Set the pairing PIN. In real life it’s probably a smart
        // move to make this user enterable and dynamic.
        bluetooth.setPin(device, new byte[] {1,1,1,1});
        bluetooth.createBonding(device, new IBluetoothDeviceCallback.Stub() {
          public void onCreateBondingResult(String _address, int _result) throws RemoteException {
            if (_result == BluetoothDevice.RESULT_SUCCESS) {
              String connectText = "Connected to " + bluetooth.getRemoteName(_address);
              Toast.makeText(getApplicationContext(), connectText, Toast.LENGTH_SHORT);
            }
          }

          public void onEnableResult(int _result) throws RemoteException {}

        });
      }
    }

    bluetooth.listBondings(); 
    bluetooth.lastSeen("");
    bluetooth.lastUsed("");
    bluetooth.removeBonding(""); 
    
    // Bluetooth Comms
    
    FileDescriptor localFile;
    RfcommSocket localSocket2 = new RfcommSocket();
    try {
      localFile = localSocket2.create();
    } catch (IOException e) { }

    FileDescriptor remoteFile;

    RfcommSocket localSocket1 = new RfcommSocket();
    try {
      localFile = localSocket1.create();
      localSocket1.bind(null);
      localSocket1.listen(1);
      RfcommSocket remotesocket = new RfcommSocket();
      remoteFile = localSocket1.accept(remotesocket, 10000);
    } 
    catch (IOException e) { }

    String remoteAddress = bluetooth.listBondings()[0];

    RfcommSocket localSocket = new RfcommSocket();
    try {
      localFile = localSocket.create();
      // Select an unused port
      if (localSocket.connect(remoteAddress, 0)) {
        FileWriter output = new FileWriter(localFile);
        output.write("Hello, Android");
        output.close();
      }
    } catch (IOException e) { }

    // Bluetooth Headset
    
    BluetoothHeadset headset = new BluetoothHeadset(this);
    headset.connectHeadset("", new IBluetoothHeadsetCallback.Stub() {
      public void onConnectHeadsetResult(String _address, int _resultCode) throws RemoteException {
        if (_resultCode == BluetoothHeadset.RESULT_SUCCESS) {
          // Connected to a new headset device.
        }
      }
    });

    if (headset.getState() == BluetoothHeadset.STATE_CONNECTED) {
      // TODO Perform actions on headset.
    }
    
    headset.close();    
  }
}