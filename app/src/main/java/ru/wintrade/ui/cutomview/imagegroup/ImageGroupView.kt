package ru.wintrade.ui.cutomview.imagegroup

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnNextLayout
import androidx.core.widget.TextViewCompat
import ru.wintrade.R
import ru.wintrade.ui.cutomview.imagegroup.ImageGroupView.Size.*
import ru.wintrade.ui.cutomview.imagegroup.ImageGroupView.Position.*
import kotlin.math.min
import kotlin.math.roundToInt as rnd

class ImageGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.imageGroupViewStyle,
    defStyleRes: Int = R.style.Widget_ImageGroupViewDefStyle
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val MAX_IMAGE_VIEW_COUNT = 4
        private const val ASPECT_RATIO = 16 / 9.0
        private const val UNSPECIFIED_WIDTH_IN_DP = 250.0
        private const val UNSPECIFIED_HEIGHT_IN_DP = UNSPECIFIED_WIDTH_IN_DP / ASPECT_RATIO
    }

    @StyleRes
    private var textAppearanceStyle = 0
    private val images = mutableListOf<String>()
    private var gridSize = 0f
    private var listener: ((index: Int, url: String) -> Unit)? = null
    private var imageLoader: ImageLoader? = null
    private val imageViewList = mutableListOf<ImageView>()
    private var overflowTextView: TextView? = null
    private var rippleColor = Color.TRANSPARENT
    private var clippingDrawable: Drawable? = null

    private val parentHalfWidth get() = measuredWidth * 0.5
    private val parentHalfHeight get() = measuredHeight * 0.5
    private val gridHalfSize get() = gridSize * 0.5

    init {
        context.withStyledAttributes(attrs, R.styleable.ImageGroupView, defStyleAttr, defStyleRes) {
            gridSize = getDimensionOrThrow(R.styleable.ImageGroupView_gridSize)
            textAppearanceStyle = getResourceId(R.styleable.ImageGroupView_overflowTexAppearance, 0)
            rippleColor = getColor(R.styleable.ImageGroupView_rippleColor, 0)
            clippingDrawable = getDrawable(R.styleable.ImageGroupView_clippingDrawable)
        }

        clippingDrawable?.let {
            clipToOutline = true
            background = it
        }
    }

    fun setImages(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)
        prepareViews()
        doOnNextLayout { loadImages() }
        requestLayout()
        invalidate()
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

    private fun loadImages() {
        val imageLoader =
            this.imageLoader ?: throw IllegalArgumentException("ImageLoader is not specified")
        imageLoader.clear()
        imageViewList.forEachIndexed { index, imageView ->
            imageLoader.load(images[index], imageView, index, images.size)
        }
    }

    private fun prepareViews() {
        removeViews()

        repeat(min(images.size, MAX_IMAGE_VIEW_COUNT)) {
            RippleImageView(context, rippleColor).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                imageViewList.add(this)
                addView(this)
            }
        }

        if (images.size > MAX_IMAGE_VIEW_COUNT) {
            dimLastImageView()
            overflowTextView = TextView(context).apply {
                TextViewCompat.setTextAppearance(this, textAppearanceStyle)
                text = "+${images.size - MAX_IMAGE_VIEW_COUNT}"
                gravity = Gravity.CENTER
                includeFontPadding = false
                addView(this)
            }
        }

        listener?.let { setListener(it) }
    }

    private fun removeViews() {
        removeAllViews()
        imageViewList.clear()
        overflowTextView = null
    }

    private fun dimLastImageView() {
        imageViewList[3].setColorFilter(Color.argb(255, 50, 50, 50), PorterDuff.Mode.MULTIPLY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (width, height) = measureView(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(width.rnd(), height.rnd())
        measureChildren()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChildren()
    }

    private fun measureView(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Double, Double> {
        val specWidth = MeasureSpec.getSize(widthMeasureSpec).toDouble()
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec).toDouble()
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        return when {
            specWidthMode == MeasureSpec.UNSPECIFIED && specHeightMode == MeasureSpec.UNSPECIFIED -> {
                val density = resources.displayMetrics.density
                UNSPECIFIED_WIDTH_IN_DP * density to UNSPECIFIED_HEIGHT_IN_DP * density
            }
            specWidthMode == MeasureSpec.UNSPECIFIED -> {
                specHeight * ASPECT_RATIO to specHeight
            }
            specHeightMode == MeasureSpec.UNSPECIFIED -> {
                specWidth to specWidth / ASPECT_RATIO
            }
            else -> {
                specWidth to specHeight
            }
        }
    }

    private fun doLayout(isMeasuring: Boolean = false) {
        when (images.size) {
            0 -> {}
            1 -> if (isMeasuring) measure1() else layout1()
            2 -> if (isMeasuring) measure2() else layout2()
            3 -> if (isMeasuring) measure3() else layout3()
            4 -> if (isMeasuring) measure4() else layout4()
            else -> if (isMeasuring) {
                measure4()
                measureText()
            } else {
                layout4()
                layoutText()
            }
        }
    }

    private fun measureChildren() {
        doLayout(isMeasuring = true)
    }

    private fun layoutChildren() {
        doLayout()
    }

    private enum class Size {
        WHOLE,
        HALF,
        QUARTER
    }

    private fun View.measure(size: Size) {
        val (width, height) = when (size) {
            WHOLE -> this@ImageGroupView.measuredWidth to this@ImageGroupView.measuredHeight
            HALF -> (parentHalfWidth - gridHalfSize).rnd() to this@ImageGroupView.measuredHeight
            QUARTER -> (parentHalfWidth - gridHalfSize).rnd() to (parentHalfHeight - gridHalfSize).rnd()
        }
        measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    private fun measure1() {
        imageViewList[0].measure(WHOLE)
    }

    private fun measure2() {
        imageViewList[0].measure(HALF)
        imageViewList[1].measure(HALF)
    }

    private fun measure3() {
        imageViewList[0].measure(HALF)
        imageViewList[1].measure(QUARTER)
        imageViewList[2].measure(QUARTER)
    }

    private fun measure4() {
        imageViewList.forEach { it.measure(QUARTER) }
    }

    private fun measureText() {
        overflowTextView?.measure(QUARTER)
    }

    private enum class Position {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    private fun View.layout(position: Position) {
        when (position) {
            TOP_LEFT -> layout(0, 0, measuredWidth, measuredHeight)
            TOP_RIGHT -> layout(
                (parentHalfWidth + gridHalfSize).rnd(), 0,
                (parentHalfWidth + gridHalfSize + measuredWidth).rnd(), measuredHeight
            )
            BOTTOM_LEFT -> layout(
                0, (parentHalfHeight + gridHalfSize).rnd(),
                measuredWidth, (parentHalfHeight + gridHalfSize + measuredHeight).rnd()
            )
            BOTTOM_RIGHT -> layout(
                (parentHalfWidth + gridHalfSize).rnd(),
                (parentHalfHeight + gridHalfSize).rnd(),
                (parentHalfWidth + gridHalfSize + measuredWidth).rnd(),
                (parentHalfHeight + gridHalfSize + measuredHeight).rnd()
            )
        }
    }

    private fun layout1() {
        imageViewList[0].layout(TOP_LEFT)
    }

    private fun layout2() {
        imageViewList[0].layout(TOP_LEFT)
        imageViewList[1].layout(TOP_RIGHT)
    }

    private fun layout3() {
        layout2()
        imageViewList[2].layout(BOTTOM_RIGHT)
    }

    private fun layout4() {
        layout2()
        imageViewList[2].layout(BOTTOM_LEFT)
        imageViewList[3].layout(BOTTOM_RIGHT)
    }

    private fun layoutText() {
        overflowTextView?.layout(BOTTOM_RIGHT)
    }

    private class RippleImageView(context: Context, @ColorInt private val color: Int) :
        AppCompatImageView(context) {

        override fun setImageDrawable(drawable: Drawable?) {
            super.setImageDrawable(
                if (color == Color.TRANSPARENT) drawable else wrapWithRipple(drawable)
            )
        }

        private fun wrapWithRipple(original: Drawable?): Drawable =
            RippleDrawable(ColorStateList.valueOf(color), original, null)
    }
}