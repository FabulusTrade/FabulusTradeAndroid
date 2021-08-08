package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import java.util.*

data class ResponseAggregatedTrade(
    @Expose
    val id_company: Int,
    @Expose
    val company: String,
    @Expose
    val count: Int,
    @Expose
    val company_img: String,
    @Expose
    val date_last: String
)