package ru.wintrade.util

import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderNews
import ru.wintrade.mvp.model.entity.api.*
import ru.wintrade.mvp.model.entity.room.RoomProfile
import java.text.SimpleDateFormat

fun mapToRoomProfile(profile: Profile) = RoomProfile(
    profile.id,
    profile.username,
    profile.email,
    profile.avatar,
    profile.firstName,
    profile.lastName,
    profile.patronymic,
    profile.phone,
    profile.token,
    profile.deviceToken,
    profile.subscriptions_count
)

fun mapToProfile(profile: RoomProfile) = Profile(
    profile.remoteId,
    profile.username,
    profile.email,
    profile.avatar,
    profile.firstName,
    profile.lastName,
    profile.patronymic,
    profile.phone,
    profile.token,
    profile.deviceToken,
    profile.subscriptions_count
)

fun mapToProfile(profile: ResponseProfile, token: String, deviceToken: String) = Profile(
    profile.id,
    profile.username,
    profile.email,
    profile.avatar,
    profile.firstName,
    profile.lastName,
    profile.patronymic,
    profile.phone,
    token,
    deviceToken,
    profile.subscriptions_count
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
    trader.followers_count_7day
)

fun mapToSubscription(subscription: ResponseSubscription) = Trader(
    subscription.id_trader.id,
    subscription.id_trader.username,
    subscription.id_trader.avatar,
    subscription.id_trader.kval,
    subscription.id_trader.is_active,
    subscription.id_trader.is_trader,
    subscription.id_trader.date_joined,
    subscription.id_trader.followers_count,
    subscription.id_trader.trades_count,
    subscription.id_trader.year_profit,
    subscription.id_trader.followers_count_7day
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

fun mapToNews(traderNews: ResponseTraderNews): TraderNews {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val dateCreate = dateFormat.parse(traderNews.date_create)
    val dateUpd = dateFormat.parse(traderNews.date_update)
    return TraderNews(
        traderNews.id,
        traderNews.text,
        traderNews.post_status,
        dateCreate,
        dateUpd,
        traderNews.pinned,
    )
}