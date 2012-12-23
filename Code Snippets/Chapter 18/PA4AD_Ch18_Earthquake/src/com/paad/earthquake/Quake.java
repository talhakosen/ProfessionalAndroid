/**
 * Listing 18-13: Making the Quake class a Parcelable
 */
package com.paad.earthquake;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Quake implements Parcelable {
  private Date date;
  private String details;
  private Location location;
  private double magnitude;
  private String link;
  public Date getDate() { return date; }
  public String getDetails() { return details; }
  public Location getLocation() { return location; }
  public double getMagnitude() { return magnitude; }
  public String getLink() { return link; }

  public Quake(Date _d, String _det, Location _loc,
               double _mag, String _link) {
    date = _d;
    details = _det;
    location = _loc;
    magnitude = _mag;
    link = _link;
  }

  @Override
  public String toString(){
    SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
    String dateString = sdf.format(date);
    return dateString + ":" + magnitude + " " + details;
  }

  private Quake(Parcel in) {
    date.setTime(in.readLong());
    details = in.readString();
    magnitude = in.readDouble();
    Location location = new Location("generated");
    location.setLatitude(in.readDouble());
    location.setLongitude(in.readDouble());
    link = in.readString();
  }

  public void writeToParcel(Parcel out, int flags) {
    out.writeLong(date.getTime());
    out.writeString(details);
    out.writeDouble(magnitude);
    out.writeDouble(location.getLatitude());
    out.writeDouble(location.getLongitude());
    out.writeString(link);
  }

  public static final Parcelable.Creator<Quake> CREATOR =
    new Parcelable.Creator<Quake>() {
      public Quake createFromParcel(Parcel in) {
        return new Quake(in);
      }

      public Quake[] newArray(int size) {
       return new Quake[size];
      }
    };

  public int describeContents() {
    return 0;
  }
}