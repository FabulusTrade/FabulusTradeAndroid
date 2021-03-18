package ru.wintrade.mvp.presenter.service

import ru.wintrade.ui.service.MessagingService

class MessagingPresenter(val service: MessagingService) {

    fun messageReceived(data: Map<String, String>) {
        service.showNotification(data.getOrElse("title"){""}, data.getOrElse("body"){""})
    }

}