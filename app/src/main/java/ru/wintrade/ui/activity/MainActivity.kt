package ru.wintrade.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_main_menu.view.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.wintrade.R
import ru.wintrade.mvp.presenter.MainPresenter
import ru.wintrade.mvp.view.MainView
import ru.wintrade.ui.App
import ru.wintrade.ui.BackButtonListener
import ru.wintrade.util.loadImage
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView,
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @InjectPresenter
    lateinit var presenter: MainPresenter

    val navigator = SupportAppNavigator(this, R.id.container)

    @ProvidePresenter
    fun providePresenter() = MainPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.instance.appComponent.inject(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun init() {
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar_blue,
                R.string.open,
                R.string.close
            )
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.bringToFront()
        setSupportActionBar(toolbar_blue)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.toolbar_blue_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    drawer_layout.openDrawer(GravityCompat.START)
                    presenter.onDrawerOpened()
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

    override fun setupHeader(avatar: String?, username: String?) {
        val headerView = nav_view.getHeaderView(0)
        avatar?.let {
            loadImage(it, headerView.iv_header_main_avatar)
        }
        headerView.tv_header_main_nickname.text = username
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
            R.id.observation_list_menu_id -> presenter.observationMenuClicked()
            R.id.invite_a_friend_menu_id -> presenter.friendInviteMenuClicked()
            R.id.about_menu_id -> presenter.aboutWTMenuClicked()
            R.id.ask_menu_id -> presenter.questionMenuClicked()
            R.id.community_menu_id -> presenter.settingsMenuClicked()
            R.id.exit_menu_id -> presenter.exitClicked()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backClicked())
                return
        }
        presenter.backClicked()
    }
}