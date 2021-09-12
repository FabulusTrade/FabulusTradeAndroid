package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_on_board.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.IOnBoardListPresenter
import ru.wintrade.mvp.view.item.OnBoardItemView
import ru.wintrade.util.loadImage

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
        holder.containerView.btn_on_board_item.setOnClickListener { presenter.onNextBtnClick(holder.pos) }
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer, OnBoardItemView {

        override var pos = -1

        override fun setImage(id: Int) = with(containerView) {
            loadImage(id, iv_on_board_item)
        }
    }
}