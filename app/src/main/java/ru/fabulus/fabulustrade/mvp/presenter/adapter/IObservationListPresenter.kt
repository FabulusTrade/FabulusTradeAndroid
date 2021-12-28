package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.ObservationItemView

interface IObservationListPresenter {
    fun getCount(): Int
    fun bind(view: ObservationItemView)
    fun onItemClick(pos: Int)
    fun deleteObservation(pos: Int)
    fun deleteSubscription(pos: Int)
}