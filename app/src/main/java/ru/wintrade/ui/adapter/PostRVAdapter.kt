package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trader_news.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.PostRVListPresenter
import ru.wintrade.mvp.view.item.PostItemView
import ru.wintrade.util.showLongToast
import java.util.*

class PostRVAdapter(val presenter: PostRVListPresenter) :
    RecyclerView.Adapter<PostRVAdapter.PostViewHolder>() {
    companion object {
        const val MAX_LINES = 5000
        const val MIN_LINES = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_trader_news, parent, false
        )
    )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        initListeners(holder)
    }

    private fun initListeners(holder: PostViewHolder) {
        holder.itemView.btn_item_trader_news_like.setOnClickListener {
            presenter.postLiked(holder)
        }
        holder.itemView.btn_item_trader_news_dislike.setOnClickListener {
            presenter.postDisliked(holder)
        }
        holder.itemView.btn_item_trader_news_menu.setOnClickListener {
            initMenu(holder)
        }
        holder.itemView.btn_item_trader_news_show_text.setOnClickListener {
            presenter.setPublicationTextMaxLines(holder)
        }
    }

    private fun initMenu(holder: PostViewHolder) {
        val menu = PopupMenu(holder.itemView.context, holder.itemView.btn_item_trader_news_menu)
        menu.inflate(R.menu.menu_publication)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.publication_share -> {
                    holder.itemView.context?.showLongToast("Поделиться")
                    return@setOnMenuItemClickListener true
                }
                R.id.publication_text_edit -> {
                    presenter.postUpdate(holder)
                    return@setOnMenuItemClickListener true
                }
                R.id.publication_text_delete -> {
                    presenter.postDelete(holder)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        menu.show()
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view),
        PostItemView {
        override var pos: Int = -1
        override var isOpen: Boolean = false

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
                    this.layoutManager = LinearLayoutManager(context)
                    val adapter = TraderNewsImagesRVAdapter()
                    this.adapter = adapter
                    adapter.setData(images)
                }
            }
        }

        override fun setLikeImage(isLiked: Boolean) {
            if (isLiked)
                itemView.btn_item_trader_news_like.setImageResource(R.drawable.ic_like)
            else itemView.btn_item_trader_news_like.setImageResource(R.drawable.ic_like_inactive)
        }

        override fun setDislikeImage(isDisliked: Boolean) {
            if (isDisliked)
                itemView.btn_item_trader_news_dislike.setImageResource(R.drawable.ic_dislike)
            else itemView.btn_item_trader_news_dislike.setImageResource(R.drawable.ic_dislike_inactive)
        }

        override fun setKebabMenuVisibility(isVisible: Boolean) {
            if (isVisible)
                itemView.btn_item_trader_news_menu.visibility = View.VISIBLE
            else itemView.btn_item_trader_news_menu.visibility = View.GONE
        }

        override fun setPublicationItemTextMaxLines(isOpen: Boolean) {
            if (isOpen) {
                itemView.tv_item_trader_news_post.maxLines = MAX_LINES
                itemView.btn_item_trader_news_show_text.text =
                    itemView.context.resources.getText(R.string.hide)
            } else {
                itemView.tv_item_trader_news_post.maxLines = MIN_LINES
                itemView.btn_item_trader_news_show_text.text =
                    itemView.context.resources.getText(R.string.show)
            }
        }
    }
}