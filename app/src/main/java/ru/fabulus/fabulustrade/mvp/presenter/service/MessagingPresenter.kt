package ru.fabulus.fabulustrade.mvp.presenter.service

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.ui.service.MessagingService
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.getNotificationId
import ru.fabulus.fabulustrade.util.toSpanned
import javax.inject.Inject

class MessagingPresenter(private val service: MessagingService) {
    companion object {
        private const val TRADER_KEY = "trader"
        private const val COMPANY_KEY = "company"
        private const val PRICE_KEY = "price"
        private const val OPERATION_DATE_KEY = "operation_date"
        private const val TRADE_CURRENCY_KEY = "trade_currency"
        private const val OPERATION_TYPE_KEY = "operation_type"
        private const val DELAYED_TRADE_KEY = "delayed_trade"
        private const val OPERATION_SUBTYPE_KEY = "operation_subtype"
        private const val OPERATION_SUBTYPE_OPENING_VALUE = "opening"
        private const val OPERATION_SUBTYPE_CLOSING_VALUE = "closing"

        private const val AUTHOR_KEY = "author"
        private const val ID_AUTHOR_POST = "id_author_post"
        private const val POST_TEXT_KEY = "post_text"
        private const val ID_POST_KEY = "id_post"
        private const val COMMENT_TEXT_KEY = "comment_text"
        private const val ID_COMMENT_KEY = "id_comment"
        private const val AUTHOR_COMMENT_KEY = "author_comment"
        private const val AUTHOR_ANSWER_KEY = "author_answer"
        private const val ANSWER_TEXT_KEY = "answer_text"
    }

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var operationSubTypeResult = ""

    fun messageReceived(data: Map<String, String>) {
        val trader = data.getOrElse(TRADER_KEY) { "" }
        val operationType = data.getOrElse(OPERATION_TYPE_KEY) { "" }
        val company = data.getOrElse(COMPANY_KEY) { "" }
        val price = data.getOrElse(PRICE_KEY) { "" }
        val currency = data.getOrElse(TRADE_CURRENCY_KEY) { "" }
        val dateData = data.getOrElse(OPERATION_DATE_KEY) { "" }
        val isDelayedTrade = data.getOrElse(DELAYED_TRADE_KEY) { "" }.uppercase() == "TRUE"
        val operationSubType = data.getOrElse(OPERATION_SUBTYPE_KEY) { "" }

        val author = data.getOrElse(AUTHOR_KEY) { "" }
        val idAuthorPost = data.getOrElse(ID_AUTHOR_POST) { "" }
        val postText = data.getOrElse(POST_TEXT_KEY) { "" }
        val idPost = data.getOrElse(ID_POST_KEY) { "" }
        val commentText = data.getOrElse(COMMENT_TEXT_KEY) { "" }
        val idComment = data.getOrElse(ID_COMMENT_KEY) { "" }
        val authorComment = data.getOrElse(AUTHOR_COMMENT_KEY) { "" }
        val authorAnswer = data.getOrElse(AUTHOR_ANSWER_KEY) { "" }
        val answerText = data.getOrElse(ANSWER_TEXT_KEY) { "" }

        when {
            trader.isNotEmpty() && operationType.isNotEmpty() && company.isNotEmpty() && price.isNotEmpty()
                    && currency.isNotEmpty() -> {
                financialOperationsPush(
                    trader,
                    operationType,
                    company,
                    price,
                    currency,
                    dateData,
                    isDelayedTrade,
                    operationSubType
                )
            }
            authorComment.isNotEmpty() && authorAnswer.isNotEmpty() && answerText.isNotEmpty()
                    && idPost.isNotEmpty() && idComment.isNotEmpty() -> {
                if (idAuthorPost == profile.user?.id){
                    service.showNotificationNewComment(
                        title = resourceProvider.getStringResource(R.string.push_new_comment_title),
                        message = "$authorAnswer: @$authorComment $answerText",
                        idPost = idPost.toInt(),
                        idComment = idComment.toInt(),
                        getNotificationId()
                    )
                }else{
                    service.showNotificationNewComment(
                        title = resourceProvider.getStringResource(R.string.push_new_mention_title),
                        message = "$authorAnswer: @$authorComment $answerText",
                        idPost = idPost.toInt(),
                        idComment = idComment.toInt(),
                        getNotificationId()
                    )
                }

            }
            author.isNotEmpty() && commentText.isNotEmpty() && idPost.isNotEmpty() && idComment.isNotEmpty() -> {
                service.showNotificationNewComment(
                    title = resourceProvider.getStringResource(R.string.push_new_comment_title),
                    message = "$author: $commentText",
                    idPost = idPost.toInt(),
                    idComment = idComment.toInt(),
                    getNotificationId()
                )
            }
            author.isNotEmpty() && postText.isNotEmpty() && idPost.isNotEmpty() -> {
                service.showNotificationNewPost(
                    title = resourceProvider.getStringResource(R.string.push_new_post_title),
                    message = "$author: $postText",
                    idPost = idPost.toInt(),
                    getNotificationId()
                )
            }
        }
    }

    private fun financialOperationsPush(
        trader: String,
        operationType: String,
        company: String,
        price: String,
        currency: String,
        date: String,
        isDelayedTrade: Boolean,
        operationSubType: String
    ) {
        var isSale: Boolean? = null
        var priceData = price
        var dateData = date

        if (operationType.uppercase() == resourceProvider.getStringResource(R.string.uppercased_sale_string)
        ) {
            isSale = true
        }
        if (operationType.uppercase() == resourceProvider.getStringResource(R.string.uppercased_buy_string)
        ) {
            isSale = false
        }

        operationSubTypeResult = when (operationSubType) {
            OPERATION_SUBTYPE_OPENING_VALUE ->
                resourceProvider.getStringResource(R.string.push_body_opening_subtype) + " " + isSale?.let {
                    if (isSale!!) resourceProvider.getStringResource(R.string.short_position)
                    else resourceProvider.getStringResource(R.string.long_position)
                }
            OPERATION_SUBTYPE_CLOSING_VALUE ->
                resourceProvider.getStringResource(R.string.push_body_closing_subtype) + " " + isSale?.let {
                    if (isSale!!) resourceProvider.getStringResource(R.string.long_position)
                    else resourceProvider.getStringResource(R.string.short_position)
                }
            else -> resourceProvider.getStringResource(R.string.empty_string)
        }

        val operationResultTitle = operationSubTypeResult.toSpanned()

        dateData = if (isDelayedTrade && dateData.isNotEmpty()) {
            //передаем в переменную дату ДД/ММ и время ММ:ЧЧ
            resourceProvider.formatString(
                R.string.push_date_pattern,
                dateData.substring(0, 5),
                dateData.substring(7, 12)
            )
        } else {
            ""
        }

        if (priceData.isNotEmpty()) {
            priceData = resourceProvider.formatDigitWithDef(
                R.string.push_price_pattern,
                priceData.toFloat()
            )
        }

        val title = HtmlCompat.fromHtml(
            resourceProvider.formatString(
                R.string.push_title_pattern,
                trader,
                operationType,
                dateData
            ), FROM_HTML_MODE_LEGACY
        )

        val body = resourceProvider.formatString(
            R.string.push_body_pattern,
            company,
            priceData,
            currency
        )

        service.showNotificationFinancialOperation(
            title,
            operationResultTitle,
            body,
            getNotificationId()
        )
        apiRepo.newTradeSubject.onNext(true)
    }

    fun updateToken(token: String) {
        if (profile.token != null) {
            apiRepo.postDeviceToken(profile.token!!, token)
        }
    }
}