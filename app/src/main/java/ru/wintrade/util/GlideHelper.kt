package ru.wintrade.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat.ID_NULL
import com.bumptech.glide.Glide

fun loadImage(path: String, container: ImageView, @DrawableRes placeholder: Int = ID_NULL) {
    Glide.with(container.context).load(path)
        .fitCenter()
        .placeholder(placeholder)
        .into(container)
}

fun loadImage(id: Int, container: ImageView) {
    Glide.with(container.context)
        .load(id)
        .into(container)
}

fun cancelImageLoading(container: ImageView) {
    Glide.with(container.context)
        .clear(container)
}