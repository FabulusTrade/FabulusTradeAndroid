package ru.wintrade.mvp.model.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.wintrade.mvp.model.datasource.local.ProfileLocalDataSource
import ru.wintrade.mvp.model.datasource.remote.UserProfileRemoteDataSource
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.UserProfile
import ru.wintrade.mvp.model.network.NetworkStatus

class ProfileRepo(
    val profileLocalDataSource: ProfileLocalDataSource,
    val userProfileRemoteDataSource: UserProfileRemoteDataSource,
    val networkStatus: NetworkStatus
) {

    fun get(): Single<Profile> = profileLocalDataSource.get().flatMap { profile ->
        if (profile.user != null) {
            networkStatus
                .isOnlineSingle()
                .flatMap { isOnline ->
                    if (isOnline) {
                        userProfileRemoteDataSource
                            .get(profile.token!!)
                            .flatMap { user ->
                                profile.user = user
                                profileLocalDataSource.save(profile)
                                Single.just(profile)
                            }
                    } else {
                        Single.just(profile)
                    }
                }
        } else
            Single.just(profile)
    }

    fun getRemoteUserProfile(profile: Profile): Single<UserProfile> =
        userProfileRemoteDataSource.get(profile.token!!)

    fun save(profile: Profile): Completable = profileLocalDataSource.save(profile)
}