package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseBlacklistItem(
    @Expose
    val user_in_blacklist_id: String
)