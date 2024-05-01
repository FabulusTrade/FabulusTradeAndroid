package ru.fabulus.fabulustrade.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.navigation.NavigationView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ActivityMainBinding
import ru.fabulus.fabulustrade.databinding.HeaderMainMenuBinding
import ru.fabulus.fabulustrade.mvp.presenter.MainPresenter
import ru.fabulus.fabulustrade.mvp.view.MainView
import ru.fabulus.fabulustrade.mvp.view.NavElementsControl
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.BackButtonListener
import ru.fabulus.fabulustrade.util.loadImage
import javax.inject.Inject


class MainActivity : MvpAppCompatActivity(), MainView,
    NavigationView.OnNavigationItemSelectedListener, NavElementsControl {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @InjectPresenter
    lateinit var presenter: MainPresenter

    val navigator = object : AppNavigator(this, R.id.container) {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            fragmentTransaction.setCustomAnimations(
                R.anim.enter_anim, R.anim.exit_anim,
                R.anim.enter_anim, R.anim.exit_anim,
            )
        }
    }

    @ProvidePresenter
    fun providePresenter() = MainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private val toolbar: Toolbar by lazy { binding.appBarMain.mainToolbar.toolbarBlue }
    private val drawerLayout: DrawerLayout by lazy { binding.drawerLayout }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        App.instance.appComponent.inject(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun init() {
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
            )
        toggle.syncState()
        binding.navView.run {
            setNavigationItemSelectedListener(this@MainActivity)
            bringToFront()
        }
        setupHeader(false, null, null, null, null, null, null)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_toolbar_blue, menu)
        appbarMenuVisible(presenter.profile.user != null)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.apply {
                    if (isDrawerOpen(GravityCompat.START)) {
                        closeDrawer(GravityCompat.START)
                    } else {
                        openDrawer(GravityCompat.START)
                        presenter.onDrawerOpened()
                    }
                }
            }

            R.id.menu_search -> {
                presenter.openSearchScreen()
            }

            R.id.menu_share -> {
                presenter.openShareScreen()
            }

            R.id.menu_win -> {
                presenter.openWinScreen()
            }
        }
        return true
    }

    override fun setupHeader(
        isTrader: Boolean,
        avatar: String?,
        username: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?
    ) {
        val headerView = binding.navView.getHeaderView(0)

        val headerBinding = HeaderMainMenuBinding.bind(headerView)

        fun setViewsVisibility(isTrader: Boolean) {
            headerBinding.btnHeaderFillProfile.visibility =
                if (isTrader) View.GONE else View.VISIBLE
            headerBinding.tvHeaderEmail.visibility = if (isTrader) View.VISIBLE else View.GONE
            headerBinding.tvHeaderFullname.visibility = if (isTrader) View.VISIBLE else View.GONE
            headerBinding.tvHeaderEmail.visibility = if (isTrader) View.VISIBLE else View.GONE
            headerBinding.tvHeaderPhone.visibility = if (isTrader) View.VISIBLE else View.GONE
        }

        if (username.isNullOrBlank()) {
            headerBinding.headerUserRegContent.visibility = View.GONE
            headerBinding.headerNoRegContent.visibility = View.VISIBLE
        } else {
            if (!isTrader) {
                setViewsVisibility(false)
                headerBinding.btnHeaderFillProfile.setOnClickListener {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    presenter.openSignUpTraderScreen()
                }
            } else {
                setViewsVisibility(true)
                headerBinding.headerUserRegContent.visibility = View.VISIBLE
                headerBinding.headerNoRegContent.visibility = View.GONE
                avatar?.let { loadImage(it, headerView.findViewById(R.id.iv_header_main_avatar)) }
                headerBinding.tvHeaderMainNickname.text = username
                headerBinding.tvHeaderFullname.text = firstName + " " + lastName
                headerBinding.tvHeaderEmail.text = email
                headerBinding.tvHeaderPhone.text = phone
                headerBinding.btnHeaderEditProfile.setOnClickListener {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    presenter.openSetUsernameScreen()
                }
            }
        }
    }

    override fun exit() {
        finish()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.traders_menu_id -> presenter.tradersMenuClicked()
            R.id.general_news_feed_id -> presenter.generalFeedClicked()
            R.id.observation_list_menu_id -> presenter.observationMenuClicked()
            R.id.invite_a_friend_menu_id -> presenter.friendInviteMenuClicked()
            R.id.about_menu_id -> presenter.aboutWTMenuClicked()
            R.id.ask_menu_id -> presenter.questionMenuClicked()
            R.id.community_menu_id -> presenter.settingsMenuClicked()
            R.id.exit_menu_id -> presenter.exitClicked()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated(message = "deprecation inhereted")
    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backClicked())
                return
        }
        presenter.backClicked()
    }

    override fun setDrawerLockMode(driverLockMode: Int) {
        drawerLayout.setDrawerLockMode(driverLockMode)
    }

    override fun toolbarVisible(visible: Boolean) {
        if (visible) {
            toolbar.visibility = View.VISIBLE
        } else {
            toolbar.visibility = View.GONE
        }
    }

    override fun setToolbarMenuVisible(visible: Boolean) {
        appbarMenuVisible(visible)
    }

    private fun appbarMenuVisible(visible: Boolean) {
//        toolbar.menu.findItem(R.id.menu_search).isVisible = visible
//        toolbar.menu.findItem(R.id.menu_share).isVisible = visible
//        toolbar.menu.findItem(R.id.menu_win).isVisible = visible
        // TODO скрываем меню, до момента пока не реализуем обработку событий
        toolbar.menu.findItem(R.id.menu_search).isVisible = false
        toolbar.menu.findItem(R.id.menu_share).isVisible = false
        toolbar.menu.findItem(R.id.menu_win).isVisible = false
    }
}