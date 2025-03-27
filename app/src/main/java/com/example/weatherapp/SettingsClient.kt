package com.example.weatherapp

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

//@TODO Analyze this entire file later
//What is all of this build()/builder() stuff?

class SettingsClient(context: Context) {
    //Why does getSettingsClient need the context as a parameter
    private val settingsClient = LocationServices.getSettingsClient(context)

    private val bestAvailableRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5000)
        .setMaxUpdateDelayMillis(15000)
        .build()

    //What is the task of type LocationSettingsResponse? What is that type?
    fun checkLocationSettings(useHighAccuracy: Boolean = true): Task<LocationSettingsResponse> {
        val builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest((bestAvailableRequest))

        builder.setAlwaysShow(true)

        return settingsClient.checkLocationSettings(builder.build())
    }

    fun checkLocationSettings(vararg requests: LocationRequest): Task<LocationSettingsResponse> {
        val builder = LocationSettingsRequest.Builder()

        for (request in requests) {
            builder.addLocationRequest(request)
        }

        builder.setAlwaysShow(true)

        return settingsClient.checkLocationSettings(builder.build())
    }

    fun handleResolution(exception: Exception, activity: Activity, requestCode: Int): Boolean {
        if(exception is ResolvableApiException) {
            try {
                exception.startResolutionForResult(activity, requestCode)
                return true
            } catch (sendEx: IntentSender.SendIntentException) {

            }
        }
        return false
    }
}