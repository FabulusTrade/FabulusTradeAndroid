package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.fabulus.fabulustrade.util.largeThenCurrentDate
import java.util.Date

@Parcelize
data class BlockUserInfo(
    val blockedUserId: String,
    val commentsBlockTime: Date,
    val postsBlockTime: Date
) : Parcelable {

    fun isEnabledOperationsOnComment(): Boolean = !commentsBlockTime.largeThenCurrentDate()
    fun isEnabledOperationsOnPost(): Boolean = !postsBlockTime.largeThenCurrentDate()
}