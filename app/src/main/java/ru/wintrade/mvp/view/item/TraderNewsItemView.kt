package ru.wintrade.mvp.view.item

import java.util.*

interface TraderNewsItemView {
    var pos: Int
    fun setNewsDate(date: Date)
    fun setPost(text: String)
}