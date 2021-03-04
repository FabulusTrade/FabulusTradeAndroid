package ru.wintrade.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.wintrade.R
import ru.wintrade.mvp.presenter.MainPresenter
import ru.wintrade.mvp.view.MainView
import ru.wintrade.ui.App
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
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.bringToFront()
    }

    override fun init() {

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
            R.id.traders_menu_id -> Toast.makeText(
                this,
                "traders_menu is checked",
                Toast.LENGTH_SHORT
            ).show()
            R.id.observation_list_menu_id -> Toast.makeText(
                this,
                "observation_menu is checked",
                Toast.LENGTH_SHORT
            ).show()
            R.id.invite_a_friend_menu_id -> Toast.makeText(
                this,
                "invite_menu is checked",
                Toast.LENGTH_SHORT
            ).show()
            R.id.about_menu_id -> Toast.makeText(this, "about_menu is checked", Toast.LENGTH_SHORT)
                .show()
            R.id.ask_menu_id -> Toast.makeText(this, "ask_menu is checked", Toast.LENGTH_SHORT)
                .show()
            R.id.settings_menu_id -> Toast.makeText(
                this,
                "settings_menu is checked",
                Toast.LENGTH_SHORT
            ).show()
            R.id.exit_menu_id -> Toast.makeText(this, "exit_menu is checked", Toast.LENGTH_SHORT)
                .show()
        }
        return true
    }
}