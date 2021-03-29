package ru.wintrade.util

import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.api.ResponseProfile
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
    profile.deviceToken
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
    profile.deviceToken
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
    deviceToken
)

fun mapToTrader(trader: ResponseTrader) = Trader(
    trader.id,
    trader.username,
    trader.email,
    trader.avatar,
    trader.kval,
    trader.is_trader,
    trader.is_active,
    trader.first_name,
    trader.last_name,
    trader.patronymic,
    trader.date_joined,
    trader.phone,
    trader.trades_count,
    trader.fio,
    trader.followers_count,
    trader.year_profit
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
        trade.subtype,
        trade.profit_count
    )

}