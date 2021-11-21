package ru.wintrade.util

import ru.wintrade.mvp.model.entity.*
import ru.wintrade.mvp.model.entity.api.*
import ru.wintrade.mvp.model.entity.common.Pagination
import java.text.SimpleDateFormat
import java.util.*

fun mapToTrader(trader: ResponseTrader) = Trader(
    trader.id,
    trader.username,
    trader.avatar,
    trader.kval,
    trader.is_trader,
    trader.is_active,
    trader.date_joined,
    trader.followers_count,
    trader.trades_count,
    trader.incr_decr_depo_365,
    trader.followers_count_7day,
    mapToPost(trader.pinned_post)
)

fun mapToTraderStatistic(response: ResponseTraderStatistic): TraderStatistic {
    val monitorIndicators = response.monthIndicators.map { mapToMonthIndicator(it) }
    val audienceData = response.audienceData.map { mapToAudienceData(it) }
    return TraderStatistic(
        response.idTrader,
        response.termOfTransaction30,
        response.amountDeals30,
        response.profitOfPercent30,
        response.profitTrades30,
        response.losingTrades30,
        response.averageProfitTrades30,
        response.averageLosingTrades30,
        response.actualProfit180,
        response.actualProfit365,
        response.sumProfit365,
        response.averageCountOperationsMonth,
        response.termOfTransaction365,
        response.profitTrades365,
        response.losingTrades365,
        response.averageProfitTrades365,
        response.averageLosingTrades365,
        response.incrDecrDepo365,
        response.colorIncrDecrDepo365,
        response.ratio365Long,
        response.ratio365Short,
        response.termOfTransaction365Long,
        response.termOfTransaction365Short,
        response.profitOfPercent365Long,
        response.losingOfPercent365Long,
        response.percentProfitOfPercent365Long,
        response.percentLosingOfPercent365Long,
        response.profitOfPercent365Short,
        response.losingOfPercent365Short,
        response.percentProfitOfPercent365Short,
        response.percentLosingOfPercent365Short,
        response.termOfTransactionNDeals,
        response.profitOfPercentNDeals,
        response.losingOfPercentNDeals,
        response.averageProfitTradesNDeals,
        response.averageLosingTradesNDeals,
        response.incrDecrDepoNDeals,
        response.colorIncrDecrDepoNDeals,
        response.ratioNDealsLong,
        response.ratioNDealsShort,
        response.termOfTransactionNDealsLong,
        response.termOfTransactionNDealsShort,
        response.profitOfPercentNDealsLong,
        response.losingOfPercentNDealsLong,
        response.percentProfitOfPercentNDealsLong,
        response.percentLosingOfPercentNDealsLong,
        response.percentProfitOfPercentNDealsShort,
        response.losingOfPercentNDealsShort,
        response.percentProfitOfPercentNDealsShort,
        response.percentLosingOfPercentNDealsShort,
        monitorIndicators,
        audienceData
    )
}

fun mapToMonthIndicator(response: ResponseMonthIndicators) = MonthIndicator(
    response.year,
    response.month,
    response.actualProfitMonth,
    response.colorActualItemMonth
)

fun mapToAudienceData(response: ResponseAudienceData) = AudienceData(
    response.dateJoined,
    response.followersCount,
    response.observerCount
)

fun mapToSubscription(subscription: ResponseSubscription) = Subscription(
    mapToTrader(subscription.id_trader),
    subscription.end_date,
    subscription.id_status
)

fun mapToTrade(trade: ResponseTrade): Trade {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = dateFormat.parse(trade.date)
    return Trade(
        trade.id,
        trade.trader,
        null,
        trade.operation_type,
        trade.company,
        trade.company_img,
        trade.ticker,
        trade.order_status,
        trade.order_num,
        trade.price,
        trade.count,
        trade.currency,
        date,
        trade.profit_count,
        trade.value,
        trade.subtype
    )
}

fun <T, V> mapToPagination(
    responsePagination: ResponsePagination<V>,
    results: List<T>
): Pagination<T> =
    Pagination(
        responsePagination.count,
        responsePagination.next,
        responsePagination.previous,
        results
    )

fun mapToPost(post: ResponsePost?): Post? {
    post?.let {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateCreate = dateFormat.parse(post.date_create)
        val dateUpd = dateFormat.parse(post.date_update)
        val images = post.images.map { it.image }
        return Post(
            post.id,
            post.trader_id,
            post.text,
            post.post_status,
            dateCreate,
            dateUpd,
            post.pinned,
            images,
            post.like_count,
            post.dislike_count,
            post.is_liked,
            post.is_disliked
        )
    } ?: return null
}

fun mapToAggregatedTrade(trade: ResponseAggregatedTrade?): TradesByCompanyAggregated? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val lastTradeDate = dateFormat.parse(trade?.date_last)
    return if (trade != null) {
        TradesByCompanyAggregated(
            trade.id_company,
            trade.company,
            trade.company_img,
            trade.count,
            lastTradeDate
        )
    } else null
}

fun mapToTradeByCompany(companyTrade: ResponseCompanyTradingOperations): TradesSortedByCompany {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val tradeDate = dateFormat.parse(companyTrade.date)
    return TradesSortedByCompany(
        companyTrade.id,
        companyTrade.operation_type,
        companyTrade.company,
        companyTrade.company_img,
        tradeDate,
        companyTrade.profit_count,
        companyTrade.price,
        companyTrade.currency
    )
}