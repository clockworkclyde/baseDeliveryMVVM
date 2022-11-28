package com.github.clockworkclyde.basedeliverymvvm.util

import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng

fun Point.toLatLng() = LatLng(this.latitude(), this.longitude())