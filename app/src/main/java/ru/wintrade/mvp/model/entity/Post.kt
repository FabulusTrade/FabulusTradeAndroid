package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    val id: Int,
    val text: String,
    val postStatus: String,
    val dateCreate: Date,
    val dateUpdate: Date?,
    val pinned: Boolean,
    val images: List<String>
): Parcelable

