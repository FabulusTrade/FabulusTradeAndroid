package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemSubscriberObservationBinding
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IObservationListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.ObservationItemView
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import ru.fabulus.fabulustrade.util.showLongToast
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
        holder.binding.layoutTradersSignedItem.setOnClickListener {
            presenter.onItemClick(holder.bindingAdapterPosition)
        }

        holder.binding.cbSubscriberObservation.setOnClickListener {
            presenter.deleteObservation(holder.bindingAdapterPosition)
            val context = holder.itemView.context
            context.showLongToast(context.resources.getString(R.string.removed_from_observation), Toast.LENGTH_SHORT)
        }

        holder.binding.tvSubscriberObservationIsSubscribe.setOnClickListener {
            presenter.deleteSubscription(holder.bindingAdapterPosition)
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class ObservationItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ObservationItemView {

        val binding = ItemSubscriberObservationBinding.bind(itemView)

        override var pos = -1

        override fun setTraderName(name: String) {
            binding.tvSubscriberObservationName.text = name
        }

        override fun setTraderProfit(profit: String, textColor: Int) {
            binding.tvSubscriberObservationProfit.setTextAndColor(profit, textColor)
        }

        override fun setTraderAvatar(avatar: String?) {
            avatar?.let { loadImage(it, binding.ivSubcsriberObservationAva) }
        }

        override fun subscribeStatus(isSubscribe: Boolean) {
            if (isSubscribe) {
                binding.cbSubscriberObservation.visibility = View.GONE
                binding.tvSubscriberObservationIsSubscribe.visibility = View.VISIBLE
            } else {
                with(binding.cbSubscriberObservation) {
                    visibility = View.VISIBLE
                    isChecked = true
                }
                binding.tvSubscriberObservationIsSubscribe.visibility = View.GONE
            }
        }
    }
}