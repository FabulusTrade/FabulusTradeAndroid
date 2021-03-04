package ru.wintrade.navigation

import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.wintrade.ui.fragment.*

class Screens {
    class LoadingScreen : SupportAppScreen() {
        override fun getFragment() = LoadingFragment.newInstance()
    }

    class HomeScreen : SupportAppScreen() {
        override fun getFragment() = HomeFragment.newInstance()
    }

    class OnBoardScreen : SupportAppScreen() {
        override fun getFragment() = OnBoardFragment.newInstance()
    }

    class SignUpScreen : SupportAppScreen() {
        override fun getFragment() = SignUpFragment.newInstance()
    }

    class SplashScreen : SupportAppScreen() {
        override fun getFragment() = SplashFragment.newInstance()
    }
}