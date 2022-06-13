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
        var price = data.getOrElse(PRICE_KEY) { "" }
        val currency = data.getOrElse(TRADE_CURRENCY_KEY) { "" }
        var dateData = data.getOrElse(OPERATION_DATE_KEY) { "" }
        val isDelayedTrade = data.getOrElse(DELAYED_TRADE_KEY) { "" }.uppercase() == "TRUE"
        val operationSubType = data.getOrElse(OPERATION_SUBTYPE_KEY) { "" }

        var isSale: Boolean? = null

        if (operationType.uppercase()
                .equals(resourceProvider.getStringResource(R.string.uppercased_sale_string))
        )
            isSale = true
        if (operationType.uppercase()
                .equals(resourceProvider.getStringResource(R.string.uppercased_buy_string))
        )
            isSale = false

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

        if (price.isNotEmpty()) {
            price =
                resourceProvider.formatDigitWithDef(R.string.push_price_pattern, price.toFloat())
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
            price,
            currency
        )

        service.showNotification(title, operationResultTitle, body, getNotificationId())
        apiRepo.newTradeSubject.onNext(true)
    }

    fun updateToken(token: String) {
        if (profile.token != null) {
            apiRepo.postDeviceToken(profile.token!!, token)
        }
    }
}