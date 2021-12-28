package ru.fabulus.fabulustrade.di.module

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.fabulus.fabulustrade.mvp.model.api.WinTradeApi
import ru.fabulus.fabulustrade.mvp.model.datasource.local.ProfileLocalDataSource
import ru.fabulus.fabulustrade.mvp.model.datasource.local.UserProfileLocalDataSource
import ru.fabulus.fabulustrade.mvp.model.datasource.remote.UserProfileRemoteDataSource
import ru.fabulus.fabulustrade.mvp.model.entity.room.db.Database
import ru.fabulus.fabulustrade.mvp.model.firebase.FirebaseAuth
import ru.fabulus.fabulustrade.mvp.model.network.NetworkStatus
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.repo.ProfileRepo
import ru.fabulus.fabulustrade.mvp.model.repo.RoomRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.activity.ActivityHolder
import ru.fabulus.fabulustrade.ui.firebase.AndroidFirebaseAuth
import ru.fabulus.fabulustrade.ui.resource.AndroidResourceProvider
import javax.inject.Singleton

@Module
class RepoModule {
    @Singleton
    @Provides
    fun resourceProvider(app: App): ResourceProvider {
        return AndroidResourceProvider(app)
    }

    @Singleton
    @Provides
    fun firebaseAuth(holder: ActivityHolder): FirebaseAuth {
        return AndroidFirebaseAuth(holder)
    }

    @Singleton
    @Provides
    fun holder() = ActivityHolder()

    @Singleton
    @Provides
    fun apiRepo(api: WinTradeApi, networkStatus: NetworkStatus): ApiRepo {
        return ApiRepo(api, networkStatus)
    }

    @Singleton
    @Provides
    fun database(app: App): Database {
        return Room.databaseBuilder(app, Database::class.java, Database.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun roomRepo(database: Database): RoomRepo {
        return RoomRepo(database)
    }

    @Singleton
    @Provides
    fun userProfileRemoteDataSource(api: WinTradeApi): UserProfileRemoteDataSource {
        return UserProfileRemoteDataSource(api)
    }

    @Singleton
    @Provides
    fun userProfileLocalDataSource(database: Database): UserProfileLocalDataSource {
        return UserProfileLocalDataSource(database.userProfileDao())
    }

    @Singleton
    @Provides
    fun profileLocalDataSource(
        database: Database,
        userProfileLocalDataSource: UserProfileLocalDataSource
    ): ProfileLocalDataSource {
        return ProfileLocalDataSource(database.profileDao(), userProfileLocalDataSource)
    }

    @Singleton
    @Provides
    fun profileRepo(
        profileLocalDataSource: ProfileLocalDataSource,
        userProfileRemoteDataSource: UserProfileRemoteDataSource,
        networkStatus: NetworkStatus
    ): ProfileRepo {
        return ProfileRepo(
            profileLocalDataSource,
            userProfileRemoteDataSource,
            networkStatus
        )
    }
}