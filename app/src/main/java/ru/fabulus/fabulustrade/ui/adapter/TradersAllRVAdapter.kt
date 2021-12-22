package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_traders_all.view.*
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ITradersAllListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.TradersAllItemView
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import ru.fabulus.fabulustrade.util.showLongToast
import javax.inject.Inject


class TradersAllRVAdapter(val presenter: ITradersAllListPresenter) :
    RecyclerView.Adapter<TradersAllRVAdapter.TradersAllViewHolder>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TradersAllViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_traders_all, parent, false)
    )

    override fun onBindViewHolder(holder: TradersAllViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        initListeners(holder)
    }

    fun initListeners(holder: TradersAllViewHolder) {
        holder.itemView.layout_all_traders_item.setOnClickListener {
            presenter.openTraderStat(holder.bindingAdapterPosition)
        }
        holder.itemView.cb_traders_all_item_observe.setOnClickListener { view ->
            val context = holder.itemView.context
            if (holder.itemView.cb_traders_all_item_observe.isChecked) {
                if (presenter.checkIfTraderIsMe(holder.bindingAdapterPosition)) {
                    (view as CheckBox).isChecked = false
                } else {
                    presenter.observeBtnClicked(holder.bindingAdapterPosition, true)
                    if (presenter.isLogged())
                        context.showLongToast(
                            context.resources.getString(R.string.added_to_observation),
                            Toast.LENGTH_SHORT
                        )
                }
            } else {
                presenter.observeBtnClicked(holder.bindingAdapterPosition, false)
                context.showLongToast(
                    context.resources.getString(R.string.removed_from_observation),
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class TradersAllViewHolder(view: View) : RecyclerView.ViewHolder(view),
        TradersAllItemView {
        override var pos = -1
        override fun setTraderName(name: String) {
            itemView.tv_traders_all_item_name.text = name
        }

        override fun setTraderProfit(profit: String, textColor: Int) {
            itemView.tv_traders_all_item_year_profit.setTextAndColor(profit, textColor)
        }

        override fun setTraderAvatar(avatar: String) {
            loadImage(avatar, itemView.iv_traders_all_item_ava)
        }

        override fun setTraderObserveBtn(isObserve: Boolean?) {
            isObserve?.let {
                itemView.tv_traders_all_item_subscription.visibility = View.GONE
                itemView.cb_traders_all_item_observe.visibility = View.VISIBLE
                itemView.cb_traders_all_item_observe.isChecked = isObserve
            } ?: setVisibleSubscription()
        }

        private fun setVisibleSubscription() {
            itemView.tv_traders_all_item_subscription.visibility = View.VISIBLE
            itemView.cb_traders_all_item_observe.visibility = View.GONE
        }
    }
}