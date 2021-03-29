package ru.wintrade.di.module

import dagger.Module
import dagger.Provides
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import javax.inject.Singleton

@Module
class ProfileModule {
    @Singleton
    @Provides
    fun profileStorage(): ProfileStorage {
        return ProfileStorage()
    }
}