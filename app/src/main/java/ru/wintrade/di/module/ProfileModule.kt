package ru.wintrade.di.module

import dagger.Module
import dagger.Provides
import ru.wintrade.mvp.model.entity.Profile
import javax.inject.Singleton

@Module
class ProfileModule {
    @Singleton
    @Provides
    fun profile(): Profile {
        return Profile()
    }
}