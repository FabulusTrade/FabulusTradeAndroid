package ru.wintrade.util

import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.api.ResponseProfile
import ru.wintrade.mvp.model.entity.api.ResponseSubscription
import ru.wintrade.mvp.model.entity.api.ResponseTrade
import ru.wintrade.mvp.model.entity.api.ResponseTrader
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
    trader.trades_count,
    trader.followers_count,
    trader.year_profit,
    trader.followers_count_7day
)

fun mapToSubscription(subscription: ResponseSubscription) = Trader(
    subscription.id_trader.id,
    subscription.id_trader.username,
    subscription.id_trader.avatar,
    subscription.id_trader.kval,
    subscription.id_trader.isActive,
    subscription.id_trader.isTrader,
    subscription.id_trader.dateJoined,
    subscription.id_trader.tradesCount,
    subscription.id_trader.followersCount,
    subscription.id_trader.yearProfit,
    subscription.id_trader.followersForWeekCount
)

fun mapToTrade(trade: ResponseTrade): Trade {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val date = dateFormat.parse(trade.date)
    return Trade(
        trade.id,
        trade.trader,
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