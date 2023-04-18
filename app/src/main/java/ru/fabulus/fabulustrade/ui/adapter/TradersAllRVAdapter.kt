package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemTradersAllBinding
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
        holder.binding.layoutAllTradersItem.setOnClickListener {
            presenter.openTraderStat(holder.bindingAdapterPosition)
        }
        holder.binding.cbTradersAllItemObserve.setOnClickListener { view ->
            val context = holder.itemView.context
            if (holder.binding.cbTradersAllItemObserve.isChecked) {
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

        val binding = ItemTradersAllBinding.bind(itemView)

        override var pos = -1
        override fun setTraderName(name: String) {
            binding.tvTradersAllItemName.text = name
        }

        override fun setTraderProfit(profit: String, textColor: Int) {
            binding.tvTradersAllItemYearProfit.setTextAndColor(profit, textColor)
        }

        override fun setTraderAvatar(avatar: String) {
            loadImage(avatar, binding.ivTradersAllItemAva)
        }

        override fun setTraderObserveBtn(isObserve: Boolean?) {
            isObserve?.let {
                binding.tvTradersAllItemSubscription.visibility = View.GONE
                binding.cbTradersAllItemObserve.visibility = View.VISIBLE
                binding.cbTradersAllItemObserve.isChecked = isObserve
            } ?: setVisibleSubscription()
        }

        private fun setVisibleSubscription() {
            binding.tvTradersAllItemSubscription.visibility = View.VISIBLE
            binding.cbTradersAllItemObserve.visibility = View.GONE
        }
    }
}