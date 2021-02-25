package ru.wintrade.di.module

import dagger.Module
import dagger.Provides
import ru.wintrade.ui.App

@Module
class AppModule(val app: App) {
    @Provides
    fun app(): App {
        return app
    }
}