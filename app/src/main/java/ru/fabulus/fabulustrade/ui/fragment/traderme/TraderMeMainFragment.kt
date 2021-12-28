package ru.fabulus.fabulustrade.ui.fragment.traderme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTraderMeMainBinding
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeMainPresenter
import ru.fabulus.fabulustrade.mvp.view.traderme.TraderMeMainView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.BackButtonListener
import ru.fabulus.fabulustrade.ui.adapter.TraderMeMainAdapter
import ru.fabulus.fabulustrade.util.*

class TraderMeMainFragment : MvpAppCompatFragment(), TraderMeMainView, BackButtonListener {
    private var _binding: FragmentTraderMeMainBinding? = null
    private val binding: FragmentTraderMeMainBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TraderMeMainFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeMainPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeMainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderMeMainBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initView()
        initPopupMenu()
    }

    private fun initPopupMenu() {
        val popupMenu =
            androidx.appcompat.widget.PopupMenu(requireContext(), binding.ivTraderMeMainAva)
        popupMenu.inflate(R.menu.menu_avatar)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.change_avatar -> {
                    loadFileFromDevice()
                    true
                }
                R.id.delete_avatar -> {
                    presenter.deleteAvatar()
                    true
                }
                else -> false
            }
        }
        binding.ivTraderMeMainAva.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun loadFileFromDevice() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK).apply {
                this.type = getString(R.string.gallery_mask_traderFragment)
            }
        val intentChooser = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, galleryIntent)
            this.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.gallery_traderFragment))
        }
        startActivityForResult(intentChooser, IntentConstants.PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentConstants.PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.createBitmapFromResult(requireActivity())
            presenter.setImage(imageBitmap!!)
        }
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun setAvatar(url: String?) {
        url?.let { loadImage(it, binding.ivTraderMeMainAva) }
    }

    override fun initVP(traderStatistic: TraderStatistic) {
        binding.vpTraderMeMain.adapter = TraderMeMainAdapter(this, traderStatistic)
        TabLayoutMediator(
            binding.tabLayoutTraderMainMe,
            binding.vpTraderMeMain
        ) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_trader_profit)
                1 -> tab.setIcon(R.drawable.ic_trader_news)
                // TODO скрываем меню, до момента пока не реализуем обработку событий
//                2 -> tab.setIcon(R.drawable.ic_trader_instrument)
                2 -> tab.setIcon(R.drawable.ic_trader_deal)
                3 -> tab.setIcon(R.drawable.visibility)
            }
        }.attach()
    }

    override fun setProfit(profit: String, textColor: Int) {
        binding.tvTraderMeMainProfit.setTextAndColor(profit, textColor)
    }

    override fun setUsername(username: String) {
        binding.tvTraderMeMainName.text = username
    }

    override fun setSubscriberCount(count: Int) {
        val subscriberCountText = "Подписки $count"
        binding.tvTraderMeMainSubscriberCount.text = subscriberCountText
    }

    override fun backClicked(): Boolean {
        presenter.backClicked()
        return true
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}