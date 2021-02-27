package ru.wintrade.di.module

import dagger.Module
import dagger.Provides
import ru.wintrade.ui.App
import ru.wintrade.util.ResourceHelper
import javax.inject.Singleton

@Module
class ResourceModule {
    @Singleton
    @Provides
    fun resourceHelper(context: App): ResourceHelper {
        return ResourceHelper(context)
    }
}