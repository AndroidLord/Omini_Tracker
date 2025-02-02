package com.example.ominitracker.util

import android.content.Context
import android.content.res.Configuration
import com.example.ominitracker.data.modal.UserInfo
import java.util.Locale

object Util {

    val packageName = "com.example.ominitracker"

    val notificationChannelId = "reminder_channel"
    val notificationServiceChannelId = "reminder_service_channel"

}

object PreferenceFile{
    val APP_DATA =  "app_data"
}

object ContextUtils {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
