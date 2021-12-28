package ru.fabulus.fabulustrade.di.module

import dagger.Module
import dagger.Provides
import ru.fabulus.fabulustrade.ui.App

@Module
class AppModule(val app: App) {
    @Provides
    fun app(): App {
        return app
    }
}