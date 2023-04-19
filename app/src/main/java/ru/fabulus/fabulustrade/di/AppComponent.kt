package ru.fabulus.fabulustrade.di

import dagger.Component
import ru.fabulus.fabulustrade.di.module.ApiModule
import ru.fabulus.fabulustrade.di.module.AppModule
import ru.fabulus.fabulustrade.di.module.CiceroneModule
import ru.fabulus.fabulustrade.di.module.HelperModule
import ru.fabulus.fabulustrade.di.module.ProfileModule
import ru.fabulus.fabulustrade.di.module.RepoModule
import ru.fabulus.fabulustrade.mvp.presenter.AboutWinTradePresenter
import ru.fabulus.fabulustrade.mvp.presenter.BasePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.BlacklistPresenter
import ru.fabulus.fabulustrade.mvp.presenter.CommentPostDetailPresenter
import ru.fabulus.fabulustrade.mvp.presenter.CompanyTradingOperationsJournalPresenter
import ru.fabulus.fabulustrade.mvp.presenter.CompanyTradingOperationsPresenter
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.FriendInvitePresenter
import ru.fabulus.fabulustrade.mvp.presenter.MainPresenter
import ru.fabulus.fabulustrade.mvp.presenter.PostDetailPresenter
import ru.fabulus.fabulustrade.mvp.presenter.QuestionPresenter
import ru.fabulus.fabulustrade.mvp.presenter.SettingsPresenter
import ru.fabulus.fabulustrade.mvp.presenter.SmsConfirmPresenter
import ru.fabulus.fabulustrade.mvp.presenter.TradeArgumentPresenter
import ru.fabulus.fabulustrade.mvp.presenter.TradeDetailPresenter
import ru.fabulus.fabulustrade.mvp.presenter.WebViewPresenter
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
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderMainPresenter
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderPopularInstrumentsPresenter
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderPostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderProfitPresenter
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderTradePresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeMainPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeObservationPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeSubTradePresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeTradePresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersAllPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersFilterPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersMainPresenter
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.activity.MainActivity
import ru.fabulus.fabulustrade.ui.activity.SplashActivity
import ru.fabulus.fabulustrade.ui.adapter.BlacklistRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.CompanyTradingOperationsJournalRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.CompanyTradingOperationsRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.ObservationRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.PostRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.TraderNewsImagesRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.TradersAllRVAdapter
import ru.fabulus.fabulustrade.ui.fragment.AboutWinTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.BasePostFragment
import ru.fabulus.fabulustrade.ui.fragment.BlacklistFragment
import ru.fabulus.fabulustrade.ui.fragment.CompanyTradingOperationsFragment
import ru.fabulus.fabulustrade.ui.fragment.CompanyTradingOperationsJournalFragment
import ru.fabulus.fabulustrade.ui.fragment.CreatePostFragment
import ru.fabulus.fabulustrade.ui.fragment.FriendInviteFragment
import ru.fabulus.fabulustrade.ui.fragment.QuestionFragment
import ru.fabulus.fabulustrade.ui.fragment.SettingsFragment
import ru.fabulus.fabulustrade.ui.fragment.SmsConfirmFragment
import ru.fabulus.fabulustrade.ui.fragment.TradeArgumentFragment
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment
import ru.fabulus.fabulustrade.ui.fragment.WebViewFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.OnBoardFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentFirst
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentFour
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentSecond
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentThird
import ru.fabulus.fabulustrade.ui.fragment.entrance.ResetPasswordFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.SignInFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.SignUpFragment
import ru.fabulus.fabulustrade.ui.fragment.generalfeed.GeneralFeedFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberMainFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberPostFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderMainFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderPostFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderProfitFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeMainFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMePostFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeProfitFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeSubTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeTradeFragment
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
    fun inject(subscriberPostFragment: SubscriberPostFragment)
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
    fun inject(generalFeedFragment: GeneralFeedFragment)
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
    fun inject(basePostPresenter: BasePostPresenter<BasePostView>)
    fun inject(commentPostDetailPresenter: CommentPostDetailPresenter)
    fun inject(commentRVAdapter: CommentRVAdapter)
    fun inject(blacklistFragment: BlacklistFragment)
    fun inject(blacklistPresenter: BlacklistPresenter)
    fun inject(blacklistRVAdapter: BlacklistRVAdapter)
    fun inject(tradeArgumentFragment: TradeArgumentFragment)
    fun inject(tradeArgumentPresenter: TradeArgumentPresenter)
}