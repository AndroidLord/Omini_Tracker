package com.example.ominitracker.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {

    @Serializable
    object Home : Routes("home")
    @Serializable
    object Reminder : Routes("reminder")
    @Serializable
    object OnBoarding : Routes("onboarding")
    @Serializable
    object Loading : Routes("loading")
}