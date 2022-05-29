package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.databinding.ItemBlacklistBinding
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IBlacklistListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.BlacklistItemView
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import javax.inject.Inject

class BlacklistRVAdapter(val presenter: IBlacklistListPresenter) :
    RecyclerView.Adapter<BlacklistRVAdapter.BlacklistItemViewHolder>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlacklistItemViewHolder {
        val binding =
            ItemBlacklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = BlacklistItemViewHolder(binding)
        initListeners(binding)
        return holder
    }

    fun initListeners(binding: ItemBlacklistBinding) {
        binding.tvBlacklistDeleteButton.setOnClickListener() {
            presenter.deleteFromBlacklist(binding.tvTraderId.text.toString())
        }
    }

    override fun onBindViewHolder(holder: BlacklistItemViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class BlacklistItemViewHolder(private val binding: ItemBlacklistBinding) :
        RecyclerView.ViewHolder(binding.root),
        BlacklistItemView {
        override var pos = -1

        override fun setTraderId(id: String) {
            binding.tvTraderId.text = id
        }

        override fun setTraderName(name: String) {
            binding.tvBlacklistName.text = name
        }

        override fun setFollowersCount(count: Int) {
            binding.tvFollowerCounter.text = count.toString()
        }

        override fun setTraderProfit(profit: String, textColor: Int) {
            binding.tvBlacklistProfit.setTextAndColor(profit, textColor)
        }

        override fun setBlacklistedAt(blacklistedAt: String) {
            binding.tvBlacklistedAt.text = blacklistedAt
        }

        override fun setTraderAvatar(avatar: String?) {
            avatar?.let { loadImage(it, binding.ivBlacklistAva) }
        }
    }
}