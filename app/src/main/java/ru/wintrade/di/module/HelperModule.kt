package ru.wintrade.di.module

import dagger.Module
import dagger.Provides
import ru.wintrade.ui.App
import ru.wintrade.util.ImageHelper
import javax.inject.Singleton

@Module
class HelperModule {
    @Singleton
    @Provides
    fun imageHelper(app: App): ImageHelper {
        return ImageHelper(app)
    }
}