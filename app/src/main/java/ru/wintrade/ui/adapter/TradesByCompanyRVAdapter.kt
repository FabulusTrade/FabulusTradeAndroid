package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_trades_by_company.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ITradesByCompanyListPresenter
import ru.wintrade.mvp.view.item.TradesByCompanyItemView
import ru.wintrade.util.loadImage

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
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), TradesByCompanyItemView {
        override var pos = -1

        override fun setCompanyLogo(image: String) = with(itemView) {
            loadImage(image, iv_item_trades_by_company_logo)
        }

        override fun setTradesCount(count: Int) = with(itemView) {
            tv_item_trades_by_company_counter.text = count.toString()
        }

        override fun setCompanyName(name: String) = with(itemView) {
            tv_item_trades_by_company_name.text = name
        }

        override fun setLastTradeTime(time: String) = with(itemView) {
            tv_item_trades_by_company_date.text = time
        }
    }

}