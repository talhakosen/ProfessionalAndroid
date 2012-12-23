package com.paad.earthquake;

import com.paad.earthquake.Quake;

interface IEarthquakeService {
  List<Quake> getEarthquakes();
  void refreshEarthquakes();
}
