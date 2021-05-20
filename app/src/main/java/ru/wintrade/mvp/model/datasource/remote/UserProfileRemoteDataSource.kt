package ru.wintrade.mvp.model.datasource.remote

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.wintrade.mvp.model.api.WinTradeApi
import ru.wintrade.mvp.model.entity.UserProfile

class UserProfileRemoteDataSource(val api: WinTradeApi) {
    fun get(token: String): Single<UserProfile> =
        api.getUserProfile(token).flatMap { responseUserProfile ->
            Single.just(
                UserProfile(
                    responseUserProfile.id,
                    responseUserProfile.username,
                    responseUserProfile.email,
                    responseUserProfile.avatar,
                    responseUserProfile.kval,
                    responseUserProfile.is_trader,
                    responseUserProfile.first_name,
                    responseUserProfile.last_name,
                    responseUserProfile.patronymic,
                    responseUserProfile.date_joined,
                    responseUserProfile.phone,
                    responseUserProfile.followers_count,
                    responseUserProfile.subscriptions_count
                )
            )
        }.subscribeOn(Schedulers.io())
}