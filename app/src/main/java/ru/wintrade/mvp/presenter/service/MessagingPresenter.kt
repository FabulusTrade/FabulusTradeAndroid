package ru.wintrade.mvp.presenter.service

import ru.wintrade.ui.service.MessagingService
import ru.wintrade.util.getNotificationId

class MessagingPresenter(val service: MessagingService) {

    fun messageReceived(data: Map<String, String>) {
        val id = data.getOrElse("id") { "" }
        val trader = data.getOrElse("trader") { "" }
        val operationType = data.getOrElse("operation_type") { "" }
        val company = data.getOrElse("company") { "" }
        val companyImg = data.getOrElse("company_img") { "" }
        val ticker = data.getOrElse("ticker") { "" }
        val orderStatus = data.getOrElse("order_status") { "" }
        val price = data.getOrElse("price") { "" }
        val count = data.getOrElse("count") { "" }
        val currency = data.getOrElse("currency") { "" }
        val dateData = data.getOrElse("date") { "" }
        val profitCount = data.getOrElse("profit_count") { "" }

        val title = "$trader: $operationType"
        val body = "$ticker по цене: $price"
        service.showNotification(title, body, getNotificationId())
    }


}