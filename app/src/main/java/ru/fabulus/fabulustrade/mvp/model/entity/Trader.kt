package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trader(
    val id: String,
    val username: String?,
    val avatar: String?,
    val kval: Boolean,
    val isActive: Boolean,
    val isTrader: Boolean,
    val dateJoined: String?,
    val followersCount: Int,
    val tradesCount: Int,
    val colorIncrDecrDepo365: ColorItem?,
    val followersForWeekCount: Int,
    val pinnedPost: Post?,
    val flashLimit: Int
) : Parcelable