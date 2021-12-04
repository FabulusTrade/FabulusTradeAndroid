package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import com.ortiz.touchview.OnTouchImageViewListener
import com.ortiz.touchview.TouchImageView
import ru.wintrade.R
import ru.wintrade.util.cancelImageLoading
import ru.wintrade.util.loadImage

internal class ImageBrowsingAdapter(
    private val imageUrls: List<String>,
    private val enableUserInput: (isEnabled: Boolean) -> Unit
) :
    RecyclerView.Adapter<ImageBrowsingAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        TouchImageView(parent.context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onViewRecycled(holder: Holder) {
        holder.recycle()
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = (view as TouchImageView).apply {
            setOnTouchImageViewListener(object : OnTouchImageViewListener {
                override fun onMove() {
                    enableUserInput(!isZoomed)
                }
            })
        }

        fun bind(url: String) {
            loadImage(url, imageView, R.drawable.img_placeholder)
        }

        fun recycle() {
            cancelImageLoading(imageView)
        }
    }
}
