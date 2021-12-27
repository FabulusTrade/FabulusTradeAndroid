package ru.fabulus.fabulustrade.ui.fragment.entrance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentOnBoardBinding
import ru.fabulus.fabulustrade.mvp.presenter.entrance.OnBoardPresenter
import ru.fabulus.fabulustrade.mvp.view.NavElementsControl
import ru.fabulus.fabulustrade.mvp.view.entrance.OnBoardView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.OnBoardVPAdapter

class OnBoardFragment : MvpAppCompatFragment(), OnBoardView {
    private var _binding: FragmentOnBoardBinding? = null
    private val binding: FragmentOnBoardBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = OnBoardFragment()
    }

    private var onBoardVPAdapter: OnBoardVPAdapter? = null

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
    ): View? {
        _binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        val navElementsControl = requireActivity() as? NavElementsControl
        navElementsControl?.let {
            it.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            it.toolbarVisible(false)
        }
        onBoardVPAdapter = OnBoardVPAdapter(presenter.listPresenter)
        binding.vpOnBoard.adapter = onBoardVPAdapter
    }

    override fun setVPPos(pos: Int) {
        binding.vpOnBoard.setCurrentItem(pos, true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}