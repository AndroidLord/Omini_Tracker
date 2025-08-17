package com.example.ominitracker.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.ominitracker.ui.screen.LoadingLottieScreen
import com.example.ominitracker.ui.screen.ReminderScreen
import com.example.ominitracker.ui.screen.home.HomeScreen
import com.example.ominitracker.ui.screen.onboarding.OnboardingScreen
import com.example.ominitracker.util.PreferenceKeys
import com.example.ominitracker.util.Util
import com.example.ominitracker.util.appDataStore
import com.example.ominitracker.util.isValidAge
import com.example.ominitracker.util.saveUserInfo
import com.example.ominitracker.util.shortToast
import com.example.ominitracker.util.updateOnboardingPromptDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppNavGraph(navController: NavHostController) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val isUserSetupComplete = withContext(Dispatchers.IO) {
            context.appDataStore.data.map { preferences ->
                val isBasicInfoFilled = !(
                        preferences[PreferenceKeys.USER_NAME].isNullOrBlank() ||
                                preferences[PreferenceKeys.USER_GENDER].isNullOrBlank() ||
                                (preferences[PreferenceKeys.USER_AGE]).isValidAge()
                        )
                if (isBasicInfoFilled) {
                    return@map true
                }
                val lastPromptDateMillis =
                    preferences[PreferenceKeys.SKIP_ONBOARDING_LAST_PROMPT_DATE] ?: 0L
                val currentTimeMillis = System.currentTimeMillis()
                val daysElapsed = (currentTimeMillis - lastPromptDateMillis) / (24 * 60 * 60 * 1000)
                updateOnboardingPromptDate(context, currentTimeMillis)

                val goToHomeScreen = daysElapsed < 7
//              Log.d("AppNavGraph", "isUserSetupComplete: ${isBasicInfoFilled}, daysElapsed: $daysElapsed, goToHomeScreen: $goToHomeScreen")
                return@map goToHomeScreen
            }.first()
        }
        delay(2000)
        val route = if (isUserSetupComplete) {
            Routes.Home.route
        } else {
            Routes.OnBoarding.route
        }

        navController.navigate(route) {
            popUpTo(Routes.Loading.route) {
                inclusive = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Loading.route
    ) {
        composable(Routes.Loading.route) {
            LoadingLottieScreen()
        }
        composable(Routes.OnBoarding.route) {
            OnboardingScreen(
                skip = {
                    Log.d("AppNavGraph", "Skipping Onboarding")
                    coroutineScope.launch {
                        val sevenDaysMilli = (24 * 60 * 60 * 1000) * 7
                        val currentTimeMillis = System.currentTimeMillis()
                        val nextDate = currentTimeMillis + sevenDaysMilli
                        updateOnboardingPromptDate(context, nextDate)
                        context.shortToast(message = "Skipping onboarding!")
                    }
                    navController.navigate(Routes.Home.route){
                        popUpTo(Routes.OnBoarding.route) {
                            inclusive = true
                        }
                    }
                },
                onComplete = { userInfo ->
                    coroutineScope.launch {
                        saveUserInfo(context, userInfo)
                    }
                    navController.navigate(Routes.Home.route){
                        popUpTo(Routes.OnBoarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Routes.Home.route) {
            HomeScreen()
        }
        composable(
            Routes.Reminder.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "${Util.packageName}://${Routes.Reminder.route}"
            })
        ) {
            ReminderScreen()
        }
    }
}
