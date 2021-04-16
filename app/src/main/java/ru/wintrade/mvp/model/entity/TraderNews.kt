package ru.wintrade.mvp.model.entity

import java.util.*

data class TraderNews(
    val id: Int,
    val text: String,
    val postStatus: String,
    val dateCreate: Date,
    val dateUpdate: Date?,
    val pinned: Boolean,
    val images: List<String>
)

