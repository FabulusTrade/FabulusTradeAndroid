package ru.fabulus.fabulustrade.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.fabulus.fabulustrade.mvp.model.entity.*
import ru.fabulus.fabulustrade.mvp.model.entity.api.*
import ru.fabulus.fabulustrade.mvp.model.entity.common.Pagination
import java.text.SimpleDateFormat
import java.util.*

fun mapToTrader(trader: ResponseTrader) = Trader(
    id = trader.id,
    username = trader.username,
    avatar = trader.avatar,
    kval = trader.kval,
    isActive = trader.is_trader,
    isTrader = trader.is_active,
    dateJoined = trader.date_joined,
    followersCount = trader.followers_count,
    tradesCount = trader.trades_count,
    colorIncrDecrDepo365 = trader.colorIncrDecrDepo365,
    followersForWeekCount = trader.followers_count_7day,
    pinnedPost = mapToPost(trader.pinned_post),
    flashLimit = trader.flashLimit
)

fun mapToTraderStatistic(response: ResponseTraderStatistic): TraderStatistic {
    val monitorIndicators = response.monthIndicators.map { mapToMonthIndicator(it) }
    val audienceData = response.audienceData.map { mapToAudienceData(it) }
    return TraderStatistic(
        idTrader = response.idTrader,
        termOfTransaction30 = response.termOfTransaction30,
        amountDeals30 = response.amountDeals30,
        profitOfPercent30 = response.profitOfPercent30,
        profitTrades30 = response.profitTrades30,
        losingTrades30 = response.losingTrades30,
        averageProfitTrades30 = response.averageProfitTrades30,
        averageLosingTrades30 = response.averageLosingTrades30,
        actualProfit180 = response.actualProfit180,
        actualProfit365 = response.actualProfit365,
        sumProfit365 = response.sumProfit365,
        averageCountOperationsMonth = response.averageCountOperationsMonth,
        termOfTransaction365 = response.termOfTransaction365,
        profitTrades365 = response.profitTrades365,
        losingTrades365 = response.losingTrades365,
        averageProfitTrades365 = response.averageProfitTrades365,
        averageLosingTrades365 = response.averageLosingTrades365,
        incrDecrDepo365 = response.incrDecrDepo365,
        colorIncrDecrDepo365 = response.colorIncrDecrDepo365,
        ratio365Long = response.ratio365Long,
        ratio365Short = response.ratio365Short,
        termOfTransaction365Long = response.termOfTransaction365Long,
        termOfTransaction365Short = response.termOfTransaction365Short,
        profitOfPercent365Long = response.profitOfPercent365Long,
        losingOfPercent365Long = response.losingOfPercent365Long,
        percentProfitOfPercent365Long = response.percentProfitOfPercent365Long,
        percentLosingOfPercent365Long = response.percentLosingOfPercent365Long,
        profitOfPercent365Short = response.profitOfPercent365Short,
        losingOfPercent365Short = response.losingOfPercent365Short,
        percentProfitOfPercent365Short = response.percentProfitOfPercent365Short,
        percentLosingOfPercent365Short = response.percentLosingOfPercent365Short,
        termOfTransactionNDeals = response.termOfTransactionNDeals,
        profitOfPercentNDeals = response.profitOfPercentNDeals,
        losingOfPercentNDeals = response.losingOfPercentNDeals,
        averageProfitTradesNDeals = response.averageProfitTradesNDeals,
        averageLosingTradesNDeals = response.averageLosingTradesNDeals,
        incrDecrDepoNDeals = response.incrDecrDepoNDeals,
        colorIncrDecrDepoNDeals = response.colorIncrDecrDepoNDeals,
        ratioNDealsLong = response.ratioNDealsLong,
        ratioNDealsShort = response.ratioNDealsShort,
        termOfTransactionNDealsLong = response.termOfTransactionNDealsLong,
        termOfTransactionNDealsShort = response.termOfTransactionNDealsShort,
        profitOfPercentNDealsLong = response.profitOfPercentNDealsLong,
        losingOfPercentNDealsLong = response.losingOfPercentNDealsLong,
        percentProfitOfPercentNDealsLong = response.percentProfitOfPercentNDealsLong,
        percentLosingOfPercentNDealsLong = response.percentLosingOfPercentNDealsLong,
        profitOfPercentNDealsShort = response.profitOfPercentNDealsShort,
        losingOfPercentNDealsShort = response.losingOfPercentNDealsShort,
        percentProfitOfPercentNDealsShort = response.percentProfitOfPercentNDealsShort,
        percentLosingOfPercentNDealsShort = response.percentLosingOfPercentNDealsShort,
        monthIndicators = monitorIndicators,
        audienceData = audienceData
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

fun mapToBlacklistItem(responseBlacklistItem: ResponseBlacklistItem) = BlacklistItem(
    responseBlacklistItem.user_id,
    responseBlacklistItem.username,
    responseBlacklistItem.avatar_url,
    responseBlacklistItem.blacklisted_at,
    responseBlacklistItem.year_profit,
    responseBlacklistItem.followers_count
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
        trade.subtype,
        trade.posts,
        trade.term_of_transation
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

fun mapToArgument(argument: ResponseArgument?): Argument? {
    return argument?.let {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateCreate = dateFormat.parse(argument.date_create)
        val dateUpd = dateFormat.parse(argument.date_update)
        val images = argument.images.map { it.image }
        val comments = mapToComments(argument.comments)
        Argument(
            id = argument.id,
            userName = argument.username,
            avatarUrl = argument.avatar,
            followersCount = argument.followersCount,
            traderId = argument.trader_id,
            text = argument.text,
            postStatus = argument.post_status,
            dateCreate = dateCreate,
            dateUpdate = dateUpd,
            pinned = argument.pinned,
            images = images,
            likeCount = argument.like_count,
            dislikeCount = argument.dislike_count,
            isLiked = argument.is_liked,
            isDisliked = argument.is_disliked,
            comments = comments,
            colorIncrDecrDepo365 = argument.colorIncrDecrDepo365,
            repostCount = argument.repostCount,
            isFlashed = argument.isFlashed,
            stopLoss = argument.stop_loss,
            takeProfit = argument.take_profit,
            dealTerm = argument.deal_term,
            closedDealTerm = argument.closed_deal_term
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
        companyTrade.currency,
        companyTrade.posts
    )
}

fun mapToTradeJournalByCompany(companyTrade: ResponseCompanyTradingOperationsJournal): JournalTradesSortedByCompany {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val tradeDate = dateFormat.parse(companyTrade.date)
    return JournalTradesSortedByCompany(
        companyTrade.id,
        companyTrade.operation_type,
        companyTrade.company,
        companyTrade.company_img,
        tradeDate,
        companyTrade.profit_count,
        companyTrade.price,
        companyTrade.currency,
        companyTrade.endCount,
        companyTrade.visible,
        companyTrade.posts
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

fun mapToDeleteCommentResult(responseDeleteComment: ResponseDeleteComment): DeleteCommentResult {
    return DeleteCommentResult(
        responseDeleteComment.result,
        responseDeleteComment.message

    )
}

fun mapToComplaints(responseComplaints: ResponseComplaints): List<Complaint> {
    return responseComplaints.results.map { responseComplaint ->
        mapToComplaint(responseComplaint)
    }.toList()
}

fun mapToComplaint(responseComplaint: ResponseComplaint): Complaint {
    return Complaint(
        responseComplaint.id,
        responseComplaint.text
    )
}

fun mapToBlockUserInfo(responseBlockUserInfo: ResponseBlockUserInfo): BlockUserInfo {
    return BlockUserInfo(
        responseBlockUserInfo.blockedUserId,
        responseBlockUserInfo.commentsBlockTime,
        responseBlockUserInfo.postsBlockTime
    )
}

fun mapToCommentBlockUserInfo(responseCommentBlockedUsers: ResponseBlockUserComments): List<CommentBlockedUser> {
    return responseCommentBlockedUsers.usersList.map { user ->
        CommentBlockedUser(
            user.authorID,
            user.blockedUserID
        )
    }.toList()
}

fun mapToResultBlockUserComment(responseBlockCommentUser: ResponseBlockCommentUser): ResultBlockUserComment {
    return ResultBlockUserComment(responseBlockCommentUser.result)
}

fun mapToResultUnblockUserComment(responseUnblockCommentUser: ResponseUnblockCommentUser): ResultUnblockUserComment {
    return ResultUnblockUserComment(responseUnblockCommentUser.result)
}

fun mapToResultAddToBlacklist(responseAddToBlacklist: ResponseAddToBlacklist): ResultAddToBlacklist {
    return ResultAddToBlacklist(responseAddToBlacklist.result)
}

fun ByteArray.mapToMultipartBodyPart(index: Int): MultipartBody.Part {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
    return MultipartBody.Part.createFormData(
        "image[$index]", "photo${dateFormat.format(Date())}",
        this.toRequestBody("image/*".toMediaTypeOrNull(), 0, this.size)
    )
}

    fun mapArgumentToTrade(argument: Argument): Post {
    argument.apply {
        return Post(
            id,
            userName,
            avatarUrl,
            followersCount,
            traderId,
            text,
            postStatus,
            dateCreate,
            dateUpdate,
            pinned,
            images,
            likeCount,
            dislikeCount,
            isLiked,
            isDisliked,
            comments,
            colorIncrDecrDepo365,
            repostCount,
            isFlashed
        )
    }
}