package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultAddToBlacklist(
    val result: String
) : Parcelable