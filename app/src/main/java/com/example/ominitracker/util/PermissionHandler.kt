@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.ominitracker.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

object PermissionHandler {

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun RequestNotificationPermission() {
        val notificationPermissionState =
            rememberPermissionState(
                permission = android.Manifest.permission.POST_NOTIFICATIONS
            )


        LaunchedEffect(Unit) {
            if (!notificationPermissionState.status.isGranted) {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestForegroundServiceSpecialUsePermission() {
        val foregroundServiceSpecialUsePermissionState =
            rememberPermissionState(
                permission = android.Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE
            )

        LaunchedEffect(Unit) {
            if (!foregroundServiceSpecialUsePermissionState.status.isGranted) {
                foregroundServiceSpecialUsePermissionState.launchPermissionRequest()
            }
        }
    }


}
