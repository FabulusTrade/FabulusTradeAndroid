package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.fabulus.fabulustrade.util.largeThenCurrentDate
import java.util.*

@Parcelize
data class BlockUserInfo(
    val blockedUserId: String,
    val commentsBlockTime: Date,
    val postsBlockTime: Date
) : Parcelable {

    fun isEnabledAddComment(): Boolean = !commentsBlockTime.largeThenCurrentDate()
    fun isBlockAddPost(): Boolean = postsBlockTime.largeThenCurrentDate()

}