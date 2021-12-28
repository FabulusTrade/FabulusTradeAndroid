package ru.fabulus.fabulustrade.di.module

import dagger.Module
import dagger.Provides
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import javax.inject.Singleton

@Module
class ProfileModule {
    @Singleton
    @Provides
    fun profile(): Profile {
        return Profile(null, null, null, false)
    }
}