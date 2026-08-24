package com.example.vid.core

import android.util.Patterns

fun String.isValidUrl(): Boolean {
    return this.startsWith("http://") ||
            this.startsWith("https://") &&
                Patterns.WEB_URL.matcher(
        this
    ).matches()
}