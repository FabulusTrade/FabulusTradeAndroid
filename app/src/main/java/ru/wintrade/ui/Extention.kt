package ru.wintrade.ui

import moxy.MvpAppCompatFragment
import ru.wintrade.mvp.view.NavElementsControl

fun MvpAppCompatFragment.setDrawerLockMode(lockMode: Int) {
    val navElementsControl = this.requireActivity() as? NavElementsControl
    navElementsControl?.let {
        it.setDrawerLockMode(lockMode)
    }
}

fun MvpAppCompatFragment.setToolbarVisible(visible: Boolean = true) {
    val navElementsControl = this.requireActivity() as? NavElementsControl
    navElementsControl?.let {
        it.toolbarVisible(visible)
    }
}