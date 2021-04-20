package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.DealsDetailItemView

interface IDealsDetailListPresenter {
    fun getCount(): Int
    fun bind(view: DealsDetailItemView)
}