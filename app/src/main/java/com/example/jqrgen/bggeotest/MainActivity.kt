package com.example.jqrgen.bggeotest

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices





class MainActivity : FragmentActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    // UI Widgets.
    private var mRequestUpdatesButton: Button? = null
    private var mRemoveUpdatesButton: Button? = null
    private var mLocationUpdatesResultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRequestUpdatesButton = findViewById(R.id.request_updates_button);
        mRemoveUpdatesButton = findViewById(R.id.remove_updates_button)
        mLocationUpdatesResultView = findViewById(R.id.location_updates_result)

        // Check if the user revoked runtime permissions.
        if (!checkFineLocationPermission()) {
            requestFineLocationPermission()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()

    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        updateButtonsState(Utils().getRequestingLocationUpdates(this))
        Log.i(TAG, "Utils().getLocationUpdatesResult(this): ${Utils().getLocationUpdatesResult(this)}" )
        mLocationUpdatesResultView?.setText(Utils().getLocationUpdatesResult(this))
    }

    override fun onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        // Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
        // less frequently than this interval when the app is no longer in the foreground.
        mLocationRequest?.interval = UPDATE_INTERVAL

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL

        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest?.maxWaitTime = MAX_WAIT_TIME
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkFineLocationPermission(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestFineLocationPermission() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            // NOTE: Removed snackbar from here.
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        if (s == Utils.KEY_LOCATION_UPDATES_RESULT) {
            mLocationUpdatesResultView?.setText(Utils().getLocationUpdatesResult(this))
        } else if (s == Utils.KEY_LOCATION_UPDATES_REQUESTED) {
            updateButtonsState(Utils().getRequestingLocationUpdates(this))
        }
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private fun updateButtonsState(requestingLocationUpdates: Boolean) {
        if (requestingLocationUpdates) {
            mRequestUpdatesButton?.setEnabled(false)
            mRemoveUpdatesButton?.setEnabled(true)
        } else {
            mRequestUpdatesButton?.setEnabled(true)
            mRemoveUpdatesButton?.setEnabled(false)
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 1

        const val UPDATE_INTERVAL = 1000L*60L*180L
        const val FASTEST_UPDATE_INTERVAL = 1000L*60L*10L
        const val MAX_WAIT_TIME = 1000L*60L*60L*16L
    }

}
