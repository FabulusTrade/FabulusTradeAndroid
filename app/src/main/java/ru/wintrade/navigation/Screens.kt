package ru.wintrade.navigation

import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.trader.*
import ru.wintrade.ui.fragment.traders.*
import ru.wintrade.ui.fragment.subscriber.*

class Screens {
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

    class TradersMainScreen : SupportAppScreen() {
        override fun getFragment() = TradersMainFragment.newInstance()
    }

    class TradersAllScreen : SupportAppScreen() {
        override fun getFragment() = TradersAllFragment.newInstance()
    }

    class TradersFilterScreen : SupportAppScreen() {
        override fun getFragment() = TradersFilterFragment.newInstance()
    }

    class SubscriberMainScreen : SupportAppScreen() {
        override fun getFragment() = SubscriberMainFragment.newInstance()
    }

    class SubscriberObservationScreen : SupportAppScreen() {
        override fun getFragment() = SubscriberObservationFragment.newInstance()
    }

    class SubscriberDealScreen : SupportAppScreen() {
        override fun getFragment() = SubscriberDealFragment.newInstance()
    }

    class SubscriberNewsScreen : SupportAppScreen() {
        override fun getFragment() = SubscriberNewsFragment.newInstance()
    }
}