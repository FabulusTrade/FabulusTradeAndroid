package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Trade(
    val id: Long,
    val traderId: String,
    var trader: Trader?,
    val operationType: String,
    val company: String,
    val companyImg: String,
    val ticker: String,
    val orderStatus: String,
    val orderNum: String,
    val price: Float,
    val currency: String,
    val date: Date,
    val profitCount: Float?,
    val subtype: String,
    val posts: Int?,
    val termOfTransaction: Float?
) : Parcelable