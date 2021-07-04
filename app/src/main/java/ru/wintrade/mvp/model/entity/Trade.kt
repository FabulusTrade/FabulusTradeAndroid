package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
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
    val orderNum: Long?,
    val price: Float,
    val count: Float,
    val currency: String,
    val date: Date,
    val profitCount: String?,
    val value: String?,
    val subtype: String
) : Parcelable