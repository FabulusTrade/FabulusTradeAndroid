package ru.fabulus.fabulustrade.ui.customview.imagegroup

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import java.lang.ref.WeakReference

class ImageLoaderImpl(@DrawableRes private val placeHolder: Int = 0) : ImageLoader {

    private val targetList = mutableSetOf<WeakReference<ViewTarget<ImageView, Drawable>>>()

    private val transitionOptions = withCrossFade(
        DrawableCrossFadeFactory.Builder().apply {
            setCrossFadeEnabled(false)
        })

    override fun load(url: String, imageView: ImageView, index: Int, count: Int) {
        val target = Glide.with(imageView)
            .load(url)
            .placeholder(placeHolder)
            .transition(transitionOptions)
            .into(imageView)

        targetList.add(WeakReference(target))
    }

    override fun clear() {
        targetList.forEach { reference ->
            reference.get()?.let { target -> Glide.with(target.view).clear(target.view) }
        }
        targetList.clear()
    }
}