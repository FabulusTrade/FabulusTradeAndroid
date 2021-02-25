package ru.wintrade.ui

import android.app.Application
import ru.wintrade.di.AppComponent
import ru.wintrade.di.DaggerAppComponent
import ru.wintrade.di.module.AppModule

class App: Application() {
    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}