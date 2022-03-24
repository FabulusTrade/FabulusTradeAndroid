package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseCompanyTradingOperations(
    @Expose
    val id: Long,
    @Expose
    val operation_type: String,
    @Expose
    val company: String,
    @Expose
    val company_img: String,
    @Expose
    val date: String,
    @Expose
    val profit_count: Double?,
    @Expose
    val price: Float,
    @Expose
    val currency: String
)