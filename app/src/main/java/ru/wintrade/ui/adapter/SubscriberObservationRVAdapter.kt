package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_subscriber_observation.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ISubscriberObservationListPresenter
import ru.wintrade.mvp.view.item.SubscriberObservationItemView
import ru.wintrade.util.loadImage

class SubscriberObservationRVAdapter(val presenter: ISubscriberObservationListPresenter) :
    RecyclerView.Adapter<SubscriberObservationRVAdapter.SubscriberObservationItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = SubscriberObservationItemViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subscriber_observation, parent, false)
    )

    override fun onBindViewHolder(holder: SubscriberObservationItemViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class SubscriberObservationItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        SubscriberObservationItemView {
        override var pos = -1

        override fun setTraderName(name: String) {
            itemView.tv_subscriber_observation_name.text = name
        }

        override fun setTraderProfit(profit: String) {
            itemView.tv_subscriber_observation_profit.text = profit
        }

        override fun setTraderAvatar(avatar: String) {
            loadImage(avatar, itemView.iv_subscriber_observation_ava)
        }
    }
}
