package ru.wintrade.mvp.presenter.service

import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.ui.service.MessagingService
import ru.wintrade.util.getNotificationId
import javax.inject.Inject

class MessagingPresenter(private val service: MessagingService) {
    companion object {
        private const val ID_KEY = "id"
        private const val TRADER_KEY = "id"
        private const val OPERATION_TYPE_KEY = "id"
        private const val COMPANY_KEY = "id"
        private const val COMPANY_IMG_KEY = "id"
        private const val TICKER_KEY = "id"
        private const val ORDER_STATUS_KEY = "id"
        private const val PRICE_KEY = "id"
        private const val COUNT_KEY = "id"
        private const val CURRENCY_KEY = "id"
        private const val DATE_KEY = "id"
        private const val PROFIT_COUNT_KEY = "id"
        private const val OPERATION_TYPE_NAME_KEY = "id"
    }

    @Inject
    lateinit var apiRepo: ApiRepo

    fun messageReceived(data: Map<String, String>) {
        val id = data.getOrElse(ID_KEY) { "" }
        val trader = data.getOrElse(TRADER_KEY) { "" }
        val operationType = data.getOrElse(OPERATION_TYPE_KEY) { "" }
        val company = data.getOrElse(COMPANY_KEY) { "" }
        val companyImg = data.getOrElse(COMPANY_IMG_KEY) { "" }
        val ticker = data.getOrElse(TICKER_KEY) { "" }
        val orderStatus = data.getOrElse(ORDER_STATUS_KEY) { "" }
        val price = data.getOrElse(PRICE_KEY) { "" }
        val count = data.getOrElse(COUNT_KEY) { "" }
        val currency = data.getOrElse(CURRENCY_KEY) { "" }
        val dateData = data.getOrElse(DATE_KEY) { "" }
        val profitCount = data.getOrElse(PROFIT_COUNT_KEY) { "" }
        val operationTypeName = data.getOrElse(OPERATION_TYPE_NAME_KEY) { "" }
        val title = "$trader: $operationTypeName"
        val body = "$ticker по цене: $price"
        service.showNotification(title, body, getNotificationId())
        apiRepo.newTradeSubject.onNext(true)
    }
}