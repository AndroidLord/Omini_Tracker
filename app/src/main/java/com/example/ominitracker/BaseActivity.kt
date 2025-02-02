package com.example.ominitracker

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.ominitracker.util.ContextUtils
import com.example.ominitracker.util.PreferenceKeys
import com.example.ominitracker.util.appDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

open class BaseActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val updatedContext = updateLocaleIfNeeded(newBase)
        super.attachBaseContext(updatedContext)
    }

    private fun updateLocaleIfNeeded(context: Context): Context {
        val locale = runBlocking {
            context.appDataStore.data.map { it[PreferenceKeys.LOCALE] ?: "en" }.first()
        }
        return ContextUtils.setLocale(context, locale)
    }
}
