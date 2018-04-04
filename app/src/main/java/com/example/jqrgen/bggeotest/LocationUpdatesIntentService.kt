package com.example.jqrgen.bggeotest

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationResult


/**
 * Handles incoming location updates and displays a notification with the location data.
 *
 * For apps targeting API level 25 ("Nougat") or lower, location updates may be requested
 * using {@link android.app.PendingIntent#getService(Context, int, Intent, int)} or
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)}. For apps targeting
 * API level O, only {@code getBroadcast} should be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */

class LocationUpdatesIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {
        Log.i(TAG, "onHandleIntent")

        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val locations = result.locations
                    Utils().setLocationUpdatesResult(this, locations)
                    Utils().sendNotification(this, Utils().getLocationResultTitle(this, locations))
                    Log.i(TAG, Utils().getLocationUpdatesResult(this))
                }
            }
        }
    }

    companion object {
        private const val ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.locationupdatespendingintent.action" + ".PROCESS_UPDATES"
        const val TAG = "bgLoc LUIntentService"
    }
}
