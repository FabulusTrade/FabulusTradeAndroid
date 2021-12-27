package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseOperationsCount(
    @Expose
    @SerializedName("number_of_operations") val numberOfOperations: Int
)