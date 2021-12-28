package ru.fabulus.fabulustrade.mvp.model.datasource.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.room.RoomProfile
import ru.fabulus.fabulustrade.mvp.model.entity.room.dao.ProfileDao

class ProfileLocalDataSource(
    val profileDao: ProfileDao,
    val userProfileLocalDataSource: UserProfileLocalDataSource
) {
    fun get(): Single<Profile> = Single.create<Profile> { emitter ->
        val roomProfile = profileDao.get()
        if (roomProfile == null) {
            val profile = Profile(null, null, null, false)
            profileDao.insert(mapToRoom(profile))
            emitter.onSuccess(profile)
        } else {
            mapFromRoom(roomProfile).subscribe({ profile ->
                emitter.onSuccess(profile)
            }, {
                // Ошибка не обрабатывается
            })
        }
    }.subscribeOn(Schedulers.io())

    fun save(profile: Profile): Completable = Completable.create { emitter ->
        profileDao.insert(mapToRoom(profile))
        if (profile.user == null)
            emitter.onComplete()
        else
            userProfileLocalDataSource
                .save(profile.user!!)
                .subscribe({
                    emitter.onComplete()
                }, {
                    // Ошибка не обрабатывается
                })
    }.subscribeOn(Schedulers.io())

    private fun mapToRoom(profile: Profile): RoomProfile = RoomProfile(
        userId = profile.user?.id,
        token = profile.token,
        deviceToken = profile.deviceToken,
        hasVisitedTutorial = profile.hasVisitedTutorial
    )

    private fun mapFromRoom(roomProfile: RoomProfile): Single<Profile> {
        return roomProfile.userId?.let { id ->
            userProfileLocalDataSource
                .get(id)
                .flatMap { userProfile ->
                    Single.just(
                        Profile(
                            userProfile,
                            roomProfile.token,
                            roomProfile.deviceToken,
                            roomProfile.hasVisitedTutorial
                        )
                    )
                }
        } ?: Single.just(Profile(null, null, null, roomProfile.hasVisitedTutorial))
    }
}