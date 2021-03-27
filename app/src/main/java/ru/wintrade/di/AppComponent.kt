package ru.wintrade.di

import dagger.Component
import ru.wintrade.di.module.*
import ru.wintrade.mvp.presenter.*
import ru.wintrade.mvp.presenter.service.MessagingPresenter
import ru.wintrade.mvp.presenter.subscriber.SubscriberMainPresenter
import ru.wintrade.mvp.presenter.trader.*
import ru.wintrade.mvp.presenter.traders.TradersAllPresenter
import ru.wintrade.mvp.presenter.traders.TradersFilterPresenter
import ru.wintrade.mvp.presenter.traders.TradersMainPresenter
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import ru.wintrade.ui.activity.SplashActivity
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.subscriber.SubscriberMainFragment
import ru.wintrade.ui.fragment.trader.*
import ru.wintrade.ui.fragment.traders.TradersAllFragment
import ru.wintrade.ui.fragment.traders.TradersFilterFragment
import ru.wintrade.ui.fragment.traders.TradersMainFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        AppModule::class,
        CiceroneModule::class,
        RepoModule::class,
        ProfileModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
    fun inject(signUpFragment: SignUpFragment)
    fun inject(signUpPresenter: SignUpPresenter)
    fun inject(onBoardFragment: OnBoardFragment)
    fun inject(onBoardPresenter: OnBoardPresenter)
    fun inject(smsConfirmPresenter: SmsConfirmPresenter)
    fun inject(smsConfirmFragment: SmsConfirmFragment)
    fun inject(signInFragment: SignInFragment)
    fun inject(signInPresenter: SignInPresenter)
    fun inject(tradersMainFragment: TradersMainFragment)
    fun inject(tradersMainPresenter: TradersMainPresenter)
    fun inject(messagingPresenter: MessagingPresenter)
    fun inject(traderStatFragment: TraderStatFragment)
    fun inject(traderStatPresenter: TraderStatPresenter)
    fun inject(traderProfitFragment: TraderProfitFragment)
    fun inject(traderProfitPresenter: TraderProfitPresenter)
    fun inject(traderNewsFragment: TraderNewsFragment)
    fun inject(traderNewsPresenter: TraderNewsPresenter)
    fun inject(traderPopularInstrumentsFragment: TraderPopularInstrumentsFragment)
    fun inject(traderPopularInstrumentsPresenter: TraderPopularInstrumentsPresenter)
    fun inject(traderDealFragment: TraderDealFragment)
    fun inject(traderDealPresenter: TraderDealPresenter)
    fun inject(splashActivity: SplashActivity)
    fun inject(splashPresenter: SplashPresenter)
    fun inject(tradersAllFragment: TradersAllFragment)
    fun inject(tradersAllPresenter: TradersAllPresenter)
    fun inject(tradersFilterFragment: TradersFilterFragment)
    fun inject(tradersFilterPresenter: TradersFilterPresenter)
    fun inject(subscriberMainFragment: SubscriberMainFragment)
    fun inject(subscriberMainPresenter: SubscriberMainPresenter)
    fun inject(app: App)
}