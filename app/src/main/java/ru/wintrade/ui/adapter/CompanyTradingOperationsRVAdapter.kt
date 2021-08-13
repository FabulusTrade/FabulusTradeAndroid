package ru.wintrade.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comp_trading_ops.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ICompanyTradingOperationsListPresenter
import ru.wintrade.mvp.view.item.CompanyTradingOperationsItemView
import ru.wintrade.util.loadImage

class CompanyTradingOperationsRVAdapter(val presenter: ICompanyTradingOperationsListPresenter) :
    RecyclerView.Adapter<CompanyTradingOperationsRVAdapter.DetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comp_trading_ops, parent, false)
        )

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        when {
            holder.itemView.tv_item_comp_trading_ops_profit.text.isNullOrEmpty() -> {
                holder.itemView.tv_item_comp_trading_ops_profit.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.color.colorWhite)
            }
            holder.itemView.tv_item_comp_trading_ops_profit.text.substring(0, 1) == "-" -> {
                holder.itemView.tv_item_comp_trading_ops_profit.setTextColor(Color.RED)
            }
            else -> {
                holder.itemView.tv_item_comp_trading_ops_profit.setTextColor(Color.GREEN)
            }
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view),
        CompanyTradingOperationsItemView {
        override var pos = -1
        override fun setOperationType(type: String) {
            itemView.tv_item_comp_trading_ops_operation.text = type
        }

        override fun setCompanyLogo(url: String) {
            loadImage(url, itemView.iv_item_comp_trading_ops_logo)
        }

        override fun setOperationDate(date: String) {
            itemView.tv_item_comp_trading_ops_date.text = date
        }

        override fun setProfitCount(profit: String?) {
            itemView.tv_item_comp_trading_ops_profit.text = profit
        }

        override fun setTradePrice(price: String) {
            itemView.tv_item_comp_trading_ops_price.text = price
        }
    }
}
