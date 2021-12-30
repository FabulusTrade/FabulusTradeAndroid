package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class AddedComment(
    val id: Int,
    val postId: Int,
    var parentCommentId: Int?,
    val text: String,
    val dateCreate: Date,
    val dateUpdate: Date
) : Parcelable