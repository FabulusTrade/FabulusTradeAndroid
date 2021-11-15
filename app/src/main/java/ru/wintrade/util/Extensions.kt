package ru.wintrade.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import moxy.MvpAppCompatFragment
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.NavElementsControl
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat

fun Context.showLongToast(msg: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(applicationContext, msg, duration).show()
}

fun Intent.createBitmapFromResult(activity: Activity): Bitmap? {
    val intentBundle = this.extras
    val intentUri = this.data
    var bitmap: Bitmap? = null
    if (intentBundle != null) {
        bitmap = (intentBundle.get("data") as? Bitmap)?.apply {
            compress(Bitmap.CompressFormat.JPEG, 75, ByteArrayOutputStream())
        }
    }
    if (bitmap == null && intentUri != null) {
        intentUri.let { bitmap = BitmapUtils.decodeBitmap(intentUri, activity) }
    }
    return bitmap
}

fun MvpAppCompatFragment.setDrawerLockMode(lockMode: Int) {
    val navElementsControl = this.requireActivity() as? NavElementsControl
    navElementsControl?.setDrawerLockMode(lockMode)
}

fun MvpAppCompatFragment.setToolbarVisible(visible: Boolean = true) {
    val navElementsControl = this.requireActivity() as? NavElementsControl
    navElementsControl?.toolbarVisible(visible)
}

fun MvpAppCompatFragment.setToolbarMenuVisible(visible: Boolean = true) {
    val navElementsControl = this.requireActivity() as? NavElementsControl
    navElementsControl?.setToolbarMenuVisible(visible)
}

fun Double.doubleToStringWithFormat(format: String, withPercent: Boolean? = null): String {
    return when (withPercent) {
        true -> "${DecimalFormat(format).format(this)} %"
        else -> DecimalFormat(format).format(this)
    }
}

fun String.toApiDate(): String =
    split(".").reversed().joinToString("-")

fun String.toUiDate(): String =
    split("-").reversed().joinToString(".")

fun ResourceProvider.formatString(stringId: Int, vararg args: Any?): String =
    String.format(getStringResource(stringId), *args)