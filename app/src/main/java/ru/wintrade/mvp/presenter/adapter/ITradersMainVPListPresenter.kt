package ru.wintrade.mvp.presenter.adapter

import androidx.fragment.app.Fragment

interface ITradersMainVPListPresenter {
    fun getCount(): Int
    fun getFragmentList(): List<Fragment>
}