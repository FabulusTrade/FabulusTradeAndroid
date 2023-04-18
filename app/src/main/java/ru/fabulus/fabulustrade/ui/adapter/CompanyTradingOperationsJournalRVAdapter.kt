package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemCompTradingOpsJournalBinding
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
        holder.binding.layoutCompTradingOpsJournal.setOnClickListener {
            presenter.itemClicked(holder)
        }
        when {
            holder.binding.tvItemCompTradingOpsJournalProfit.text.isNullOrEmpty() -> {
                holder.binding.tvItemCompTradingOpsJournalProfit.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.color.colorWhite)
            }
            holder.binding.tvItemCompTradingOpsJournalProfit.text.substring(0, 1) == "-" -> {
                holder.binding.tvItemCompTradingOpsJournalProfit.setTextColor(
                    resourceProvider.getColor(R.color.colorRedPercent)
                )
            }
            else -> {
                holder.binding.tvItemCompTradingOpsJournalProfit.setTextColor(
                    resourceProvider.getColor(R.color.colorGreenPercent)
                )
            }
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view),
        CompanyTradingOperationsJournalItemView {

        val binding = ItemCompTradingOpsJournalBinding.bind(itemView)

        override var pos = -1
        override fun setOperationType(type: String) {
            binding.tvItemCompTradingOpsJournalOperation.text = type
        }

        override fun setCompanyLogo(url: String) {
            loadImage(url, binding.ivItemCompTradingOpsJournalLogo)
        }

        override fun setOperationDate(date: String) {
            binding.tvItemCompTradingOpsJournalDate.text = date
        }

        override fun setProfitCount(profit: String?) {
            binding.tvItemCompTradingOpsJournalProfit.text = profit
        }

        override fun setTradePrice(price: String) {
            binding.tvItemCompTradingOpsJournalPrice.text = price
        }

        override fun setEndCount(endCount: Int) {
            binding.tvItemCompTradingOpsJournalQuantity.text = endCount.toString() + resourceProvider.getStringResource(
                            R.string.pcs_string)
        }

        override fun setVisible(visible: Boolean) {
            binding.tvItemCompTradingOpsJournalObservation.isChecked = visible
        }

        override fun setClipVisibility(visibility: Boolean) {
            binding.ivItemCompTradingOpsJournalAttachedPostClip.visibility =
                if (visibility) View.VISIBLE else View.INVISIBLE
        }
    }
}