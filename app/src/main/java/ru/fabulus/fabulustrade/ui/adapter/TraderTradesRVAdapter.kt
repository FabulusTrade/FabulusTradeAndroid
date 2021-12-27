package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trades_by_company.view.*
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ITraderTradesRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.TraderTradeItemView
import ru.fabulus.fabulustrade.util.loadImage

class TraderTradesRVAdapter(val presenter: ITraderTradesRVListPresenter) :
    RecyclerView.Adapter<TraderTradesRVAdapter.TradesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradesViewHolder =
        TradesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trades_by_company, parent, false)
        )

    override fun onBindViewHolder(holder: TradesViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.itemView.cv_item_trades_by_company.setOnClickListener {
            presenter.clicked(holder.pos)
        }
    }

    override fun getItemCount() = presenter.getCount()

    inner class TradesViewHolder(view: View) : RecyclerView.ViewHolder(view), TraderTradeItemView {
        override var pos = -1

        override fun setCompanyName(name: String) {
            itemView.tv_item_trades_by_company_name.text = name
        }

        override fun setCompanyLogo(url: String) {
            loadImage(url, itemView.iv_item_trades_by_company_logo)
        }

        override fun setCompanyTradeCount(count: String) {
            itemView.tv_item_trades_by_company_counter.text = count
        }

        override fun setCompanyLastTradeDate(date: String) {
            itemView.tv_item_trades_by_company_date.text = date
        }
    }
}