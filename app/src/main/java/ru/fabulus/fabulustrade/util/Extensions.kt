package ru.fabulus.fabulustrade.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import moxy.MvpAppCompatFragment
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.NavElementsControl
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.showLongToast(msg: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    this.showToast(msg, duration)
}

fun Context.showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
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

fun ResourceProvider.formatQuantityString(stringId: Int, quantity: Int, vararg args: Any?): String =
    getQuantityString(stringId, quantity, *args)

// возвращает cтроку, или строку по умолчанию
fun ResourceProvider.formatStringWithDef(stringId: Int, value: Any?, defaultStringId: Int): String =
    value?.let { resultValue ->
        String.format(getStringResource(stringId), resultValue)
    } ?: getStringResource(defaultStringId)

// для цифровых значений по умолчанию возвращаем "-"
fun ResourceProvider.formatDigitWithDef(stringId: Int, value: Any?): String =
    formatStringWithDef(stringId, value, R.string.empty_profit_result)

// преобразует цвет из строки в целочисленные эквивалент, или возвращает значение по уполчанию.
// например: #008134 в -16744140
fun ResourceProvider.stringColorToIntWithDef(
    colorString: String?,
    defaultColorResourceId: Int = R.color.colorGray
): Int =
    colorString?.let { Color.parseColor(colorString) } ?: getColor(defaultColorResourceId)


fun TextView.setTextAndColor(textValue: String, color: Int) {
    text = textValue
    setTextColor(color)
}

/**
 * Формат преобразованной даты используется "dd.MM.yyyy HH:mm"
 */
fun Date.toStringFormat(patternDate: String = "dd.MM.yyyy HH:mm"): String =
    SimpleDateFormat(patternDate, Locale.getDefault()).format(this)

/** возвращает:
 * true -  для отрицательных чисел
 * false - для полоэительных
 * для -0.0 вернет - true
 * для 0.0 вернет - false
 */
fun Double.isNegativeDigit(): Boolean {
    return 1/this < 0
}