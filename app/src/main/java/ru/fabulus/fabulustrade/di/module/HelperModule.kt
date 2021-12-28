package ru.fabulus.fabulustrade.di.module

import dagger.Module
import dagger.Provides
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.ImageHelper
import javax.inject.Singleton

@Module
class HelperModule {
    @Singleton
    @Provides
    fun imageHelper(app: App): ImageHelper {
        return ImageHelper(app)
    }
}