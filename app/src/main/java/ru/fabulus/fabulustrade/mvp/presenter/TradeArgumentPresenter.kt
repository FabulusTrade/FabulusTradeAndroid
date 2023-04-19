package ru.fabulus.fabulustrade.mvp.presenter

import android.util.Log
import com.github.terrakok.cicerone.ResultListenerHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.view.TradeArgumentView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.isCanDeletePost
import ru.fabulus.fabulustrade.util.isCanEditPost
import ru.fabulus.fabulustrade.util.toStringFormat
import kotlin.math.roundToInt

class TradeArgumentPresenter(val trade: Trade) : BasePostPresenter<TradeArgumentView>() {

    var argument: Argument? = null

    companion object {
        private const val TAG = "PostDetailPresenter"
    }

    private var isOpeningTrade: Boolean = true

    private var updateArgumentResultListener: ResultListenerHandler? = null

    override fun onFirstViewAttach() {
        App.instance.appComponent.inject(this)
        super.onFirstViewAttach()

        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)

        viewState.setRepostCount(argument?.repostCount.toString())

        viewState.setType(trade.operationType)
        viewState.setCompany(trade.company)
        viewState.setTicker(trade.ticker)
        viewState.setPrice(
            resourceProvider.formatDigitWithDef(
                R.string.tv_trade_detail_price_text,
                trade.price
            )
        )
        viewState.setPriceTitle(
            resourceProvider.formatString(
                R.string.tv_trade_detail_price_title_text,
                trade.currency
            )
        )
        viewState.setDate(trade.date.toStringFormat("dd.MM.yyyy / HH:mm"))

        viewState.setSubtype(trade.subtype)

        if (trade.subtype.isNotEmpty()) {
            if (trade.subtype.subSequence(0, 8) == "Открытие") {
                viewState.setTradeType(TradeDetailFragment.TradeType.OPENING_TRADE)
            } else {
                viewState.setTradeType(TradeDetailFragment.TradeType.CLOSING_TRADE)
            }
        }

        if (trade.subtype.isNotEmpty()) {
            if (trade.subtype.subSequence(0, 8) == "Открытие") {
                isOpeningTrade = true
                viewState.setTradeType(TradeDetailFragment.TradeType.OPENING_TRADE)
            } else {
                isOpeningTrade = false
                viewState.setTradeType(TradeDetailFragment.TradeType.CLOSING_TRADE)
            }
        }

        initArgument()
    }

    private fun initArgument() {
        apiRepo
            .getArgumentByTrade(profile.token!!, trade.id.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ arguments ->
                argument = arguments.results[0]
                super.assignArgument(argument!!)
                super.initPost()
                if (isOpeningTrade) {
                    argument!!.takeProfit?.let { viewState.setTakeProfit(it) }
                    argument!!.stopLoss?.let { viewState.setStopLoss(it) }
                } else {
                    trade.profitCount?.let { profit ->
                        if (profit < 0.0) {
                            viewState.setLoss(profit / 100.0f, 2)
                        } else {
                            viewState.setProfit(profit / 100.0f, 2)
                        }
                    }
                }
                if (isOpeningTrade) {
                    argument!!.dealTerm?.let {
                        viewState.setDealTerm(it.roundToInt())
                    }
                    viewState.relocateKebabForOpeningTrade()
                } else {
                    argument!!.closedDealTerm?.let {
                        viewState.setDealTerm(it / 3600.0 / 24.0, 2)
                    }
                }
                initMenu()
            }, { error ->
                Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
            })
    }

    private fun initMenu() {
        argument?.let {
            if (isSelfArgument(argument!!)) {
                viewState.setPostMenuSelf(argument!!)
            } else {
                fillComplaints()
            }
        }
    }

    private fun fillComplaints() {
        apiRepo
            .getComplaints(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ complaintList ->
                argument?.let { viewState.setPostMenuSomeone(it, complaintList) }
            }, { error ->
                Log.d(TAG, "Error: ${error.message.toString()}")
                Log.d(TAG, error.printStackTrace().toString())
            }
            )
    }

    private fun isSelfArgument(argument: Argument): Boolean {
        return argument.traderId == profile.user?.id
    }

    fun editArgument() {
        if (isCanEditPost(post.dateCreate)) {
            prepareEditArgument()
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_edited))
        }
    }

    private fun prepareEditArgument() {
        updateArgumentResultListener =
            router.setResultListener(CreatePostPresenter.UPDATE_POST_RESULT) { updatedArgument ->
                (updatedArgument as? Post)?.let {
                    post = updatedArgument
                    viewState.setPostText(post.text)
                    viewState.setPostImages(post.images)
                    router.sendResult(
                        CreatePostPresenter.UPDATE_POST_IN_FRAGMENT_RESULT,
                        updatedArgument
                    )
                }
            }

        router.replaceScreen(
            Screens.tradeDetailScreen(
                trade,
                true
            )
        )
    }

    fun deleteArgument() {
        if (argument == null) return
        if (isCanDeletePost(argument!!.dateCreate)) {
            apiRepo.deletePost(profile.token!!, argument!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    router.sendResult(CreatePostPresenter.DELETE_POST_RESULT, "")
                    router.exit()
                }, {})
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.post_can_not_be_deleted))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateArgumentResultListener?.dispose()
    }
}