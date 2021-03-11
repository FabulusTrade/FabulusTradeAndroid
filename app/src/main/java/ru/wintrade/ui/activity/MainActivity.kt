package ru.wintrade.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.wintrade.R
import ru.wintrade.mvp.presenter.MainPresenter
import ru.wintrade.mvp.view.MainView
import ru.wintrade.ui.App
import java.util.concurrent.TimeUnit
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
    }

    override fun init() {
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.bringToFront()
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
            R.id.exit_menu_id -> {
                finish()
            }
        }
        return true
    }

}