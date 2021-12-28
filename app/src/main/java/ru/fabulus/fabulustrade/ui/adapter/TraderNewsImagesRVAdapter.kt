package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import kotlinx.android.synthetic.main.item_trader_news_item_image.view.*
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.loadImage
import javax.inject.Inject

class TraderNewsImagesRVAdapter :
    RecyclerView.Adapter<TraderNewsImagesRVAdapter.TraderNewsImageViewHolder>() {

    val images = mutableListOf<String>()

    @Inject
    lateinit var router: Router

    init {
        App.instance.appComponent.inject(this)
    }

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
            setClickListener(pos)
        }

        // Открываем активити для рассмотра картинки, передаем туда веб адрес самой картинки
        private fun setClickListener(pos: Int) {
            itemView.iv_item_trader_news_item_image.setOnClickListener {
                router.navigateTo(Screens.imageBrowsingFragment(listOf(images[pos])))
            }
        }

    }
}