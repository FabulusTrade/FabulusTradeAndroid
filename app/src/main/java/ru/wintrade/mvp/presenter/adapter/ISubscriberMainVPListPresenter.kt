package ru.wintrade.mvp.presenter.adapter

import androidx.fragment.app.Fragment

interface ISubscriberMainVPListPresenter {
    fun getCount(): Int
    fun getFragmentList(): List<Fragment>
}