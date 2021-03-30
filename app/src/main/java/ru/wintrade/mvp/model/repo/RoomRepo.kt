package ru.wintrade.mvp.model.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.room.db.Database
import ru.wintrade.util.mapToProfile
import ru.wintrade.util.mapToRoomProfile

class RoomRepo(val database: Database) {
    fun insertProfile(profile: Profile) = Completable.create { emitter ->
        val roomProfile = mapToRoomProfile(profile)
        database.profileDao().insert(roomProfile)
        emitter.onComplete()
    }.subscribeOn(Schedulers.io())

    fun getProfile() = Single.create<Profile> { emiter ->
        val roomProfile = database.profileDao().get()
        roomProfile?.let {
            val profile = mapToProfile(it)
            emiter.onSuccess(profile)
        } ?: let {
            emiter.onError(RuntimeException())
        }
    }.subscribeOn(Schedulers.io())

    fun deleteProfile() = Completable.create { emitter->
        database.profileDao().delete()
        emitter.onComplete()
    }.subscribeOn(Schedulers.io())
}