package ru.wintrade.ui.customview.imagegroup

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnNextLayout
import ru.wintrade.R
import kotlin.math.min

class ImageGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.imageGroupViewStyle,
    defStyleRes: Int = R.style.Widget_ImageGroupViewDefStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val MAX_IMAGE_VIEW_COUNT = 4
    }

    private val images = mutableListOf<String>()
    private val imageViewList = mutableListOf<ImageView>()
    private var listener: ((index: Int, url: String) -> Unit)? = null
    private var imageLoader: ImageLoader? = null
    private var overflowTextView: TextView? = null

    @ColorInt
    private val dimmedImageViewColor =
        ResourcesCompat.getColor(
            resources,
            R.color.image_group_view_dimmed_image_view_color,
            context.theme
        )

    @ColorInt
    private val dimmedOverflownImageViewColor =
        ResourcesCompat.getColor(
            resources,
            R.color.image_group_view_dimmed_overflown_image_view_color,
            context.theme
        )

    @StringRes
    private var overflownTextTemplate = 0

    @StyleRes
    private val styleFromDefAttr: Int = run {
        context.withStyledAttributes(attrs = intArrayOf(defStyleAttr)) {
            return@run getResourceId(0, ResourcesCompat.ID_NULL)
        }
        ResourcesCompat.ID_NULL
    }

    private val styledContext = ContextThemeWrapper(context,
        resources.newTheme().apply {
            setTo(context.theme)
            applyStyle(defStyleRes, true)
            applyStyle(styleFromDefAttr, true)
            applyStyle(attrs?.styleAttribute ?: ResourcesCompat.ID_NULL, true)
        }
    )

    private val inflater = LayoutInflater.from(styledContext)

    private val layoutIds = listOf(
        R.layout.image_group_view_one_image_layout,
        R.layout.image_group_view_two_images_layout,
        R.layout.image_group_view_three_images_layout,
        R.layout.image_group_view_four_images_layout
    )

    private val imageViewIds =
        listOf(R.id.image1, R.id.image2, R.id.image3, R.id.image4)

    private val isOverflown: Boolean get() = images.size > MAX_IMAGE_VIEW_COUNT

    init {
        context.withStyledAttributes(attrs, R.styleable.ImageGroupView, defStyleAttr, defStyleRes) {
            clipToOutline = getBoolean(R.styleable.ImageGroupView_clipToOutline, clipToOutline)
            overflownTextTemplate = getResourceId(
                R.styleable.ImageGroupView_overflownTextTemplate,
                R.string.image_group_view_overflown_text_template
            )
        }
    }

    fun setListener(listener: ((index: Int, url: String) -> Unit)) {
        this.listener = listener
        imageViewList.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                listener.invoke(index, images[index])
            }
        }
    }

    fun setImageLoader(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    fun getImages(): List<String> = images

    fun setImages(newImages: List<String>) {
        images.apply {
            clear()
            addAll(newImages)
        }
        prepareViews()
        requestLayout()
        invalidate()
        doOnNextLayout { loadImages() }
    }

    private fun prepareViews() {
        removeOldViews()
        val imageViewCount = min(images.size, MAX_IMAGE_VIEW_COUNT)
        if (imageViewCount == 0) return
        inflateLayout(imageViewCount)
        setUpImageViews()
        if (isOverflown) setUpOverflownTextView()
    }

    private fun loadImages() {
        val imageLoader = imageLoader ?: throwImageLoaderError()
        imageLoader.clear()
        imageViewList.forEachIndexed { index, imageView ->
            imageLoader.load(images[index], imageView, index, images.size)
        }
    }

    private fun removeOldViews() {
        removeAllViews()
        imageViewList.clear()
        overflowTextView = null
    }

    private fun inflateLayout(imageViewCount: Int) {
        layoutIds[imageViewCount - 1].let { layout ->
            inflater.inflate(layout, this, true)
        }
    }

    private fun throwImageLoaderError(): Nothing =
        throw IllegalArgumentException(context.getString(R.string.image_group_view_image_loader_is_not_specified))

    private fun setUpOverflownTextView() {
        overflowTextView = findViewById<TextView>(R.id.overflow_text_view)?.apply {
            visibility = VISIBLE
            text = getOverflownText()
        }
    }

    private fun getOverflownText() =
        context.getString(overflownTextTemplate, images.size - MAX_IMAGE_VIEW_COUNT)

    private fun setUpImageViews() {
        findImageViews()
        imageViewList.forEach { it.setColorFilter(dimmedImageViewColor) }
        if (isOverflown) imageViewList.getOrNull(3)?.setColorFilter(dimmedOverflownImageViewColor)
        listener?.let { setListener(it) }
    }

    private fun findImageViews() {
        imageViewIds.forEach { id ->
            findViewById<ImageView>(id)?.let { imageView ->
                imageViewList.add(imageView)
            }
        }
    }
}