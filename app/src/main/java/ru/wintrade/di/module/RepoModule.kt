package ru.wintrade.di.module

import dagger.Module
import dagger.Provides
import ru.wintrade.mvp.model.firebase.FirebaseAuth
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.ActivityHolder
import ru.wintrade.ui.firebase.AndroidFirebaseAuth
import ru.wintrade.ui.resource.AndroidResourceProvider
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


}