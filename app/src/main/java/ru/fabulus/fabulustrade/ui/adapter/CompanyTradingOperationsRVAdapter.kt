package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemCompTradingOpsBinding
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ICompanyTradingOperationsListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.CompanyTradingOperationsItemView
import ru.fabulus.fabulustrade.util.loadImage
import javax.inject.Inject

class CompanyTradingOperationsRVAdapter(val presenter: ICompanyTradingOperationsListPresenter) :
    RecyclerView.Adapter<CompanyTradingOperationsRVAdapter.DetailViewHolder>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comp_trading_ops, parent, false)
        )

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        holder.binding.layoutCompTradingOps.setOnClickListener {
            presenter.itemClicked(holder)
        }
        when {
            holder.binding.tvItemCompTradingOpsProfit.text.isNullOrEmpty() -> {
                holder.binding.tvItemCompTradingOpsProfit.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.color.colorWhite)
            }
            holder.binding.tvItemCompTradingOpsProfit.text.substring(0, 1) == "-" -> {
                holder.binding.tvItemCompTradingOpsProfit.setTextColor(
                    resourceProvider.getColor(R.color.colorRedPercent)
                )
            }
            else -> {
                holder.binding.tvItemCompTradingOpsProfit.setTextColor(
                    resourceProvider.getColor(R.color.colorGreenPercent)
                )
            }
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view),
        CompanyTradingOperationsItemView {

        val binding = ItemCompTradingOpsBinding.bind(itemView)

        override var pos = -1
        override fun setOperationType(type: String) {
            binding.tvItemCompTradingOpsOperation.text = type
        }

        override fun setCompanyLogo(url: String) {
            loadImage(url, binding.ivItemCompTradingOpsLogo)
        }

        override fun setOperationDate(date: String) {
            binding.tvItemCompTradingOpsDate.text = date
        }

        override fun setProfitCount(profit: String?) {
            binding.tvItemCompTradingOpsProfit.text = profit
        }

        override fun setTradePrice(price: String) {
            binding.tvItemCompTradingOpsPrice.text = price
        }

        override fun setClipVisibility(visibility: Boolean) {
            binding.ivItemCompTradingOpsAttachedPostClip.visibility =
                if (visibility) View.VISIBLE else View.INVISIBLE
        }
    }
}