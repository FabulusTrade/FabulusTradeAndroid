package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemTradesByCompanyBinding
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
        holder.binding.cvItemTradesByCompany.setOnClickListener {
            presenter.clicked(holder.pos)
        }
    }

    override fun getItemCount() = presenter.getCount()

    inner class TradesViewHolder(view: View) : RecyclerView.ViewHolder(view), TraderTradeItemView {

        val binding = ItemTradesByCompanyBinding.bind(itemView)

        override var pos = -1

        override fun setCompanyName(name: String) {
            binding.tvItemTradesByCompanyName.text = name
        }

        override fun setCompanyLogo(url: String) {
            loadImage(url, binding.ivItemTradesByCompanyLogo)
        }

        override fun setCompanyTradeCount(count: String) {
            binding.tvItemTradesByCompanyCounter.text = count
        }

        override fun setCompanyLastTradeDate(date: String) {
            binding.tvItemTradesByCompanyDate.text = date
        }
    }
}