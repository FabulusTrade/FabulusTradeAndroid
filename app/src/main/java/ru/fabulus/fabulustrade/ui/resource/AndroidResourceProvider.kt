package ru.fabulus.fabulustrade.ui.resource

import android.content.Context
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider

class AndroidResourceProvider(val context: Context) : ResourceProvider {
    private val loadingImages = listOf("splash", "splash")
    private val onBoardImages = listOf("on_boarding")

    override fun getLoadingImages() = loadingImages.map { getImageIdFromDrawable(it) }
    override fun getOnBoardImages() = onBoardImages.map { getImageIdFromDrawable(it) }
    override fun getStringResource(resource: Int): String {
        return context.resources.getString(resource)
    }

    override fun getColor(resource: Int): Int = context.resources.getColor(resource)

    private fun getImageIdFromDrawable(imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}