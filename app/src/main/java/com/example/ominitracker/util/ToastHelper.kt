package com.example.ominitracker.util

import android.content.Context
import android.widget.Toast

//a global toast and snackbar helper
fun Context.showToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(
        this,
        message,
        duration
    ).show()
}

fun Context.shortToast(
    message: String
) {
    showToast(message, Toast.LENGTH_SHORT)
}

fun Context.longToast(
    message: String
) {
    showToast(message, Toast.LENGTH_LONG)
}