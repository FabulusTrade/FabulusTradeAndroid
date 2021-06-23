package ru.wintrade.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.entrance.OnBoardFragment
import ru.wintrade.ui.fragment.entrance.ResetPasswordFragment
import ru.wintrade.ui.fragment.entrance.SignInFragment
import ru.wintrade.ui.fragment.entrance.SignUpFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberMainFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberNewsFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberObservationFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberTradeFragment
import ru.wintrade.ui.fragment.trader.TraderMainFragment
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.trader.TraderTradeFragment
import ru.wintrade.ui.fragment.traderme.*
import ru.wintrade.ui.fragment.traders.TradersAllFragment
import ru.wintrade.ui.fragment.traders.TradersFilterFragment
import ru.wintrade.ui.fragment.traders.TradersMainFragment

object Screens {
    fun OnBoardScreen() = FragmentScreen { OnBoardFragment.newInstance() }

    fun SignUpScreen() = FragmentScreen { SignUpFragment.newInstance() }

    fun SignInScreen() = FragmentScreen { SignInFragment.newInstance() }

    fun ResetPasswordScreen() = FragmentScreen { ResetPasswordFragment.newInstance() }

    fun SmsConfirmScreen(phone: String) = FragmentScreen { SmsConfirmFragment.newInstance(phone) }

    fun TraderMainScreen(trader: Trader) = FragmentScreen { TraderMainFragment.newInstance(trader) }

    fun TraderMeSubTradeScreen(position: Int) =
        FragmentScreen { TraderMeSubTradeFragment.newInstance(position) }

    fun TraderProfitScreen() = FragmentScreen { TraderMeProfitFragment.newInstance() }

    fun TraderMeMainScreen() = FragmentScreen { TraderMeMainFragment.newInstance() }

    fun TraderNewsScreen() = FragmentScreen { TraderMePostFragment.newInstance() }

    fun TraderPopularInstrumentsScreen() =
        FragmentScreen { TraderPopularInstrumentsFragment.newInstance() }

    fun TraderDealScreen() = FragmentScreen { TraderTradeFragment.newInstance() }

    fun TradersMainScreen() = FragmentScreen { TradersMainFragment.newInstance() }

    fun TradersAllScreen() = FragmentScreen { TradersAllFragment.newInstance() }

    fun TradersFilterScreen() = FragmentScreen { TradersFilterFragment.newInstance() }

    fun SubscriberMainScreen() = FragmentScreen { SubscriberMainFragment.newInstance() }

    fun SubscriberObservationScreen() =
        FragmentScreen { SubscriberObservationFragment.newInstance() }

    fun SubscriberDealScreen() = FragmentScreen { SubscriberTradeFragment.newInstance() }

    fun SubscriberNewsScreen() = FragmentScreen { SubscriberNewsFragment.newInstance() }

    fun TradeDetailScreen(trade: Trade) = FragmentScreen { TradeDetailFragment.newInstance(trade) }

    fun CompanyTradingOperationsScreen() =
        FragmentScreen { CompanyTradingOperationsFragment.newInstance() }

    fun TraderObservationScreen() = FragmentScreen { TraderMeObservationFragment.newInstance() }

    fun AboutWinTradeScreen() = FragmentScreen { AboutWinTradeFragment.newInstance() }

    fun QuestionScreen() = FragmentScreen { QuestionFragment.newInstance() }

    fun SettingsScreen() = FragmentScreen { SettingsFragment.newInstance() }

    fun FriendInviteScreen() = FragmentScreen { FriendInviteFragment.newInstance() }

    fun CreatePostScreen(isPinnedEdit: Boolean?, pinnedText: String?) = FragmentScreen {
        CreatePostFragment.newInstance(isPinnedEdit, pinnedText)
    }
}