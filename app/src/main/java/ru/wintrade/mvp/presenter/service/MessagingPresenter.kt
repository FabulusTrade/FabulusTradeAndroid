package ru.wintrade.mvp.presenter.service

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import ru.wintrade.R
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.ui.service.MessagingService
import ru.wintrade.util.formatDigitWithDef
import ru.wintrade.util.formatString
import ru.wintrade.util.getNotificationId
import javax.inject.Inject

class MessagingPresenter(private val service: MessagingService) {
    companion object {
        private const val TRADER_KEY = "trader"
        private const val COMPANY_KEY = "company"
        private const val PRICE_KEY = "price"
        private const val OPERATION_DATE_KEY = "operation_date"
        private const val TRADE_CURRENCY_KEY = "trade_currency"
        private const val OPERATION_TYPE_KEY = "operation_type"
    }

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var body = ""

    fun messageReceived(data: Map<String, String>) {
        val trader = data.getOrElse(TRADER_KEY) { "" }
        val operationType = data.getOrElse(OPERATION_TYPE_KEY) { "" }
        val company = data.getOrElse(COMPANY_KEY) { "" }
        var price = data.getOrElse(PRICE_KEY) { "" }
        val currency = data.getOrElse(TRADE_CURRENCY_KEY) { "" }
        var dateData = data.getOrElse(OPERATION_DATE_KEY) { "" }

        if (dateData.isNotEmpty()) {
            //передаем в переменную дату ДД/ММ и время ММ:ЧЧ
            dateData = resourceProvider.formatString(
                R.string.push_date_pattern,
                dateData.substring(0, 5),
                dateData.substring(7, 12)
            )
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

        body = resourceProvider.formatString(
            R.string.push_body_pattern,
            company,
            price,
            currency
        )

        service.showNotification(title, body, getNotificationId())
        apiRepo.newTradeSubject.onNext(true)
    }
}