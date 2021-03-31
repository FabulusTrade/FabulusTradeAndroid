package ru.wintrade.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_sub_trade.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ISubscriberTradesRVListPresenter
import ru.wintrade.mvp.view.item.SubscriberTradeItemView
import ru.wintrade.util.loadImage

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
        holder.containerView.tv_item_sub_trade_more.setOnClickListener {
            presenter.clicked(holder.pos)
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer, SubscriberTradeItemView {

        override var pos = -1

        override fun setAvatar(url: String) = with(containerView) {
            loadImage(url, iv_item_sub_trade_avatar)
        }

        override fun setNickname(nickname: String) = with(containerView) {
            tv_item_sub_trade_nickname.text = nickname
        }

        override fun setType(type: String) = with(containerView) {
            tv_item_sub_operation_type.text = type
        }

        override fun setCompany(company: String) = with(containerView) {
            tv_item_sub_trade_company.text = company
        }

        override fun setPrice(price: String) = with(containerView) {
            tv_item_sub_trade_price.text = price
        }

        override fun setSum(sum: String) = with(containerView) {
            tv_item_sub_trade_sum.text = sum
        }

        override fun setCount(count: String) = with(containerView) {
            tv_item_sub_trade_count.text = count
        }

        override fun setDate(date: String) = with(containerView) {
            tv_item_sub_trade_date.text = date
        }

        override fun setProfit(profit: String, color: Int) = with(containerView) {
            tv_item_sub_trade_profit_count.text = profit
            tv_item_sub_trade_profit_count.setBackgroundColor(color)
        }
    }
}