package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_subscriber_observation.view.*
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IBlacklistListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.BlacklistItemView
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import ru.fabulus.fabulustrade.util.showLongToast
import javax.inject.Inject

class BlacklistRVAdapter(val presenter: IBlacklistListPresenter) :
    RecyclerView.Adapter<BlacklistRVAdapter.BlacklistItemViewHolder>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = BlacklistItemViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blacklist, parent, false)
    )

    override fun onBindViewHolder(holder: BlacklistItemViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class BlacklistItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        BlacklistItemView {
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