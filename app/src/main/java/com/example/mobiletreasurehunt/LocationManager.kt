package com.example.mobiletreasurehunt

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.tasks.await

object LocationManager {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    fun initialize(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(context: Context, callback: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationManager", "Location permission not granted")
            callback(null)
            return
        }

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                currentLocation = locationResult.lastLocation
                Log.d("LocationManager", "New location obtained: ${currentLocation?.latitude}, ${currentLocation?.longitude}")
                callback(currentLocation)
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? {
        return try {
            currentLocation = fusedLocationProviderClient.lastLocation.await()
            Log.d("LocationManager", "Last known location: ${currentLocation?.latitude}, ${currentLocation?.longitude}")
            currentLocation
        } catch (e: Exception) {
            Log.e("LocationManager", "Error getting last known location: ${e.message}")
            null
        }
    }
}