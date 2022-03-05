package ru.fabulus.fabulustrade.util

import ru.fabulus.fabulustrade.mvp.model.entity.*
import ru.fabulus.fabulustrade.mvp.model.entity.api.*
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
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
    trader.colorIncrDecrDepo365,
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
        trade.currency,
        date,
        trade.profit_count,
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
    return post?.let {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateCreate = dateFormat.parse(post.date_create)
        val dateUpd = dateFormat.parse(post.date_update)
        val images = post.images.map { it.image }
        val comments = mapToComments(post.comments)
        Post(
            id = post.id,
            userName = post.username,
            avatarUrl = post.avatar,
            followersCount = post.followersCount,
            traderId = post.trader_id,
            text = post.text,
            postStatus = post.post_status,
            dateCreate = dateCreate,
            dateUpdate = dateUpd,
            pinned = post.pinned,
            images = images,
            likeCount = post.like_count,
            dislikeCount = post.dislike_count,
            isLiked = post.is_liked,
            isDisliked = post.is_disliked,
            comments = comments,
            colorIncrDecrDepo365 = post.colorIncrDecrDepo365,
            repostCount = post.repostCount,
            isFlashed = post.isFlashed
        )
    }
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

fun mapToComments(responseComments: List<ResponseComment>): MutableList<Comment> {
    return responseComments.map { responseComment ->
        mapToComment(responseComment)
    }.toMutableList()
}

fun mapToComment(responseComment: ResponseComment): Comment {
    return Comment(
        responseComment.id,
        responseComment.postId,
        responseComment.parentCommentId,
        responseComment.authorUuid,
        responseComment.authorUsername,
        responseComment.avatarUrl,
        responseComment.text,
        responseComment.dateCreate,
        responseComment.dateUpdate,
        responseComment.likeCount,
        responseComment.dislikeCount,
        responseComment.isLiked
    )
}

fun mapToIncPostResult(responseIncRepostCount: ResponseIncRepostCount): IncPostResult {
    return IncPostResult(
        responseIncRepostCount.result,
        responseIncRepostCount.message
    )
}