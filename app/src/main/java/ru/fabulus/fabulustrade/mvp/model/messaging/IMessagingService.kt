package ru.fabulus.fabulustrade.mvp.model.messaging

import android.text.Spanned

interface IMessagingService {
    fun showNotification(title: Spanned, body: String, id: Int)
}