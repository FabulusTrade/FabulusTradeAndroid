package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_all_traders.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.IAllTradersListPresenter
import ru.wintrade.mvp.view.item.AllTradersItemView

class AllTradersRVAdapter(val presenter: IAllTradersListPresenter) :
    RecyclerView.Adapter<AllTradersRVAdapter.AllTradersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AllTradersViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_all_traders, parent, false)
    )

    override fun onBindViewHolder(holder: AllTradersViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class AllTradersViewHolder(view: View) : RecyclerView.ViewHolder(view),
        AllTradersItemView {
        override var pos = -1
        override fun setTraderFio(fio: String) {
            itemView.item_traders_fio.text = fio
        }

        override fun setTraderFollowerCount(followerCount: Int) {
            itemView.item_traders_followers_count.text = followerCount.toString()
        }
    }
}