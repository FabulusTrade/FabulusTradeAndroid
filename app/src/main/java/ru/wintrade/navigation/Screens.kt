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

    class SmsConfirmScreen(val phone: String) : SupportAppScreen() {
        override fun getFragment() = SmsConfirmFragment.newInstance(phone)
    }

    class SignInScreen : SupportAppScreen() {
        override fun getFragment() = SignInFragment.newInstance()
    }

    class MainTradersScreen : SupportAppScreen() {
        override fun getFragment() = MainTradersFragment.newInstance()
    }

    class TraderStatScreen : SupportAppScreen() {
        override fun getFragment() = TraderStatFragment.newInstance()
    }

    class TraderProfitScreen : SupportAppScreen() {
        override fun getFragment() = TraderProfitFragment.newInstance()
    }

    class TraderNewsScreen : SupportAppScreen() {
        override fun getFragment() = TraderNewsFragment.newInstance()
    }

    class TraderPopularInstrumentsScreen : SupportAppScreen() {
        override fun getFragment() = TraderPopularInstrumentsFragment.newInstance()
    }

    class TraderDealScreen : SupportAppScreen() {
        override fun getFragment() = TraderDealFragment.newInstance()
    }

    class AllTradersScreen : SupportAppScreen() {
        override fun getFragment() = AllTradersFragment.newInstance()
    }
}