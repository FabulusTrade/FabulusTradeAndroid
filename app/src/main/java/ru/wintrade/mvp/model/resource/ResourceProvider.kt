package ru.wintrade.mvp.model.resource

interface ResourceProvider {
    fun getLoadingImages(): List<Int>
    fun getOnBoardImages(): List<Int>
}