package com.endurancetech.unitconverterapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "last_conversion")

class LastConversionRepository(private val context: Context) {

    private val lastConversionKey = stringPreferencesKey("last_conversion_json")

    suspend fun saveLastConversion(conversion: Conversion) {
        context.dataStore.edit { preferences ->
            preferences[lastConversionKey] = Json.encodeToString(conversion)
        }
    }

    val lastConversionFlow: Flow<Conversion?> = context.dataStore.data.map { preferences ->
            preferences[lastConversionKey]?.let { jsonString ->
                Json.decodeFromString<Conversion>(jsonString)
            }
        }
}