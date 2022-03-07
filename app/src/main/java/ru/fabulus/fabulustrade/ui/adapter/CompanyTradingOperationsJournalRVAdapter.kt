package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comp_trading_ops_journal.view.*
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ICompanyTradingOperationsJournalListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.CompanyTradingOperationsJournalItemView
import ru.fabulus.fabulustrade.util.loadImage
import javax.inject.Inject

class CompanyTradingOperationsJournalRVAdapter(val presenter: ICompanyTradingOperationsJournalListPresenter) :
    RecyclerView.Adapter<CompanyTradingOperationsJournalRVAdapter.DetailViewHolder>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comp_trading_ops_journal, parent, false)
        )

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.itemView.layout_comp_trading_ops.setOnClickListener {
            presenter.itemClicked(holder)
        }
        when {
            holder.itemView.tv_item_comp_trading_ops_journal_profit.text.isNullOrEmpty() -> {
                holder.itemView.tv_item_comp_trading_ops_journal_profit.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.color.colorWhite)
            }
            holder.itemView.tv_item_comp_trading_ops_journal_profit.text.substring(0, 1) == "-" -> {
                holder.itemView.tv_item_comp_trading_ops_journal_profit.setTextColor(
                    resourceProvider.getColor(R.color.colorRedPercent)
                )
            }
            else -> {
                holder.itemView.tv_item_comp_trading_ops_journal_profit.setTextColor(
                    resourceProvider.getColor(R.color.colorGreenPercent)
                )
            }
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view),
        CompanyTradingOperationsJournalItemView {
        override var pos = -1
        override fun setOperationType(type: String) {
            itemView.tv_item_comp_trading_ops_journal_operation.text = type
        }

        override fun setCompanyLogo(url: String) {
            loadImage(url, itemView.iv_item_comp_trading_ops_journal_logo)
        }

        override fun setOperationDate(date: String) {
            itemView.tv_item_comp_trading_ops_journal_date.text = date
        }

        override fun setProfitCount(profit: String?) {
            itemView.tv_item_comp_trading_ops_journal_profit.text = profit
        }

        override fun setTradePrice(price: String) {
            itemView.tv_item_comp_trading_ops_journal_price.text = price
        }

        override fun setEndCount(endCount: Int) {
            itemView.tv_item_comp_trading_ops_journal_quantity.text = endCount.toString()
        }

        override fun setVisible(visible: Boolean) {
            itemView.tv_item_comp_trading_ops_journal_observation.isChecked = visible
        }
    }
}