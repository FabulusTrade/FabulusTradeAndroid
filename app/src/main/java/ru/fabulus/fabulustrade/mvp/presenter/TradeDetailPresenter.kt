package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Bitmap
import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IImageListPresenter
import ru.fabulus.fabulustrade.mvp.view.TradeDetailView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.toStringFormat
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class TradeDetailPresenter(val trade: Trade, private val post: Post? = null) :
    MvpPresenter<TradeDetailView>(),
    IImageListPresenter {

    companion object {
        const val NEW_ARGUMENT_RESULT = "NEW_ARGUMENT_RESULT"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var imagesToAdd = mutableListOf<ByteArray>()

    private val imagesOnServerToDelete = mutableSetOf<String>()

    private var isOpeningTrade: Boolean = true

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        trade.trader?.username?.let { username -> viewState.setName(username) }
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

        if (trade.subtype.length > 0) {
            if (trade.subtype.subSequence(0, 8) == "Открытие") {
                isOpeningTrade = true
                viewState.setTradeType(TradeDetailFragment.TradeType.OPENING_TRADE)
            } else {
                isOpeningTrade = false
                viewState.setTradeType(TradeDetailFragment.TradeType.CLOSING_TRADE)
            }
        }

        profile.user?.let { user ->
            if (trade.traderId != user.id) {
                if (trade.posts == null) {
                    viewState.setMode(TradeDetailFragment.Mode.NOT_TRADER_NO_ARGUMENT)
                } else {
                    initArgument(true)
                }
            } else {
                if (trade.posts == null) {
                    viewState.setMode(TradeDetailFragment.Mode.TRADER_NO_ARGUMENT)
                } else {
                    initArgument(true)
                }
            }
        }

        if (!isOpeningTrade) {
            trade.profitCount?.let { profit ->
                if (profit < 0.0) {
                    viewState.setLoss(profit, 2)
                } else {
                    viewState.setProfit(profit, 2)
                }
            }
            trade.termOfTransaction?.let { term ->
                viewState.setDealTerm(term / 3600f / 24f, 2)
            }
        }
    }

    private fun initArgument(navigateToArgumentScreen: Boolean) {
        apiRepo
            .getArgumentByTrade(profile.token!!, trade.id.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ arguments ->
                val argument = arguments.results[0]
                if (navigateToArgumentScreen) {
                    navigateToTradeArgument(trade, argument)
                }
                if (isOpeningTrade) {
                    argument.takeProfit?.let { viewState.setTakeProfit(it) }
                    argument.stopLoss?.let { viewState.setStopLoss(it) }
                } else {
                    trade.profitCount?.let { profit ->
                        if (profit < 0.0) {
                            viewState.setLoss(profit, 2)
                        } else {
                            viewState.setProfit(profit, 2)
                        }
                    }
                }
                argument.dealTerm?.let {
                    if (isOpeningTrade) {
                        viewState.setDealTerm(it, 0)
                    } else {
                        viewState.setDealTerm(it, 2)
                    }
                }
            }, { error ->
                Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
            })
    }

    fun onPublishClicked(
        text: String,
        stopLoss: Float?,
        takeProfit: Float?,
        dealTerm: Int?,
    ) {
        if (text.length < 2 && stopLoss == null && takeProfit == null && dealTerm == null
            && (imagesToAdd.size < 1 || imagesToAdd.size > 4)
        )
            return
        createArgument(text, stopLoss, takeProfit, dealTerm)
    }

    fun addImages(newImages: List<Bitmap>) {
        if (newImages.isEmpty()) return
        val remain =
            max(
                CreatePostPresenter.MAX_ATTACHED_IMAGE_COUNT - (post?.images?.size
                    ?: 0) + imagesToAdd.size + imagesOnServerToDelete.size,
                0
            )
        val imageCountToAdd = min(newImages.size, remain)
        repeat(imageCountToAdd) { addImage(newImages[it]) }
        viewState.showImagesAddedMessage(imageCountToAdd)
    }

    private fun updateListOfImage(deletedImage: CreatePostPresenter.ImageOfPost? = null) {
        (post?.let { post ->
            post.images.map { CreatePostPresenter.ImageOfPost.ImageOnBack(it) }
        }
            ?.let { imageList ->
                imageList - imagesOnServerToDelete.map {
                    CreatePostPresenter.ImageOfPost.ImageOnBack(it)
                }
            }
            ?: listOf<CreatePostPresenter.ImageOfPost>())
            .let { imageList ->
                imageList + imagesToAdd.map {
                    CreatePostPresenter.ImageOfPost.ImageOnDevice(it)
                }
            }
            .let { viewState.updateListOfImages(it, deletedImage) }
    }

    private fun addImage(imageBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        imagesToAdd.add(stream.toByteArray())
        updateListOfImage()
    }

    private fun createArgument(
        text: String,
        stopLoss: Float?,
        takeProfit: Float?,
        dealTerm: Int?,
    ) {
        if (trade.subtype.contains("LONG")) {
            if (takeProfit != null && takeProfit <= trade.price
                || stopLoss != null && stopLoss >= trade.price
            ) {
                viewState.showErrorMessage(resourceProvider.getStringResource(R.string.long_position_forecast_error))
                return
            }
        } else if (trade.subtype.contains("SHORT")) {
            if (takeProfit != null && takeProfit >= trade.price
                || stopLoss != null && stopLoss <= trade.price
            ) {
                viewState.showErrorMessage(resourceProvider.getStringResource(R.string.short_position_forecast_error))
                return
            }
        } else return

        apiRepo
            .createArgument(
                profile.token!!, profile.user!!.id,
                text,
                trade.id.toString(),
                imagesToAdd,
                stopLoss,
                takeProfit,
                dealTerm
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ post ->
                router.sendResult(NEW_ARGUMENT_RESULT, post)
                router.exit()
            }, {
                it.printStackTrace()
            })
    }

    fun closeClicked() {
        router.exit()
    }

    fun navigateToTradeArgument(trade: Trade, argument: Argument) {
        router.navigateTo(Screens.tradeArgumentScreen(trade, argument))
    }

    override fun markToDeleteImageOnServer(imageOfPost: CreatePostPresenter.ImageOfPost) {
        if (imageOfPost is CreatePostPresenter.ImageOfPost.ImageOnBack) {
            imagesOnServerToDelete.add(imageOfPost.image)
        } else if (imageOfPost is CreatePostPresenter.ImageOfPost.ImageOnDevice) {
            imagesToAdd.remove(imageOfPost.image)
        }
        updateListOfImage(imageOfPost)
    }
}