package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TraderStatistic(
    val idTrader: String,
    val termOfTransaction30: Double?,
    val amountDeals30: Int?,
    val profitOfPercent30: Double?,
    val profitTrades30: Double?,
    val losingTrades30: Double?,
    val averageProfitTrades30: Double?,
    val averageLosingTrades30: Double?,
    val actualProfit180: Double?,
    val actualProfit365: Double?,
    val monthIndicators: List<MonthIndicator>,
    val audienceData: List<AudienceData>
) : Parcelable

