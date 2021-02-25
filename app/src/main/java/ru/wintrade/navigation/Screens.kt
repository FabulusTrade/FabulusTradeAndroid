package ru.wintrade.navigation

import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.wintrade.ui.fragment.HomeFragment
import ru.wintrade.ui.fragment.LoadingFragment
import ru.wintrade.ui.fragment.OnBoardFragment
import ru.wintrade.ui.fragment.SignUpFragment

class Screens {
    class LoadingScreen: SupportAppScreen() {
        override fun getFragment() = LoadingFragment.newInstance()
    }

    class HomeScreen: SupportAppScreen() {
        override fun getFragment() = HomeFragment.newInstance()
    }

    class OnBoardScreen: SupportAppScreen() {
        override fun getFragment() = OnBoardFragment.newInstance()
    }

    class SignUpScreen: SupportAppScreen() {
        override fun getFragment() = SignUpFragment.newInstance()
    }
}