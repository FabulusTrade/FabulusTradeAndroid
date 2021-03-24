package ru.wintrade.mvp.presenter.adapter

import androidx.fragment.app.Fragment

interface IMainTradersVPListPresenter {
    fun getCount(): Int
    fun getFragmentList(): List<Fragment>
}