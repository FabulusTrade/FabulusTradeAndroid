package ru.fabulus.fabulustrade.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.ui.fragment.AboutWinTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.BlacklistFragment
import ru.fabulus.fabulustrade.ui.fragment.CompanyTradingOperationsFragment
import ru.fabulus.fabulustrade.ui.fragment.CompanyTradingOperationsJournalFragment
import ru.fabulus.fabulustrade.ui.fragment.CreatePostFragment
import ru.fabulus.fabulustrade.ui.fragment.FriendInviteFragment
import ru.fabulus.fabulustrade.ui.fragment.ImageBrowsingFragment
import ru.fabulus.fabulustrade.ui.fragment.PostDetailFragment
import ru.fabulus.fabulustrade.ui.fragment.QuestionFragment
import ru.fabulus.fabulustrade.ui.fragment.SettingsFragment
import ru.fabulus.fabulustrade.ui.fragment.SmsConfirmFragment
import ru.fabulus.fabulustrade.ui.fragment.TradeArgumentFragment
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment
import ru.fabulus.fabulustrade.ui.fragment.WebViewFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.OnBoardFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentFirst
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentFour
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentSecond
import ru.fabulus.fabulustrade.ui.fragment.entrance.RegistrationAsTraderFragmentThird
import ru.fabulus.fabulustrade.ui.fragment.entrance.ResetPasswordFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.SignInFragment
import ru.fabulus.fabulustrade.ui.fragment.entrance.SignUpFragment
import ru.fabulus.fabulustrade.ui.fragment.generalfeed.GeneralFeedFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberMainFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberPostFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderMainFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeMainFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMePostFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeProfitFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeSubTradeFragment
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersAllFragment
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersFilterFragment
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersMainFragment

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

    fun subscriberNewsScreen() = FragmentScreen { SubscriberPostFragment.newInstance() }

    fun tradeDetailScreen(trade: Trade, editMode: Boolean) = FragmentScreen { TradeDetailFragment.newInstance(trade, editMode) }

    fun companyTradingOperationsScreen(traderId: String, companyId: Int, isMyOperations: Boolean = false) =
        FragmentScreen { CompanyTradingOperationsFragment.newInstance(traderId, companyId, isMyOperations) }

    fun companyTradingOperationsJournalScreen(traderId: String, companyId: Int) =
        FragmentScreen { CompanyTradingOperationsJournalFragment.newInstance(traderId, companyId) }

    fun traderObservationScreen() = FragmentScreen { TraderMeObservationFragment.newInstance() }

    fun aboutWinTradeScreen() = FragmentScreen { AboutWinTradeFragment.newInstance() }

    fun questionScreen() = FragmentScreen { QuestionFragment.newInstance() }

    fun settingsScreen() = FragmentScreen { SettingsFragment.newInstance() }

    fun friendInviteScreen() = FragmentScreen { FriendInviteFragment.newInstance() }

    fun createPostScreen(
        post: Post?,
        isPublication: Boolean,
        isPinnedEdit: Boolean?,
        pinnedText: String?
    ) = FragmentScreen {
        CreatePostFragment.newInstance(post, isPublication, isPinnedEdit, pinnedText)
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

    fun imageBrowsingFragment(urls: List<String>, position: Int = 0) =
        FragmentScreen { ImageBrowsingFragment.newInstance(urls, position) }


    fun postDetailFragment(post: Post, navigateFromGeneralFeed: Boolean = false) =
        FragmentScreen { PostDetailFragment.newInstance(post, navigateFromGeneralFeed) }

    fun generalFeedFragment() = FragmentScreen{ GeneralFeedFragment.newInstance() }

    fun blacklistScreen() =
        FragmentScreen { BlacklistFragment.newInstance() }

    fun tradeArgumentScreen(trade: Trade, argument: Argument) =
        FragmentScreen { TradeArgumentFragment.newInstance(trade, argument) }

    fun tradeArgumentScreen(trade: Trade) =
        FragmentScreen { TradeArgumentFragment.newInstance(trade) }
}