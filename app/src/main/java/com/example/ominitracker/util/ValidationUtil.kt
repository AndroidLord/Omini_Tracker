package com.example.ominitracker.util

fun Int?.isValidAge(): Boolean {
    return this != null && this >= 1
}