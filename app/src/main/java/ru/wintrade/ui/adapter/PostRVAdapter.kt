package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trader_news.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.PostRVListPresenter
import ru.wintrade.mvp.view.item.PostItemView
import java.util.*

class PostRVAdapter(val presenter: PostRVListPresenter) :
    RecyclerView.Adapter<PostRVAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_trader_news, parent, false
        )
    )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.itemView.btn_item_trader_news_like.setOnClickListener {
            presenter.postLiked(holder)
        }

        holder.itemView.btn_item_trader_news_dislike.setOnClickListener {
            presenter.postDisliked(holder)
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view),
        PostItemView {
        override var pos: Int = -1

        override fun setNewsDate(date: Date) {
            itemView.tv_item_trader_news_date.text = date.toString()
        }

        override fun setPost(text: String) {
            itemView.tv_item_trader_news_post.text = text
        }

        override fun setLikesCount(likes: Int) {
            itemView.tv_item_trader_news_like_count.text = likes.toString()
        }

        override fun setDislikesCount(dislikesCount: Int) {
            itemView.tv_item_trader_news_dislike_count.text = dislikesCount.toString()
        }

        override fun setImages(images: List<String>?) {
            if (!images.isNullOrEmpty()) {
                itemView.rv_item_trader_news_img.apply {
                    this.layoutManager = GridLayoutManager(context, 2)
                    val adapter = TraderNewsImagesRVAdapter()
                    this.adapter = adapter
                    adapter.setData(images)
                }
            }
        }


    }
}