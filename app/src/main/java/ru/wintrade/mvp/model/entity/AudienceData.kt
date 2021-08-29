package ru.wintrade.mvp.model.entity

import java.io.Serializable
import java.util.*

interface AudienceData : Serializable {
    val dateJoined: Date
    val followersCount: Int
    val observerCount: Int
}