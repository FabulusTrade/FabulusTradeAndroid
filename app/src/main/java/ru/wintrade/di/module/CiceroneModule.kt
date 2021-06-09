package ru.wintrade.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CiceroneModule {

    @Singleton
    @Provides
    fun cicerone(): Cicerone<Router> {
        return Cicerone.create()
    }

    @Provides
    fun navigationHolder(cicerone: Cicerone<Router>): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @Provides
    fun router(cicerone: Cicerone<Router>): Router {
        return cicerone.router
    }
}