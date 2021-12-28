package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trades_by_company.view.*
import ru.fabulus.fabulustrade.R
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
        holder.itemView.cv_item_trades_by_company.setOnClickListener {
            presenter.onItemClick(holder)
        }
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), TradesByCompanyItemView {
        override var pos = -1

        override fun setCompanyLogo(image: String) = with(itemView) {
            loadImage(image, iv_item_trades_by_company_logo)
        }

        override fun setTradesCount(count: String) = with(itemView) {
            tv_item_trades_by_company_counter.text = count
        }

        override fun setCompanyName(name: String) = with(itemView) {
            tv_item_trades_by_company_name.text = name
        }

        override fun setLastTradeTime(time: String) = with(itemView) {
            tv_item_trades_by_company_date.text = time
        }
    }
}