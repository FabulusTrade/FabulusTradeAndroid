package ru.wintrade.navigation

import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.trader.*
import ru.wintrade.ui.fragment.traders.*
import ru.wintrade.ui.fragment.subscriber.*
import ru.wintrade.ui.fragment.traderforsubscriber.TraderForSubscriberMainFragment

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

    class TraderStatScreen(val trader: Trader) : SupportAppScreen() {
        override fun getFragment() = TraderMainFragment.newInstance(trader)
    }

    class TraderProfitScreen(val trader: Trader) : SupportAppScreen() {
        override fun getFragment() = TraderProfitFragment.newInstance(trader)
    }

    class TraderNewsScreen : SupportAppScreen() {
        override fun getFragment() = TraderPostFragment.newInstance()
    }

    class TraderPopularInstrumentsScreen : SupportAppScreen() {
        override fun getFragment() = TraderPopularInstrumentsFragment.newInstance()
    }

    class TraderDealScreen : SupportAppScreen() {
        override fun getFragment() = TraderTradeFragment.newInstance()
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
        override fun getFragment() = SubscriberTradeFragment.newInstance()
    }

    class SubscriberNewsScreen : SupportAppScreen() {
        override fun getFragment() = SubscriberNewsFragment.newInstance()
    }

    class TradeDetailScreen(val trade: Trade) : SupportAppScreen() {
        override fun getFragment() = TradeDetailFragment.newInstance(trade)
    }

    class CompanyTradingOperationsScreen() : SupportAppScreen() {
        override fun getFragment() = CompanyTradingOperationsFragment.newInstance()
    }

    class TraderObservationScreen() : SupportAppScreen() {
        override fun getFragment() = TraderObservationFragment.newInstance()
    }

    class TraderForSubscriberMainScreen(val trader: Trader) : SupportAppScreen() {
        override fun getFragment() = TraderForSubscriberMainFragment.newInstance(trader)
    }

    class AboutWinTradeScreen : SupportAppScreen() {
        override fun getFragment() = AboutWinTradeFragment.newInstance()
    }

    class QuestionScreen : SupportAppScreen() {
        override fun getFragment() = QuestionFragment.newInstance()
    }

    class SettingsScreen : SupportAppScreen() {
        override fun getFragment() = SettingsFragment.newInstance()
    }

    class FriendInviteScreen : SupportAppScreen() {
        override fun getFragment() = FriendInviteFragment.newInstance()
    }
}