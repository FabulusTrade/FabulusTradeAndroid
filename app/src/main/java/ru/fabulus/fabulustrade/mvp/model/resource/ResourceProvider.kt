package ru.fabulus.fabulustrade.mvp.model.resource

interface ResourceProvider {
    fun getLoadingImages(): List<Int>
    fun getOnBoardImages(): List<Int>
    fun getStringResource(resource: Int): String
    fun getQuantityString(resource: Int, quantity: Int, vararg args: Any?): String
    fun getColor(resource: Int): Int
}