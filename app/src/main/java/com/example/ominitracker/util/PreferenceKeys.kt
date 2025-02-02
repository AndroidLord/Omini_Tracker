package com.example.ominitracker.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val LOCALE = stringPreferencesKey("locale")
    val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_AGE = intPreferencesKey("user_age")
    val USER_GENDER = stringPreferencesKey("user_gender")
    val SKIP_ONBOARDING_LAST_PROMPT_DATE = longPreferencesKey("skip_onboarding_last_prompt_date")

}
