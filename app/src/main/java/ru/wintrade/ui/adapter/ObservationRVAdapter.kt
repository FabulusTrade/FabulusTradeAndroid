package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_subscriber_observation.view.*
import ru.wintrade.R
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.presenter.adapter.IObservationListPresenter
import ru.wintrade.mvp.view.item.ObservationItemView
import ru.wintrade.util.loadImage
import javax.inject.Inject

class ObservationRVAdapter(val presenter: IObservationListPresenter) :
    RecyclerView.Adapter<ObservationRVAdapter.ObservationItemViewHolder>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ObservationItemViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subscriber_observation, parent, false)
    )

    override fun onBindViewHolder(holder: ObservationItemViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        setProfitColor(holder)
        holder.itemView.layout_traders_signed_item.setOnClickListener {
            presenter.onItemClick(holder.adapterPosition)
        }
        holder.itemView.cb_subscriber_observation.setOnClickListener {
            presenter.deleteObservation(holder.adapterPosition)
        }
    }

    private fun setProfitColor(holder: ObservationItemViewHolder) {
        if (holder.itemView.tv_subscriber_observation_profit.text.substring(0, 1) == "-") {
            holder.itemView.tv_subscriber_observation_profit.setTextColor(
                resourceProvider.getColor(R.color.colorRedPercent)
            )
        } else {
            holder.itemView.tv_subscriber_observation_profit.setTextColor(
                resourceProvider.getColor(R.color.colorGreenPercent)
            )
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class ObservationItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ObservationItemView {
        override var pos = -1

        override fun setTraderName(name: String) {
            itemView.tv_subscriber_observation_name.text = name
        }

        override fun setTraderProfit(profit: String) {
            itemView.tv_subscriber_observation_profit.text = profit
        }

        override fun setTraderAvatar(avatar: String?) {
            avatar?.let { loadImage(it, itemView.iv_subscriber_observation_ava) }
        }

        override fun subscribeStatus(isSubscribe: Boolean) {
            if (isSubscribe) {
                itemView.cb_subscriber_observation.visibility = View.GONE
                itemView.tv_subscriber_observation_is_subscribe.visibility = View.VISIBLE
            } else {
                with(itemView.cb_subscriber_observation) {
                    visibility = View.VISIBLE
                    isChecked = true
                }
                itemView.tv_subscriber_observation_is_subscribe.visibility = View.GONE
            }
        }
    }
}