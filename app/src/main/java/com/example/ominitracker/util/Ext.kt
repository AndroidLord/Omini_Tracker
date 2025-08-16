package com.example.ominitracker.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.ominitracker.data.modal.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = PreferenceFile.APP_DATA)


suspend fun saveUserInfo(context: Context,userInfo: UserInfo) {
    context.appDataStore.edit { preferences ->
        preferences[PreferenceKeys.USER_NAME] = userInfo.name
        preferences[PreferenceKeys.USER_AGE] = userInfo.age ?: 0
        preferences[PreferenceKeys.USER_GENDER] = userInfo.gender
    }
}

suspend fun updateOnboardingPromptDate(context: Context, currentTimeMillis: Long) = withContext(Dispatchers.IO){
    context.appDataStore.edit { prefs ->
        prefs[PreferenceKeys.SKIP_ONBOARDING_LAST_PROMPT_DATE] = currentTimeMillis
    }
}
