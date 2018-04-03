package com.example.jqrgen.bggeotest

import android.content.Context
import android.preference.PreferenceManager


class Utils{

    fun setRequestingLocationUpdates(context: Context, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply()
    }

    fun getRequestingLocationUpdates(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false)
    }

    fun getLocationUpdatesResult(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "")
    }

    companion object {
        const val KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested"
        const val KEY_LOCATION_UPDATES_RESULT = "location-update-result"
    }
}