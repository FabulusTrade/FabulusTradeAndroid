package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.SubscriberObservationItemView

interface ISubscriberObservationListPresenter {
    fun getCount(): Int
    fun bind(view: SubscriberObservationItemView)
}