package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseTraderStatistic(
    @Expose
    @SerializedName("id_trader")
    val idTrader: String,
    @Expose
    @SerializedName("term_of_transaction_30")
    val termOfTransaction30: Double?,
    @Expose
    @SerializedName("amount_deals_30")
    val amountDeals30: Int?,
    @Expose
    @SerializedName("profit_of_percent_30")
    val profitOfPercent30: Double?,
    @Expose
    @SerializedName("profit_trades_30")
    val profitTrades30: Double?,
    @Expose
    @SerializedName("losing_trades_30")
    val losingTrades30: Double?,
    @Expose
    @SerializedName("average_profit_trades_30")
    val averageProfitTrades30: Double?,
    @Expose
    @SerializedName("average_losing_trades_30")
    val averageLosingTrades30: Double?,
    @Expose
    @SerializedName("actual_profit_180")
    val actualProfit180: Double?,
    @Expose
    @SerializedName("actual_profit_365")
    val actualProfit365: Double?,
    @Expose
    @SerializedName("month_indicators")
    val monthIndicators: List<ResponseMonthIndicators>,
    @Expose
    @SerializedName("audience_data")
    val audienceData: List<ResponseAudienceData>
)