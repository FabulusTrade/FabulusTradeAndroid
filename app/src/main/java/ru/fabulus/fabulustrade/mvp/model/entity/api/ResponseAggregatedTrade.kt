package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

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