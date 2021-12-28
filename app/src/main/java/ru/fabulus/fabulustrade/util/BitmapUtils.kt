package ru.fabulus.fabulustrade.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

object BitmapUtils {
    private const val MAX_SIZE = 2000

    fun decodeBitmap(uri: Uri, activity: Activity): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(activity.contentResolver.openInputStream(uri))
            var scale = 1
            while (options.outWidth / scale / 2 >= MAX_SIZE && options.outHeight / scale / 2 >= MAX_SIZE) scale *= 2
            val scaleOptions = BitmapFactory.Options()
            scaleOptions.inSampleSize = scale
            BitmapFactory.decodeStream(
                activity.contentResolver.openInputStream(uri),
                null,
                scaleOptions
            )
        } catch (e: FileNotFoundException) {
            null
        }
    }
}