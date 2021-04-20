package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_deals_detail.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.IDealsDetailListPresenter
import ru.wintrade.mvp.view.item.DealsDetailItemView
import java.util.*

class DealsDetailRVAdapter(val presenter: IDealsDetailListPresenter) :
    RecyclerView.Adapter<DealsDetailRVAdapter.DetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_deals_detail, parent, false)
        )

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view), DealsDetailItemView {
        override var pos = -1

        override fun setLastDealDate(date: Date) {
            itemView.tv_deals_detail_date.text = date.toString()
        }
    }
}
