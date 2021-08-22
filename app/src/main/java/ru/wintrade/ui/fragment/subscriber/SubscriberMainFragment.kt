package ru.wintrade.ui.fragment.subscriber

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_subscriber_main.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.subscriber.SubscriberMainPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberMainView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.SubscriberMainVPAdapter
import ru.wintrade.util.IntentConstants
import ru.wintrade.util.createBitmapFromResult
import ru.wintrade.util.loadImage

class SubscriberMainFragment : MvpAppCompatFragment(), SubscriberMainView {
    companion object {
        fun newInstance() = SubscriberMainFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberMainPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberMainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subscriber_main, container, false)

    override fun init() {
        drawerSetMode()
        initViewPager()
        initPopupMenu()
    }

    private fun initPopupMenu() {
        val popupMenu =
            context?.let { androidx.appcompat.widget.PopupMenu(it, iv_subscriber_main_ava) }
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
        iv_subscriber_main_ava.setOnClickListener {
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

    override fun setAvatar(ava: String?) {
        ava?.let { loadImage(it, iv_subscriber_main_ava) }
    }

    override fun setName(username: String) {
        tv_subscriber_main_name.text = username
    }

    override fun setSubscriptionCount(count: Int) {
        val text = "Подписки $count"
        tv_subscriber_main_subscription.text = text
    }

    private fun initViewPager() {
        vp_subscriber_main.adapter = SubscriberMainVPAdapter(this)
        TabLayoutMediator(tab_layout_subscriber_main, vp_subscriber_main) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.visibility)
                1 -> tab.setIcon(R.drawable.ic_trader_deal)
                2 -> tab.setIcon(R.drawable.ic_trader_news)
            }
        }.attach()
    }

    private fun drawerSetMode() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar_blue.visibility = View.VISIBLE
    }
}