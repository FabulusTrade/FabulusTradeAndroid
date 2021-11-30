package ru.wintrade.ui.customview.imagegroup

import android.widget.ImageView

interface ImageLoader {
    fun load(url: String, imageView: ImageView, index: Int, count: Int)
    fun clear()
}