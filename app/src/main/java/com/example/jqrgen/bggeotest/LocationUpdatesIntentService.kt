package com.example.jqrgen.bggeotest

import android.app.IntentService
import android.content.Intent


class LocationUpdatesIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {

    }

    companion object {
        const val ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.locationupdatespendingintent.action" + ".PROCESS_UPDATES"
        private val TAG = LocationUpdatesIntentService::class.java.simpleName
    }
}// Name the worker thread.
