package ru.wintrade.ui.fragment.trader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_trader_news.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.trader.TraderNewsPresenter
import ru.wintrade.mvp.view.trader.TraderNewsView
import ru.wintrade.ui.App

class TraderNewsFragment : MvpAppCompatFragment(), TraderNewsView {
    companion object {
        fun newInstance() = TraderNewsFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderNewsPresenter

    @ProvidePresenter
    fun providePresenter() = TraderNewsPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_news, container, false)

    override fun init() {
        btn_trader_news_publication.setOnClickListener {
            presenter.publicationsBtnClicked()
        }
        btn_trader_news_subscription.setOnClickListener {
            presenter.subscriptionBtnClicked()
        }
    }

    override fun setBtnsState(state: TraderNewsPresenter.State) {
        val activeBtn: Button
        val inactiveBtn: Button
        if (state == TraderNewsPresenter.State.PUBLICATIONS) {
            activeBtn = btn_trader_news_publication
            inactiveBtn = btn_trader_news_subscription
        } else {
            activeBtn = btn_trader_news_subscription
            inactiveBtn = btn_trader_news_publication
        }
        activeBtn.apply {
            backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.colorLightGreen) }
            setTextColor(context?.let {
                ContextCompat.getColorStateList(
                    it,
                    R.color.colorPrimary
                )
            })
        }
        inactiveBtn.apply {
            backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.colorWhite) }
            setTextColor(context?.let {
                ContextCompat.getColorStateList(
                    it,
                    R.color.colorGray
                )
            })
        }
    }
}