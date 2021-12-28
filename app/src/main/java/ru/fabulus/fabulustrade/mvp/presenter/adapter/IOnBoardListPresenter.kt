package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.view.item.OnBoardItemView

interface IOnBoardListPresenter {
    fun onNextBtnClick(pos: Int)
    fun getCount(): Int
    fun bindView(view: OnBoardItemView)
}