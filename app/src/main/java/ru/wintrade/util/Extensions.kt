package ru.wintrade.util

import android.content.Context
import android.widget.Toast

fun Context.showLongToast(msg: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(applicationContext, msg, duration).show()
}