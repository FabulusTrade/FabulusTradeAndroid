package ru.wintrade.ui.fragment.traderme

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_trader_me_main.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeMainPresenter
import ru.wintrade.mvp.view.traderme.TraderMeMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.ui.adapter.TraderMeMainAdapter
import ru.wintrade.ui.setDrawerLockMode
import ru.wintrade.ui.setToolbarVisible
import ru.wintrade.util.IntentConstants
import ru.wintrade.util.createBitmapFromResult
import ru.wintrade.util.loadImage

class TraderMeMainFragment : MvpAppCompatFragment(), TraderMeMainView, BackButtonListener {

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
    ): View? = inflater.inflate(R.layout.fragment_trader_me_main, container, false)

    override fun init() {
        initView()
        initViewPager()
        initPopupMenu()
    }

    private fun initPopupMenu() {
        val popupMenu =
            context?.let { androidx.appcompat.widget.PopupMenu(it, iv_trader_me_main_ava) }
        popupMenu?.inflate(R.menu.menu_avatar)
        popupMenu?.setOnMenuItemClickListener {
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
        iv_trader_me_main_ava.setOnClickListener {
            popupMenu?.show()
        }
    }

    private fun loadFileFromDevice() {
        val galleryIntent = Intent(Intent.ACTION_PICK).apply { this.type = "image/*" }
        val intentChooser = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, galleryIntent)
            this.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.gallery))
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

    private fun initViewPager() {
        vp_trader_me_main.adapter = TraderMeMainAdapter(this)
        TabLayoutMediator(
            tab_layout_trader_main_me,
            vp_trader_me_main
        ) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_trader_profit)
                1 -> tab.setIcon(R.drawable.ic_trader_news)
                2 -> tab.setIcon(R.drawable.ic_trader_instrument)
                3 -> tab.setIcon(R.drawable.ic_trader_deal)
                4 -> tab.setIcon(R.drawable.ic_visibility)
            }
        }.attach()
    }

    override fun setAvatar(url: String?) {
        url?.let { loadImage(it, iv_trader_me_main_ava) }
    }

    override fun setProfit(profit: String, isPositive: Boolean) {
        tv_trader_me_main_profit.text = profit
        if (isPositive)
            tv_trader_me_main_profit.setTextColor(Color.GREEN)
        else
            tv_trader_me_main_profit.setTextColor(Color.RED)
    }

    override fun setUsername(username: String) {
        tv_trader_me_main_name.text = username
    }

    override fun setSubscriberCount(count: Int) {
        val subscriberCountText = "Подписки $count"
        tv_trader_me_main_subscriber_count.text = subscriberCountText
    }

    override fun backClicked(): Boolean {
        presenter.backClicked()
        return true
    }
}