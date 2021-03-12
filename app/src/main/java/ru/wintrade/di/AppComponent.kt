package ru.wintrade.di

import dagger.Component
import ru.wintrade.di.module.AppModule
import ru.wintrade.di.module.CiceroneModule
import ru.wintrade.di.module.RepoModule
import ru.wintrade.mvp.presenter.*
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import ru.wintrade.ui.fragment.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        RepoModule::class
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
    fun inject(entranceFragment: EntranceFragment)
    fun inject(entrancePresenter: EntrancePresenter)
    fun inject(app: App)
}