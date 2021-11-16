package ru.wintrade.mvp.model.resource

interface ResourceProvider {
    fun getLoadingImages(): List<Int>
    fun getOnBoardImages(): List<Int>
    fun getStringResource(resource: Int): String
    fun getColor(resource: Int): Int
}