package ru.wintrade.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.ui.fragment.*
import ru.wintrade.ui.fragment.entrance.*
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
    fun onBoardScreen() = FragmentScreen { OnBoardFragment.newInstance() }

    fun signUpScreen() = FragmentScreen { SignUpFragment.newInstance() }

    fun signInScreen() = FragmentScreen { SignInFragment.newInstance() }

    fun resetPasswordScreen() = FragmentScreen { ResetPasswordFragment.newInstance() }

    fun smsConfirmScreen(phone: String) = FragmentScreen { SmsConfirmFragment.newInstance(phone) }

    fun traderMainScreen(trader: Trader) = FragmentScreen { TraderMainFragment.newInstance(trader) }

    fun traderMeSubTradeScreen(position: Int) =
        FragmentScreen { TraderMeSubTradeFragment.newInstance(position) }

    fun traderProfitScreen(traderStatistic: TraderStatistic) =
        FragmentScreen { TraderMeProfitFragment.newInstance(traderStatistic) }

    fun traderMeMainScreen() = FragmentScreen { TraderMeMainFragment.newInstance() }

    fun traderNewsScreen() = FragmentScreen { TraderMePostFragment.newInstance() }

    fun traderPopularInstrumentsScreen() =
        FragmentScreen { TraderPopularInstrumentsFragment.newInstance() }

    fun traderDealScreen(trader: Trader) =
        FragmentScreen { TraderTradeFragment.newInstance(trader) }

    fun tradersMainScreen() = FragmentScreen { TradersMainFragment.newInstance() }

    fun tradersAllScreen() = FragmentScreen { TradersAllFragment.newInstance() }

    fun tradersFilterScreen() = FragmentScreen { TradersFilterFragment.newInstance() }

    fun subscriberMainScreen() = FragmentScreen { SubscriberMainFragment.newInstance() }

    fun subscriberObservationScreen() =
        FragmentScreen { SubscriberObservationFragment.newInstance() }

    fun subscriberDealScreen() = FragmentScreen { SubscriberTradeFragment.newInstance() }

    fun subscriberNewsScreen() = FragmentScreen { SubscriberNewsFragment.newInstance() }

    fun tradeDetailScreen(trade: Trade) = FragmentScreen { TradeDetailFragment.newInstance(trade) }

    fun companyTradingOperationsScreen(traderId: String, companyId: Int) =
        FragmentScreen { CompanyTradingOperationsFragment.newInstance(traderId, companyId) }

    fun traderObservationScreen() = FragmentScreen { TraderMeObservationFragment.newInstance() }

    fun aboutWinTradeScreen() = FragmentScreen { AboutWinTradeFragment.newInstance() }

    fun questionScreen() = FragmentScreen { QuestionFragment.newInstance() }

    fun settingsScreen() = FragmentScreen { SettingsFragment.newInstance() }

    fun friendInviteScreen() = FragmentScreen { FriendInviteFragment.newInstance() }

    fun createPostScreen(
        postId: String?,
        isPublication: Boolean,
        isPinnedEdit: Boolean?,
        pinnedText: String?
    ) = FragmentScreen {
        CreatePostFragment.newInstance(postId, isPublication, isPinnedEdit, pinnedText)
    }

    fun registrationAsTraderFirstScreen() =
        FragmentScreen { RegistrationAsTraderFragmentFirst.newInstance() }

    fun registrationAsTraderSecondScreen() =
        FragmentScreen { RegistrationAsTraderFragmentSecond.newInstance() }

    fun registrationAsTraderSecondScreen(traderInfo: TraderRegistrationInfo) =
        FragmentScreen { RegistrationAsTraderFragmentSecond.newInstance(traderInfo) }

    fun registrationAsTraderThirdScreen(traderInfo: TraderRegistrationInfo) =
        FragmentScreen { RegistrationAsTraderFragmentThird.newInstance(traderInfo) }

    fun registrationAsTraderFourScreen() =
        FragmentScreen { RegistrationAsTraderFragmentFour.newInstance() }
}