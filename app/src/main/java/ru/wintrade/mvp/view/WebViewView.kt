package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface WebViewView : MvpView {
    fun init()
    fun setMainContent(url: String)
}
