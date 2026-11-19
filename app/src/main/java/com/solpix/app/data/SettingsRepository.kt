package com.solpix.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "solpix_settings")

class SettingsRepository(private val context: Context) {
    companion object {
        val LAT_KEY = doublePreferencesKey("latitude")
        val LON_KEY = doublePreferencesKey("longitude")
    }

    val locationFlow = context.dataStore.data.map { prefs ->
        Pair(prefs[LAT_KEY] ?: 24.8607, prefs[LON_KEY] ?: 67.0011)
    }

    suspend fun saveLocation(lat: Double, lon: Double) {
        context.dataStore.edit { prefs ->
            prefs[LAT_KEY] = lat
            prefs[LON_KEY] = lon
        }
    }
}