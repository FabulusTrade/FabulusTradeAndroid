package ru.fabulus.fabulustrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemOnBoardBinding
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IOnBoardListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.OnBoardItemView

class OnBoardVPAdapter(
    val presenter: IOnBoardListPresenter
) : RecyclerView.Adapter<OnBoardVPAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_on_board,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pos = position
        presenter.bindView(holder)
        holder.binding.btnOnBoardItem.setOnClickListener { presenter.onNextBtnClick(holder.pos) }
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer, OnBoardItemView {

        val binding = ItemOnBoardBinding.bind(itemView)

        override var pos = -1

        override fun setImage(id: Int) {}
    }
}