package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.ObservationItemView

interface IObservationListPresenter {
    fun getCount(): Int
    fun bind(view: ObservationItemView)
    fun onItemClick(pos: Int)
}