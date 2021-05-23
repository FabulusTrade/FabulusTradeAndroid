package ru.wintrade.mvp.model.datasource.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.wintrade.mvp.model.entity.UserProfile
import ru.wintrade.mvp.model.entity.room.RoomUserProfile
import ru.wintrade.mvp.model.entity.room.dao.UserProfileDao

class UserProfileLocalDataSource(val dao: UserProfileDao) {
    fun get(id: String): Single<UserProfile> = Single.create<UserProfile> { emitter ->
        val roomUserProfile = dao.get(id)
        roomUserProfile?.let { profile ->
            val userProfile = UserProfile(
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
                profile.subscriptionsCount
            )
            emitter.onSuccess(userProfile)
        } ?: emitter.onError(Exception())
    }.subscribeOn(Schedulers.io())

    fun save(userProfile: UserProfile): Completable = Completable.fromAction {
        val roomUserProfile = RoomUserProfile(
            userProfile.id,
            userProfile.username,
            userProfile.email,
            userProfile.avatar,
            userProfile.kval,
            userProfile.isTrader,
            userProfile.firstName,
            userProfile.lastName,
            userProfile.patronymic,
            userProfile.dateJoined,
            userProfile.phone,
            userProfile.followersCount,
            userProfile.subscriptionsCount
        )
        dao.insert(roomUserProfile)
    }.subscribeOn(Schedulers.io())
}