package ru.fabulus.fabulustrade.mvp.view

interface NavElementsControl {
    fun setDrawerLockMode(driverLockMode: Int)
    fun toolbarVisible(visible: Boolean)
    fun setToolbarMenuVisible(visible: Boolean)
}