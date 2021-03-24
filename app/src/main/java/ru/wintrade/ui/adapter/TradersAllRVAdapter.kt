package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_all_traders.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ITradersAllListPresenter
import ru.wintrade.mvp.view.item.TradersAllItemView

class TradersAllRVAdapter(val presenterAll: ITradersAllListPresenter) :
    RecyclerView.Adapter<TradersAllRVAdapter.TradersAllViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TradersAllViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_all_traders, parent, false)
    )

    override fun onBindViewHolder(holder: TradersAllViewHolder, position: Int) {
        holder.pos = position
        presenterAll.bind(holder)
        holder.itemView.layout_all_traders_item.setOnClickListener {
            presenterAll.openTraderStat(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = presenterAll.getCount()

    inner class TradersAllViewHolder(view: View) : RecyclerView.ViewHolder(view),
        TradersAllItemView {
        override var pos = -1
        override fun setTraderFio(fio: String) {
            itemView.item_traders_fio.text = fio
        }

        override fun setTraderFollowerCount(followerCount: Int) {
            itemView.item_traders_followers_count.text = followerCount.toString()
        }
    }
}