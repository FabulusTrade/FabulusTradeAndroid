package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemSubTradeBinding
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ISubscriberTradesRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.SubscriberTradeItemView
import ru.fabulus.fabulustrade.util.loadImage

class SubscriberTradesRVAdapter(
    val presenter: ISubscriberTradesRVListPresenter
) : RecyclerView.Adapter<SubscriberTradesRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_sub_trade,
            parent,
            false
        )
    )

    override fun getItemCount() = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.containerView.setOnClickListener {
            presenter.clicked(holder.pos)
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer, SubscriberTradeItemView {

        val binding = ItemSubTradeBinding.bind(itemView)

        override var pos = -1

        override fun setAvatar(url: String) = with(binding) {
            loadImage(url, binding.ivItemSubTradeAvatar)
        }

        override fun setNickname(nickname: String) = with(binding) {
            binding.tvItemSubTradeNickname.text = nickname
        }

        override fun setType(type: String) = with(binding) {
            binding.tvItemSubOperationType.text = type
        }

        override fun setCompany(company: String) = with(binding) {
            tvItemSubTradeCompany.text = company
        }

        override fun setPrice(price: String) = with(binding) {
            tvItemSubTradePrice.text = price
        }

        override fun setDate(date: String) = with(binding) {
            tvItemSubTradeDate.text = date
        }

        override fun setProfitVisibility(visibility: Int) = with(binding) {
            tvItemSubTradeProfitCount.visibility = visibility
        }

        override fun setProfit(profit: String, color: Int) = with(binding) {
            tvItemSubTradeProfitCount.text = profit
            tvItemSubTradeProfitCount.setTextColor(ContextCompat.getColor(containerView.context, color))
        }
    }
}