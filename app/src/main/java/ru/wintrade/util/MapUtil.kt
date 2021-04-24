package ru.wintrade.util

import ru.wintrade.mvp.model.entity.*
import ru.wintrade.mvp.model.entity.api.*
import ru.wintrade.mvp.model.entity.common.Pagination
import ru.wintrade.mvp.model.entity.room.RoomProfile
import java.text.SimpleDateFormat

fun mapToRoomProfile(profile: Profile) = RoomProfile(
    profile.id,
    profile.username,
    profile.email,
    profile.avatar,
    profile.kval,
    profile.isTrader,
    profile.firstName,
    profile.lastName,
    profile.patronymic,
    profile.dateJoined,
    profile.phone,
    profile.followersCount,
    profile.subscriptionsCount,
    profile.token,
    profile.deviceToken,
)

fun mapToProfile(profile: RoomProfile) = Profile(
    profile.remoteId,
    profile.username,
    profile.email,
    profile.avatar,
    profile.kval,
    profile.isTrader,
    profile.firstName,
    profile.lastName,
    profile.patronymic,
    profile.dateJoined,
    profile.phone,
    profile.followersCount,
    profile.subscriptionsCount,
    profile.token,
    profile.deviceToken,
)

fun mapToProfile(profile: ResponseProfile, token: String, deviceToken: String) = Profile(
    profile.id,
    profile.username,
    profile.email,
    profile.avatar,
    profile.kval,
    profile.is_trader,
    profile.first_name,
    profile.last_name,
    profile.patronymic,
    profile.date_joined,
    profile.phone,
    profile.followers_count,
    profile.subscriptions_count,
    token,
    deviceToken,
)

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
    trader.year_profit,
    trader.followers_count_7day,
    mapToPost(trader.pinned_post)
)

fun mapToSubscription(subscription: ResponseSubscription) = Subscription(
    mapToTrader(subscription.id_trader),
    subscription.end_date,
    subscription.id_status
)

fun mapToTrade(trade: ResponseTrade, trader: Trader): Trade {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val date = dateFormat.parse(trade.date)
    return Trade(
        trade.id,
        trade.trader,
        trader,
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
        trade.profit_count
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateCreate = dateFormat.parse(post.date_create)
        val dateUpd = dateFormat.parse(post.date_update)
        val images = post.images.map { it.image }
        return Post(
            post.id,
            post.text,
            post.post_status,
            dateCreate,
            dateUpd,
            post.pinned,
            images
        )
    } ?: return null
}