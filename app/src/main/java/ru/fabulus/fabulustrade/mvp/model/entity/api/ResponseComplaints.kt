package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseComplaints(
    @Expose
    @SerializedName("results")
    val results: List<ResponseComplaint>
)