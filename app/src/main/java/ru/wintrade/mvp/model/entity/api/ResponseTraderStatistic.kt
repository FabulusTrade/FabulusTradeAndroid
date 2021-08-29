package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.wintrade.mvp.model.entity.AudienceData
import ru.wintrade.mvp.model.entity.MonthIndicator
import ru.wintrade.mvp.model.entity.TraderStatistic

data class ResponseTraderStatistic(
    @Expose
    @SerializedName("id_trader")
    override val idTrader: String,
    @Expose
    @SerializedName("term_of_transaction_30")
    override val termOfTransaction30: Double,
    @Expose
    @SerializedName("amount_deals_30")
    override val amountDeals30: Int,
    @Expose
    @SerializedName("profit_of_percent_30")
    override val profitOfPercent30: Double,
    @Expose
    @SerializedName("profit_trades_30")
    override val profitTrades30: Double,
    @Expose
    @SerializedName("losing_trades_30")
    override val losingTrades30: Double,
    @Expose
    @SerializedName("average_profit_trades_30")
    override val averageProfitTrades30: Double,
    @Expose
    @SerializedName("average_losing_trades_30")
    override val averageLosingTrades30: Double,
    @Expose
    @SerializedName("actual_profit_180")
    override val actualProfit180: Double,
    @Expose
    @SerializedName("actual_profit_365")
    override val actualProfit365: Double,
    @Expose
    @SerializedName("month_indicators")
    override val monthIndicators: MonthIndicator,
    @Expose
    @SerializedName("audience_data")
    override val audienceData: AudienceData
) : TraderStatistic