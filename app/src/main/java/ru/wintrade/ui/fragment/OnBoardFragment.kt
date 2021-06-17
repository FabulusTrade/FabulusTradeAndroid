package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_on_board.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.OnBoardPresenter
import ru.wintrade.mvp.view.OnBoardView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.OnBoardVPAdapter

class OnBoardFragment : MvpAppCompatFragment(), OnBoardView {

    companion object {
        fun newInstance() = OnBoardFragment()
    }

    private var adapter: OnBoardVPAdapter? = null

    @InjectPresenter
    lateinit var presenter: OnBoardPresenter

    @ProvidePresenter
    fun providePresenter() = OnBoardPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_on_board, container, false)

    override fun init() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        requireActivity().toolbar_blue.visibility = View.GONE
        adapter = OnBoardVPAdapter(presenter.listPresenter)
        vp_on_board.adapter = adapter
    }

    override fun setVPPos(pos: Int) {
        vp_on_board.setCurrentItem(pos, true)
    }
}