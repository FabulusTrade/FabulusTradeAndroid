package ru.fabulus.fabulustrade.ui

import android.app.Application
import ru.fabulus.fabulustrade.di.AppComponent
import ru.fabulus.fabulustrade.di.DaggerAppComponent
import ru.fabulus.fabulustrade.di.module.AppModule
import ru.fabulus.fabulustrade.ui.activity.ActivityHolder
import ru.fabulus.fabulustrade.ui.activity.ActivityLifecycleCallback
import javax.inject.Inject

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    @Inject
    lateinit var holder: ActivityHolder

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        appComponent.inject(this)
        registerActivityLifecycleCallbacks(ActivityLifecycleCallback(holder))
    }
}