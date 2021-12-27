package ru.fabulus.fabulustrade.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

class ImageHelper(val context: Context) {
    @SuppressLint("Range")
    fun getBytesAndFileNameByUri(uri: String): Pair<String?, ByteArray> {
        val bytes = context.contentResolver.openInputStream(Uri.parse(uri))!!.readBytes()
        var name: String? = null
        val proj = arrayOf(OpenableColumns.DISPLAY_NAME)
        val cursor = context.contentResolver.query(Uri.parse(uri), proj, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return Pair(name, bytes)
    }
}