package ru.wintrade.util

import android.content.Context

class ResourceHelper(val context: Context) {
    fun getLoadingImages() =
        listOf(
            getImageIdFromDrawable("splash"),
            getImageIdFromDrawable("splash"),
            getImageIdFromDrawable("splash")
        )


    fun getImageIdFromDrawable(imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}