package ru.wintrade.di

import dagger.Component
import ru.wintrade.di.module.*
import ru.wintrade.mvp.presenter.*
import ru.wintrade.mvp.presenter.service.MessagingPresenter
import ru.wintrade.mvp.presenter.trader.*
import ru.wintrade.mvp.presenter.traders.*
import ru.wintrade.mvp.presenter.subscriber.*
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import ru.wintrade.ui.activity.SplashActivity
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.trader.*
import ru.wintrade.ui.fragment.traders.*
import ru.wintrade.ui.fragment.subscriber.*

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
    fun inject(app: App)
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
    fun inject(traderMainFragment: TraderMainFragment)
    fun inject(traderMainPresenter: TraderMainPresenter)
    fun inject(traderProfitFragment: TraderProfitFragment)
    fun inject(traderProfitPresenter: TraderProfitPresenter)
    fun inject(traderPostFragment: TraderPostFragment)
    fun inject(traderPostPresenter: TraderPostPresenter)
    fun inject(traderPopularInstrumentsFragment: TraderPopularInstrumentsFragment)
    fun inject(traderPopularInstrumentsPresenter: TraderPopularInstrumentsPresenter)
    fun inject(traderTradeFragment: TraderTradeFragment)
    fun inject(traderTradePresenter: TraderTradePresenter)
    fun inject(splashActivity: SplashActivity)
    fun inject(splashPresenter: SplashPresenter)
    fun inject(tradersAllFragment: TradersAllFragment)
    fun inject(tradersAllPresenter: TradersAllPresenter)
    fun inject(tradersFilterFragment: TradersFilterFragment)
    fun inject(tradersFilterPresenter: TradersFilterPresenter)
    fun inject(subscriberMainFragment: SubscriberMainFragment)
    fun inject(subscriberMainPresenter: SubscriberMainPresenter)
    fun inject(subscriberObservationFragment: SubscriberObservationFragment)
    fun inject(subscriberObservationPresenter: SubscriberObservationPresenter)
    fun inject(subscriberTradeFragment: SubscriberTradeFragment)
    fun inject(subscriberTradePresenter: SubscriberTradePresenter)
    fun inject(subscriberNewsFragment: SubscriberNewsFragment)
    fun inject(subscriberPostPresenter: SubscriberPostPresenter)
    fun inject(tradeDetailFragment: TradeDetailFragment)
    fun inject(tradeDetailPresenter: TradeDetailPresenter)
    fun inject(traderDealsDetailFragment: TraderDealsDetailFragment)
    fun inject(traderDealsDetailPresenter: TraderDealsDetailPresenter)
    fun inject(traderObservationFragment: TraderObservationFragment)
    fun inject(traderObservationPresenter: TraderObservationPresenter)
}