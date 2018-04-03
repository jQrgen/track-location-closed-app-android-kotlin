package com.example.jqrgen.bggeotest

import android.content.Context
import android.preference.PreferenceManager



class Utils{

    fun getRequestingLocationUpdates(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false)
    }

    companion object {
        const val KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested"

    }
}