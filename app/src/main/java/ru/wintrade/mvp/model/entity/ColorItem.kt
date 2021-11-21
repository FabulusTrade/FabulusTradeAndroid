package ru.wintrade.mvp.model.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorItem(
    @Expose
    val value: Double?,
    @Expose
    val color: String?
) : Parcelable