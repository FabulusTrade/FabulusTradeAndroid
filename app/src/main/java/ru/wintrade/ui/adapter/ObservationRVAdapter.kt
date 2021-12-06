package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_subscriber_observation.view.*
import ru.wintrade.R
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.presenter.adapter.IObservationListPresenter
import ru.wintrade.mvp.view.item.ObservationItemView
import ru.wintrade.util.loadImage
import ru.wintrade.util.setTextAndColor
import ru.wintrade.util.showLongToast
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
        holder.itemView.layout_traders_signed_item.setOnClickListener {
            presenter.onItemClick(holder.bindingAdapterPosition)
        }

        holder.itemView.cb_subscriber_observation.setOnClickListener {
            presenter.deleteObservation(holder.bindingAdapterPosition)
            val context = holder.itemView.context
            context.showLongToast(context.resources.getString(R.string.removed_from_observation), Toast.LENGTH_SHORT)
        }

        holder.itemView.tv_subscriber_observation_is_subscribe.setOnClickListener {
            presenter.deleteSubscription(holder.bindingAdapterPosition)
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class ObservationItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ObservationItemView {
        override var pos = -1

        override fun setTraderName(name: String) {
            itemView.tv_subscriber_observation_name.text = name
        }

        override fun setTraderProfit(profit: String, textColor: Int) {
            itemView.tv_subscriber_observation_profit.setTextAndColor(profit, textColor)
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