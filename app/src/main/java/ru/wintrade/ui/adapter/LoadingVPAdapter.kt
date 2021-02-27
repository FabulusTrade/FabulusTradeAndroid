package ru.wintrade.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_loading_image.view.*
import ru.wintrade.R
import ru.wintrade.mvp.presenter.adapter.ILoadingListPresenter
import ru.wintrade.mvp.view.item.LoadingItemView
import ru.wintrade.util.loadImage

class LoadingVPAdapter(
    val presenter: ILoadingListPresenter
): RecyclerView.Adapter<LoadingVPAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_loading_image,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pos = position
        presenter.bindView(holder)
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer, LoadingItemView {

        override var pos = -1

        override fun setImage(id: Int) = with(containerView) {
            loadImage(id, iv_loading_image)
        }
    }
}