package com.peterchege.pchat.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.peterchege.pchat.core.datastore.preferences.PreferencesKeys.FCM_TOKEN
import com.peterchege.pchat.core.datastore.preferences.PreferencesKeys.THEME_OPTIONS
import com.peterchege.pchat.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private object PreferencesKeys {
    val FCM_TOKEN = stringPreferencesKey("fcm_token")
    val THEME_OPTIONS = stringPreferencesKey("theme_options")
}
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun setTheme(themeValue: String) {
        dataStore.edit { preferences ->
            preferences[THEME_OPTIONS] = themeValue
        }
    }
    fun getTheme(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_OPTIONS] ?: Constants.LIGHT_MODE
        }
    }

    suspend fun setFCMToken(token:String){
        dataStore.edit { preferences ->
            preferences[FCM_TOKEN] = token
        }
    }

    fun getFCMToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[FCM_TOKEN]
        }
    }

}