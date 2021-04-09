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
            isPressed(btn_trader_news_publication, btn_trader_news_subscription)
        }
        btn_trader_news_subscription.setOnClickListener {
            isPressed(btn_trader_news_subscription, btn_trader_news_publication)
        }
    }

    private fun isPressed(
        btnPressed: Button,
        btnNotPressed: Button
    ) {
        with(btnPressed) {
            backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.colorLightGreen) }
            setTextColor(context?.let {
                ContextCompat.getColorStateList(
                    it,
                    R.color.colorPrimary
                )
            })
        }
        with(btnNotPressed) {
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