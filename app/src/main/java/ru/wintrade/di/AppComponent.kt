package ru.wintrade.di

import dagger.Component
import ru.wintrade.di.module.*
import ru.wintrade.mvp.presenter.*
import ru.wintrade.mvp.presenter.entrance.OnBoardPresenter
import ru.wintrade.mvp.presenter.entrance.SignInPresenter
import ru.wintrade.mvp.presenter.entrance.SplashPresenter
import ru.wintrade.mvp.presenter.registration.subscriber.ResetPasswordPresenter
import ru.wintrade.mvp.presenter.registration.subscriber.SignUpPresenter
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderFirstPresenter
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderFourPresenter
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderSecondPresenter
import ru.wintrade.mvp.presenter.registration.trader.RegAsTraderThirdPresenter
import ru.wintrade.mvp.presenter.service.MessagingPresenter
import ru.wintrade.mvp.presenter.subscriber.SubscriberMainPresenter
import ru.wintrade.mvp.presenter.subscriber.SubscriberObservationPresenter
import ru.wintrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.wintrade.mvp.presenter.subscriber.SubscriberTradePresenter
import ru.wintrade.mvp.presenter.trader.*
import ru.wintrade.mvp.presenter.traderme.*
import ru.wintrade.mvp.presenter.traders.TradersAllPresenter
import ru.wintrade.mvp.presenter.traders.TradersFilterPresenter
import ru.wintrade.mvp.presenter.traders.TradersMainPresenter
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import ru.wintrade.ui.activity.SplashActivity
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.entrance.*
import ru.wintrade.ui.fragment.subscriber.SubscriberMainFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberNewsFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberObservationFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberTradeFragment
import ru.wintrade.ui.fragment.trader.*
import ru.wintrade.ui.fragment.traderme.*
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
        ProfileModule::class,
        HelperModule::class
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
    fun inject(resetPasswordFragment: ResetPasswordFragment)
    fun inject(resetPasswordPresenter: ResetPasswordPresenter)
    fun inject(tradersMainFragment: TradersMainFragment)
    fun inject(tradersMainPresenter: TradersMainPresenter)
    fun inject(messagingPresenter: MessagingPresenter)
    fun inject(traderMainFragment: TraderMainFragment)
    fun inject(traderMainPresenter: TraderMainPresenter)
    fun inject(traderMeMainFragment: TraderMeMainFragment)
    fun inject(traderMeMainPresenter: TraderMeMainPresenter)
    fun inject(traderMeProfitFragment: TraderMeProfitFragment)
    fun inject(traderMeProfitPresenter: TraderMeProfitPresenter)
    fun inject(traderMeSubTradeFragment: TraderMeSubTradeFragment)
    fun inject(traderMeSubTradePresenter: TraderMeSubTradePresenter)
    fun inject(traderProfitPresenter: TraderProfitPresenter)
    fun inject(traderProfitFragment: TraderProfitFragment)
    fun inject(traderPostFragment: TraderPostFragment)
    fun inject(traderPostPresenter: TraderPostPresenter)
    fun inject(traderMePostFragment: TraderMePostFragment)
    fun inject(traderMePostPresenter: TraderMePostPresenter)
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
    fun inject(companyTradingOperationsFragment: CompanyTradingOperationsFragment)
    fun inject(companyTradingOperationsPresenter: CompanyTradingOperationsPresenter)
    fun inject(traderMeObservationFragment: TraderMeObservationFragment)
    fun inject(traderMeObservationPresenter: TraderMeObservationPresenter)
    fun inject(traderMeTradeFragment: TraderMeTradeFragment)
    fun inject(traderMeTradePresenter: TraderMeTradePresenter)
    fun inject(aboutWinTradeFragment: AboutWinTradeFragment)
    fun inject(aboutWinTradePresenter: AboutWinTradePresenter)
    fun inject(questionFragment: QuestionFragment)
    fun inject(questionPresenter: QuestionPresenter)
    fun inject(friendInviteFragment: FriendInviteFragment)
    fun inject(friendInvitePresenter: FriendInvitePresenter)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(settingsPresenter: SettingsPresenter)
    fun inject(createPostFragment: CreatePostFragment)
    fun inject(createPostPresenter: CreatePostPresenter)
    fun inject(regAsTraderFirstPresenter: RegAsTraderFirstPresenter)
    fun inject(registrationAsTraderFirstFragment: RegistrationAsTraderFragmentFirst)
    fun inject(regAsTraderSecondPresenter: RegAsTraderSecondPresenter)
    fun inject(registrationAsTraderSecondFragment: RegistrationAsTraderFragmentSecond)
    fun inject(regAsTraderThirdPresenter: RegAsTraderThirdPresenter)
    fun inject(registrationAsTraderFirstFragment: RegistrationAsTraderFragmentThird)
    fun inject(regAsTraderFourPresenter: RegAsTraderFourPresenter)
    fun inject(registrationAsTraderFirstFragment: RegistrationAsTraderFragmentFour)
    fun inject(webViewPresenter: WebViewPresenter)
    fun inject(webViewFragment: WebViewFragment)
}