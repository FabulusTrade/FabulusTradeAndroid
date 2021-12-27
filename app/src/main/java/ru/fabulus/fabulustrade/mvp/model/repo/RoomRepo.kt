package ru.fabulus.fabulustrade.mvp.model.repo

import ru.fabulus.fabulustrade.mvp.model.entity.room.db.Database

class RoomRepo(val database: Database) {
//    fun insertProfile(profile: Profile) = Completable.create { emitter ->
//        val roomProfile = mapToRoomProfile(profile)
//        database.profileDao().insert(roomProfile)
//        emitter.onComplete()
//    }.subscribeOn(Schedulers.io())
//
//    fun getProfile() = Single.create<Profile> { emiter ->
//        val roomProfile = database.profileDao().get()
//        roomProfile?.let {
//            val profile = mapToProfile(it)
//            emiter.onSuccess(profile)
//        } ?: let {
//            emiter.onError(RuntimeException())
//        }
//    }.subscribeOn(Schedulers.io())
//
//    fun deleteProfile() = Completable.create { emitter->
//        database.profileDao().delete()
//        emitter.onComplete()
//    }.subscribeOn(Schedulers.io())
}