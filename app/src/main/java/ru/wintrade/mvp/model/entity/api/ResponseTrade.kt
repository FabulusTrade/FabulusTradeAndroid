package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseTrade(
    @Expose
    val id: Long,
    @Expose
    val trader: String,
    @Expose
    val operation_type: String,
    @Expose
    val company: String,
    @Expose
    val company_img: String,
    @Expose
    val ticker: String,
    @Expose
    val order_status: String,
    @Expose
    val order_num: String,
    @Expose
    val price: Float,
    @Expose
    val count: Int,
    @Expose
    val currency: String,
    @Expose
    val date: String,
    @Expose
    val subtype: String,
    @Expose
    val profit_count: String,
    @Expose
    val value: String
)