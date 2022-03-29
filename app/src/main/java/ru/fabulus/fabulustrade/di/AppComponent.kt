package ru.fabulus.fabulustrade.di

import dagger.Component
import ru.fabulus.fabulustrade.di.module.*
import ru.fabulus.fabulustrade.mvp.presenter.*
import ru.fabulus.fabulustrade.mvp.presenter.entrance.OnBoardPresenter
import ru.fabulus.fabulustrade.mvp.presenter.entrance.SignInPresenter
import ru.fabulus.fabulustrade.mvp.presenter.entrance.SplashPresenter
import ru.fabulus.fabulustrade.mvp.presenter.registration.subscriber.ResetPasswordPresenter
import ru.fabulus.fabulustrade.mvp.presenter.registration.subscriber.SignUpPresenter
import ru.fabulus.fabulustrade.mvp.presenter.registration.trader.RegAsTraderFirstPresenter
import ru.fabulus.fabulustrade.mvp.presenter.registration.trader.RegAsTraderFourPresenter
import ru.fabulus.fabulustrade.mvp.presenter.registration.trader.RegAsTraderSecondPresenter
import ru.fabulus.fabulustrade.mvp.presenter.registration.trader.RegAsTraderThirdPresenter
import ru.fabulus.fabulustrade.mvp.presenter.service.MessagingPresenter
import ru.fabulus.fabulustrade.mvp.presenter.subscriber.SubscriberMainPresenter
import ru.fabulus.fabulustrade.mvp.presenter.subscriber.SubscriberObservationPresenter
import ru.fabulus.fabulustrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.subscriber.SubscriberTradePresenter
import ru.fabulus.fabulustrade.mvp.presenter.trader.*
import ru.fabulus.fabulustrade.mvp.presenter.traderme.*
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersAllPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersFilterPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersMainPresenter
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.activity.MainActivity
import ru.fabulus.fabulustrade.ui.activity.SplashActivity
import ru.fabulus.fabulustrade.ui.adapter.*
import ru.fabulus.fabulustrade.ui.fragment.*
import ru.fabulus.fabulustrade.ui.fragment.entrance.*
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberMainFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberNewsFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.*
import ru.fabulus.fabulustrade.ui.fragment.traderme.*
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersAllFragment
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersFilterFragment
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersMainFragment
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
    fun inject(companyTradingOperationsJournalFragment: CompanyTradingOperationsJournalFragment)
    fun inject(companyTradingOperationsJournalPresenter: CompanyTradingOperationsJournalPresenter)
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
    fun inject(observationRVAdapter: ObservationRVAdapter?)
    fun inject(tradersAllRVAdapter: TradersAllRVAdapter?)
    fun inject(companyTradingOperationsRVAdapter: CompanyTradingOperationsRVAdapter?)
    fun inject(companyTradingOperationsJournalRVAdapter: CompanyTradingOperationsJournalRVAdapter?)
    fun inject(traderNewsImagesRVAdapter: TraderNewsImagesRVAdapter)
    fun inject(postRVAdapter: PostRVAdapter)
    fun inject(basePostFragment: BasePostFragment)
    fun inject(postDetailPresenter: PostDetailPresenter)
    fun inject(basePostPresenter: BasePostPresenter)
    fun inject(commentRVAdapter: CommentRVAdapter)
}