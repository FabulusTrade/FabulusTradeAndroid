package ru.wintrade.mvp.presenter.adapter

import androidx.fragment.app.Fragment

interface ITraderStatVPListPresenter {
    fun getCount(): Int
    fun getFragmentList(): List<Fragment>
}