package ru.wintrade.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.wintrade.mvp.model.entity.SignUpData
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
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

    fun signUpScreen(asTraderRegistration: Boolean) =
        FragmentScreen { SignUpFragment.newInstance(asTraderRegistration) }

    fun signInScreen(isSubscriber: Boolean) =
        FragmentScreen { SignInFragment.newInstance(isSubscriber) }

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

    fun tradersMainScreen(checkedFilter: Int?) =
        FragmentScreen { TradersMainFragment.newInstance(checkedFilter) }

    fun tradersAllScreen(checkedFilter: Int) =
        FragmentScreen { TradersAllFragment.newInstance(checkedFilter) }

    fun tradersFilterScreen(checkedFilter: Int) =
        FragmentScreen { TradersFilterFragment.newInstance(checkedFilter) }

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

    fun registrationAsTraderFirstScreen(signUpData: SignUpData) =
        FragmentScreen { RegistrationAsTraderFragmentFirst.newInstance(signUpData) }

    fun registrationAsTraderSecondScreen() =
        FragmentScreen { RegistrationAsTraderFragmentSecond.newInstance() }

    fun registrationAsTraderSecondScreen(signUpData: SignUpData) =
        FragmentScreen { RegistrationAsTraderFragmentSecond.newInstance(signUpData) }

    fun registrationAsTraderThirdScreen(signUpData: SignUpData) =
        FragmentScreen { RegistrationAsTraderFragmentThird.newInstance(signUpData) }

    fun registrationAsTraderFourScreen() =
        FragmentScreen { RegistrationAsTraderFragmentFour.newInstance() }

    fun webViewFragment(url: String) =
        FragmentScreen { WebViewFragment.newInstance(url) }

    fun imageBrowsingFragment(urlImage: String) =
        FragmentScreen { ImageBrowsingFragment.newInstance(urlImage) }
}