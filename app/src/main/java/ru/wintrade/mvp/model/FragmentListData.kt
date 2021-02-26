package ru.wintrade.mvp.model

import androidx.fragment.app.Fragment
import ru.wintrade.ui.fragment.OnBoardFragment
import ru.wintrade.ui.fragment.SplashFragment

class FragmentListData {
    fun getFragmentsList(): ArrayList<Fragment> {
        return arrayListOf(SplashFragment.newInstance(), OnBoardFragment.newInstance())
    }
}