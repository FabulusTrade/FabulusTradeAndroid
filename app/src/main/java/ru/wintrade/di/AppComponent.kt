package ru.wintrade.di

import dagger.Component
import ru.wintrade.di.module.*
import ru.wintrade.mvp.presenter.*
import ru.wintrade.mvp.presenter.service.MessagingPresenter
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import ru.wintrade.ui.activity.SplashActivity
import ru.wintrade.ui.fragment.*
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
    fun inject(homeFragment: HomeFragment)
    fun inject(homePresenter: HomePresenter)
    fun inject(loadingFragment: LoadingFragment)
    fun inject(loadingPresenter: LoadingPresenter)
    fun inject(signUpFragment: SignUpFragment)
    fun inject(signUpPresenter: SignUpPresenter)
    fun inject(onBoardFragment: OnBoardFragment)
    fun inject(onBoardPresenter: OnBoardPresenter)
    fun inject(smsConfirmPresenter: SmsConfirmPresenter)
    fun inject(smsConfirmFragment: SmsConfirmFragment)
    fun inject(signInFragment: SignInFragment)
    fun inject(signInPresenter: SignInPresenter)
    fun inject(allTradersFragment: AllTradersFragment)
    fun inject(allTradersPresenter: AllTradersPresenter)
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
    fun inject(app: App)
}