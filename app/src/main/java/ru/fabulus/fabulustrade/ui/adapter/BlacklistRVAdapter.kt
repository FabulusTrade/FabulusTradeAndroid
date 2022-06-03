package ru.fabulus.fabulustrade.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemBlacklistBinding
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IBlacklistListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.BlacklistItemView
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
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
        return holder
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

        val tvBlacklistDeleteButton: TextView = itemView.findViewById(R.id.tv_blacklist_delete_button)

        init {
            tvBlacklistDeleteButton.setOnClickListener {
                presenter.deleteFromBlacklist(bindingAdapterPosition)
            }
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
            val parsingDF: DateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
            } else {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            }
            val formattingDF: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm");
            try {
                val date: Date? = parsingDF.parse(blacklistedAt)
                binding.tvBlacklistedAt.text = formattingDF?.format(date)
            } catch (e: Exception) {
            }
        }

        override fun setProfitNegativeArrow() {
            binding.ivProfitArrow.setImageResource(R.drawable.ic_profit_arrow_down)
        }

        override fun setProfitPositiveArrow() {
            binding.ivProfitArrow.setImageResource(R.drawable.ic_profit_arrow_up)
        }

        override fun setTraderAvatar(avatar: String?) {
            avatar?.let { loadImage(it, binding.ivBlacklistAva) }
        }
    }
}