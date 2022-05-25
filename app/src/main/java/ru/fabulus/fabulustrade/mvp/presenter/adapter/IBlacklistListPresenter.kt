package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.BlacklistItemView

interface IBlacklistListPresenter {
    fun getCount(): Int
    fun bind(view: BlacklistItemView)
    fun onItemClick(pos: Int)
}