package com.example.weatherapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

//@TODO Analyze this entire file later

class LocationRepository(
    //val fusedLocationClient: FusedLocationProviderClient,
    val context: Context,
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient = SettingsClient(context)

    private val REQUEST_CHECK_SETTINGS = 1002

    private fun checkLocationAccess(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCurrentLocation(onLocationRetrieved: (Coordinates) -> Unit) {
        if(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                if(location != null) {
                    val coordinates = Coordinates(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    onLocationRetrieved(coordinates)
                } else {
                    fusedLocationClient.getLastLocation()
                        .addOnSuccessListener { lastLocation ->
                            if(lastLocation != null) {
                                val coordinates = Coordinates(
                                    latitude = lastLocation.latitude,
                                    longitude = lastLocation.longitude
                                )
                                onLocationRetrieved(coordinates)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Failed to get last location", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to get current location", exception)
            }
    }

    fun handleLocationAccess(activity: Activity, onLocationRetrieved: (Coordinates) -> Unit, onSettingsError: (Exception) -> Unit) {
        if(!checkLocationAccess(context)) {
            requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1001)
            return
        }

        //Won't let me add activity
        val settingsClient = SettingsClient(context)

        settingsClient.checkLocationSettings()
            .addOnSuccessListener {
                getCurrentLocation(onLocationRetrieved)
            }
            .addOnFailureListener { exception ->
                if(settingsClient.handleResolution(exception, activity, REQUEST_CHECK_SETTINGS)) {
                    //Resolution started, wait for result in activity
                } else {
                    onSettingsError(exception)
                }
            }
    }
}