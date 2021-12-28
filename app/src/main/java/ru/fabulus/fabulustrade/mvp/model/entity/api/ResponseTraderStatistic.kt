package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.fabulus.fabulustrade.mvp.model.entity.ColorItem

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
    @SerializedName("sum_profit_365")
    val sumProfit365: Double?,
    @Expose
    @SerializedName("average_count_operations_month")
    val averageCountOperationsMonth: Double?,
    @Expose
    @SerializedName("term_of_transaction_365")
    val termOfTransaction365: Double?,
    @Expose
    @SerializedName("profit_trades_365")
    val profitTrades365: Double?,
    @Expose
    @SerializedName("losing_trades_365")
    val losingTrades365: Double?,
    @Expose
    @SerializedName("average_profit_trades_365")
    val averageProfitTrades365: Double?,
    @Expose
    @SerializedName("average_losing_trades_365")
    val averageLosingTrades365: Double?,
    @Expose
    @SerializedName("incr_decr_depo_365")
    val incrDecrDepo365: Double?,
    @Expose
    @SerializedName("color_incr_decr_depo_365")
    val colorIncrDecrDepo365: ColorItem?,
    @Expose
    @SerializedName("ratio_365_long")
    val ratio365Long: Double?,
    @Expose
    @SerializedName("ratio_365_short")
    val ratio365Short: Double?,
    @Expose
    @SerializedName("term_of_transaction_365_long")
    val termOfTransaction365Long: Double?,
    @Expose
    @SerializedName("term_of_transaction_365_short")
    val termOfTransaction365Short: Double?,
    @Expose
    @SerializedName("profit_of_percent_365_long")
    val profitOfPercent365Long: Double?,
    @Expose
    @SerializedName("losing_of_percent_365_long")
    val losingOfPercent365Long: Double?,
    @Expose
    @SerializedName("percent_profit_of_percent_365_long")
    val percentProfitOfPercent365Long: Double?,
    @Expose
    @SerializedName("percent_losing_of_percent_365_long")
    val percentLosingOfPercent365Long: Double?,
    @Expose
    @SerializedName("profit_of_percent_365_short")
    val profitOfPercent365Short: Double?,
    @Expose
    @SerializedName("losing_of_percent_365_short")
    val losingOfPercent365Short: Double?,
    @Expose
    @SerializedName("percent_profit_of_percent_365_short")
    val percentProfitOfPercent365Short: Double?,
    @Expose
    @SerializedName("percent_losing_of_percent_365_short")
    val percentLosingOfPercent365Short: Double?,
    @Expose
    @SerializedName("term_of_transaction_n_deals")
    val termOfTransactionNDeals: Double?,
    @Expose
    @SerializedName("profit_of_percent_n_deals")
    val profitOfPercentNDeals: Double?,
    @Expose
    @SerializedName("losing_of_percent_n_deals")
    val losingOfPercentNDeals: Double?,
    @Expose
    @SerializedName("average_profit_trades_n_deals")
    val averageProfitTradesNDeals: Double?,
    @Expose
    @SerializedName("average_losing_trades_n_deals")
    val averageLosingTradesNDeals: Double?,
    @Expose
    @SerializedName("incr_decr_depo_n_deals")
    val incrDecrDepoNDeals: Double?,
    @Expose
    @SerializedName("color_incr_decr_depo_n_deals")
    val colorIncrDecrDepoNDeals: ColorItem?,
    @Expose
    @SerializedName("ratio_n_deals_long")
    val ratioNDealsLong: Double?,
    @Expose
    @SerializedName("ratio_n_deals_short")
    val ratioNDealsShort: Double?,
    @Expose
    @SerializedName("term_of_transaction_n_deals_long")
    val termOfTransactionNDealsLong: Double?,
    @Expose
    @SerializedName("term_of_transaction_n_deals_short")
    val termOfTransactionNDealsShort: Double?,
    @Expose
    @SerializedName("profit_of_percent_n_deals_long")
    val profitOfPercentNDealsLong: Double?,
    @Expose
    @SerializedName("losing_of_percent_n_deals_long")
    val losingOfPercentNDealsLong: Double?,
    @Expose
    @SerializedName("percent_profit_of_percent_n_deals_long")
    val percentProfitOfPercentNDealsLong: Double?,
    @Expose
    @SerializedName("percent_losing_of_percent_n_deals_long")
    val percentLosingOfPercentNDealsLong: Double?,
    @Expose
    @SerializedName("profit_of_percent_n_deals_short")
    val profitOfPercentNDealsShort: Double?,
    @Expose
    @SerializedName("losing_of_percent_n_deals_short")
    val losingOfPercentNDealsShort: Double?,
    @Expose
    @SerializedName("percent_profit_of_percent_n_deals_short")
    val percentProfitOfPercentNDealsShort: Double?,
    @Expose
    @SerializedName("percent_losing_of_percent_n_deals_short")
    val percentLosingOfPercentNDealsShort: Double?,
    @Expose
    @SerializedName("month_indicators")
    val monthIndicators: List<ResponseMonthIndicators>,
    @Expose
    @SerializedName("audience_data")
    val audienceData: List<ResponseAudienceData>
)