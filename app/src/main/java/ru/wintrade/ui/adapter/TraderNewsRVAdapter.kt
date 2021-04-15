package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trader_news.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ITraderNewsRVListPresenter
import ru.wintrade.mvp.view.item.TraderNewsItemView
import java.util.*

class TraderNewsRVAdapter(val presenter: ITraderNewsRVListPresenter) :
    RecyclerView.Adapter<TraderNewsRVAdapter.TraderNewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TraderNewsViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_trader_news, parent, false
        )
    )

    override fun onBindViewHolder(holder: TraderNewsViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.itemView.btn_item_trader_news_show.setOnClickListener {
            holder.itemView.tv_item_trader_news_post.maxLines = 0
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class TraderNewsViewHolder(view: View) : RecyclerView.ViewHolder(view),
        TraderNewsItemView {
        override var pos: Int = -1

        override fun setNewsDate(date: Date) {
            itemView.tv_item_trader_news_date.text = date.toString()
        }

        override fun setPost(text: String) {
            itemView.tv_item_trader_news_post.text = text
        }
    }
}