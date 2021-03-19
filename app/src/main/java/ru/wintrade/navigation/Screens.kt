package ru.wintrade.navigation

import androidx.fragment.app.Fragment
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

    class AllTradersScreen : SupportAppScreen() {
        override fun getFragment() = AllTradersFragment.newInstance()
    }

    class TraderStatScreen : SupportAppScreen() {
        override fun getFragment() = TraderStatFragment.newInstance()
    }
}