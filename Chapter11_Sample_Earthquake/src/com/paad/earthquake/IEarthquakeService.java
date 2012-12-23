/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\PADDEV_BETA\\Chapter11_Sample_Earthquake\\src\\com\\paad\\earthquake\\IEarthquakeService.aidl
 */
package com.paad.earthquake;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
import java.util.List;
public interface IEarthquakeService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.paad.earthquake.IEarthquakeService
{
private static final java.lang.String DESCRIPTOR = "com.paad.earthquake.IEarthquakeService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IEarthquakeService interface,
 * generating a proxy if needed.
 */
public static com.paad.earthquake.IEarthquakeService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
com.paad.earthquake.IEarthquakeService in = (com.paad.earthquake.IEarthquakeService)obj.queryLocalInterface(DESCRIPTOR);
if ((in!=null)) {
return in;
}
return new com.paad.earthquake.IEarthquakeService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getEarthquakes:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.paad.earthquake.Quake> _result = this.getEarthquakes();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_refreshEarthquakes:
{
data.enforceInterface(DESCRIPTOR);
this.refreshEarthquakes();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.paad.earthquake.IEarthquakeService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public java.util.List<com.paad.earthquake.Quake> getEarthquakes() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.paad.earthquake.Quake> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEarthquakes, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.paad.earthquake.Quake.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void refreshEarthquakes() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_refreshEarthquakes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getEarthquakes = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_refreshEarthquakes = (IBinder.FIRST_CALL_TRANSACTION + 1);
}
public java.util.List<com.paad.earthquake.Quake> getEarthquakes() throws android.os.RemoteException;
public void refreshEarthquakes() throws android.os.RemoteException;
}
