package ru.fabulus.fabulustrade.mvp.model.messaging

import android.text.Spanned

interface IMessagingService {
    fun showNotificationFinancialOperation(
        title: Spanned,
        operationResultTitle: Spanned,
        body: String,
        id: Int
    )

    fun showNotificationNewPost(title: String, message: String, idPost: Int, idNotification: Int)
    fun showNotificationNewComment(
        title: String,
        message: String,
        idPost: Int,
        idComment: Int,
        idNotification: Int
    )

    fun showNotificationNewAnswer(
        title: String,
        message: String,
        idPost: Int,
        idComment: Int,
        idNotification: Int
    )
}