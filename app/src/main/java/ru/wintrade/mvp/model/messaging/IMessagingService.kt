package ru.wintrade.mvp.model.messaging

interface IMessagingService {
    fun showNotification(title: String, body: String)
}