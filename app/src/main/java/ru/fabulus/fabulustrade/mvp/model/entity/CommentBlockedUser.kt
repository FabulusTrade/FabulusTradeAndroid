package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentBlockedUser(
    val authorID: String,
    val blockedUserID: String
) : Parcelable