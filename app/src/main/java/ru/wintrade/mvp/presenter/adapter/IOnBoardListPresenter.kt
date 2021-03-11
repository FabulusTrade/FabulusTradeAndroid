package ru.wintrade.mvp.presenter.adapter

import ru.wintrade.mvp.view.item.OnBoardItemView

interface IOnBoardListPresenter {
    fun onNextBtnClick(pos: Int)
    fun getCount(): Int
    fun bindView(view: OnBoardItemView)
}