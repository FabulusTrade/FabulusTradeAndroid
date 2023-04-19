package ru.fabulus.fabulustrade.ui.fragment.subscriber

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
import ru.fabulus.fabulustrade.databinding.FragmentSubscriberMainBinding
import ru.fabulus.fabulustrade.mvp.presenter.subscriber.SubscriberMainPresenter
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberMainView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.SubscriberMainVPAdapter
import ru.fabulus.fabulustrade.util.IntentConstants
import ru.fabulus.fabulustrade.util.createBitmapFromResult
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setDrawerLockMode
import ru.fabulus.fabulustrade.util.setToolbarVisible

class SubscriberMainFragment : MvpAppCompatFragment(), SubscriberMainView {
    private var _binding: FragmentSubscriberMainBinding? = null
    private val binding: FragmentSubscriberMainBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

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
    ): View? {
        _binding = FragmentSubscriberMainBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        drawerSetMode()
        initViewPager()
        initPopupMenu()
    }

    private fun initPopupMenu() {
        val popupMenu =
            androidx.appcompat.widget.PopupMenu(requireContext(), binding.ivSubscriberMainAva)
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
        binding.ivSubscriberMainAva.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun loadFileFromDevice() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK).apply {
                this.type = getString(R.string.gallery_mask_subsFragment)
            }
        val intentChooser = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, galleryIntent)
            this.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.gallery_subsFragment))
        }
        startActivityForResult(intentChooser, IntentConstants.PICK_IMAGE)
    }

    @Deprecated(message="deprecation inhereted")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentConstants.PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.createBitmapFromResult(requireActivity())
            presenter.setImage(imageBitmap!!)
        }
    }

    override fun setAvatar(ava: String?) {
        ava?.let { loadImage(it, binding.ivSubscriberMainAva) }
    }

    override fun setName(username: String) {
        binding.tvSubscriberMainName.text = username
    }

    private fun initViewPager() {
        with(binding) {
            vpSubscriberMain.adapter = SubscriberMainVPAdapter(this@SubscriberMainFragment)
            TabLayoutMediator(tabLayoutSubscriberMain, vpSubscriberMain) { tab, pos ->
                when (pos) {
                    0 -> tab.setIcon(R.drawable.visibility)
                    1 -> tab.setIcon(R.drawable.ic_trader_deal)
                    2 -> tab.setIcon(R.drawable.ic_trader_news)
                }
            }.attach()
        }
    }

    private fun drawerSetMode() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}