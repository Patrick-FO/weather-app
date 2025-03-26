package com.example.weatherapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LocationRepository(
    val fusedLocationClient: FusedLocationProviderClient,
    val context: Context,
    val activity: Activity
) {
    /* Saved for later for when I'm gonna add location updates
    //What is going on in the locationRequest variable?
    //As its deprecated, what are the alternatives? Check documentation
    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        interval = 100000
        fastestInterval = 50000
    }

    //What is the LocationCallback class?
    private var locationCallback: LocationCallback? = null
    */

    private fun checkLocationAccess(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun handleLocationAccess(activity: Activity, onLocationRetrieved: (Coordinates) -> Unit) {
        if(!checkLocationAccess(context)) {
            requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1001)
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener { location ->
                    val coordinates = Coordinates(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    onLocationRetrieved(coordinates)
                }
        }
    }


}