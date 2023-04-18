package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemTradesByCompanyBinding
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ITradesByCompanyListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.TradesByCompanyItemView
import ru.fabulus.fabulustrade.util.loadImage

class TradesByCompanyRVAdapter(val presenter: ITradesByCompanyListPresenter) :
    RecyclerView.Adapter<TradesByCompanyRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trades_by_company, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.binding.cvItemTradesByCompany.setOnClickListener {
            presenter.onItemClick(holder)
        }
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), TradesByCompanyItemView {

        val binding = ItemTradesByCompanyBinding.bind(itemView)

        override var pos = -1

        override fun setCompanyLogo(image: String) = with(itemView) {
            loadImage(image, binding.ivItemTradesByCompanyLogo)
        }

        override fun setTradesCount(count: String) = with(itemView) {
            binding.tvItemTradesByCompanyCounter.text = count
        }

        override fun setCompanyName(name: String) = with(itemView) {
            binding.tvItemTradesByCompanyName.text = name
        }

        override fun setLastTradeTime(time: String) = with(itemView) {
            binding.tvItemTradesByCompanyDate.text = time
        }
    }
}