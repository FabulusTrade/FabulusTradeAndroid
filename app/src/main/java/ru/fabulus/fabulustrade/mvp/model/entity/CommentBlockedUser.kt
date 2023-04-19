package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentBlockedUser(
    val authorID: String,
    val blockedUserID: String
) : Parcelable