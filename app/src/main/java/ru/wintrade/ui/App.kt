package ru.wintrade.ui

import android.app.Application
import ru.wintrade.di.AppComponent
import ru.wintrade.di.DaggerAppComponent
import ru.wintrade.di.module.AppModule
import ru.wintrade.ui.activity.ActivityHolder
import ru.wintrade.ui.activity.ActivityLifecycleCallback
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