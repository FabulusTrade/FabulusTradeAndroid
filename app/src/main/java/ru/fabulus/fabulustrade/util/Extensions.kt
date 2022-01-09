package ru.fabulus.fabulustrade.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import moxy.MvpAppCompatFragment
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.NavElementsControl
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.showLongToast(msg: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    this.showToast(msg, duration)
}

fun Context.showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, msg, duration).show()
}

fun showCustomSnackbar(
    layoutId: Int,
    layoutInflater: LayoutInflater,
    containerLayout: View,
    msg: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT
) {

    val snackbar = Snackbar.make(containerLayout, msg, duration)

    // Get the Snackbar's layout view
    val layout = snackbar.view as SnackbarLayout

    // Hide the text
    val textView = layout.findViewById<View>(R.id.snackbar_text) as TextView
    textView.visibility = View.INVISIBLE

    // Inflate our custom view
    val snackView: View = layoutInflater.inflate(layoutId, null)

    //If the view is not covering the whole snackbar layout, add this line
    layout.setPadding(0, 0, 0, 0)

    // Add the view to the Snackbar's layout
    layout.addView(snackView, 0)
    // Show the Snackbar
    snackbar.show()
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
 * false - для положительных
 * для -0.0 вернет - true
 * для 0.0 вернет - false
 */
fun Double.isNegativeDigit(): Boolean {
    return 1 / this < 0
}

fun ImageView.getBitmapUriFromDrawable(): Uri? {

    val context = this.context
    val drawable: Drawable = this.drawable
    var bmp: Bitmap? = null

    bmp = drawable.toBitmap()
    var bmpUri: Uri? = null
    try {

        // Use methods on Context to access package-specific directories on external storage.
        // This way, you don't need to request external read/write permission.
        // See https://youtu.be/5xVh-7ywKpE?t=25m25s
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
        bmpUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()

            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            FileProvider.getUriForFile(
                context,
                context.getString(R.string.file_provider_name),
                file
            ) // use this version for API >= 24
        } else {
            Uri.fromFile(file)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}

fun String.toSpannableText(
    startIndex: Int,
    endIndex: Int,
    textColor: Int
): Spannable {
    val outPutColoredText: Spannable = SpannableString(this)
    if (endIndex > startIndex) {
        outPutColoredText.setSpan(
            ForegroundColorSpan(textColor), startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return outPutColoredText
}