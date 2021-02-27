package ru.wintrade.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(path: String, container: ImageView) {
    Glide.with(container.context)
        .load(path)
        .into(container)
}

fun loadImage(id: Int, container: ImageView) {
    Glide.with(container.context)
        .load(id)
        .into(container)
}