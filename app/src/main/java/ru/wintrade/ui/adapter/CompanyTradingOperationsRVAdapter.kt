package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comp_trading_ops.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ICompanyTradingOperationsListPresenter
import ru.wintrade.mvp.view.item.CompanyTradingOperationsItemView
import java.util.*

class CompanyTradingOperationsRVAdapter(val presenter: ICompanyTradingOperationsListPresenter) :
    RecyclerView.Adapter<CompanyTradingOperationsRVAdapter.DetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comp_trading_ops, parent, false)
        )

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view), CompanyTradingOperationsItemView {
        override var pos = -1

        override fun setLastOperationDate(date: Date) {
            itemView.tv_item_comp_trading_ops_date.text = date.toString()
        }
    }
}
