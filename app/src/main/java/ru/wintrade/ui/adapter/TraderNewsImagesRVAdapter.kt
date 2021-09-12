package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trader_news_item_image.view.*
import ru.wintrade.R
import ru.wintrade.util.loadImage

class TraderNewsImagesRVAdapter :
    RecyclerView.Adapter<TraderNewsImagesRVAdapter.TraderNewsImageViewHolder>() {

    val images = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TraderNewsImageViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_trader_news_item_image, parent, false
        )
    )

    override fun onBindViewHolder(holder: TraderNewsImageViewHolder, position: Int) {
        holder.setImage(position)
    }

    override fun getItemCount() = images.size

    fun setData(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    inner class TraderNewsImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun setImage(pos: Int) {
            loadImage(images[pos], itemView.iv_item_trader_news_item_image)
        }
    }
}