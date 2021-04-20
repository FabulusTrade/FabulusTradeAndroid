package ru.wintrade.ui.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.layout_title.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.BuildConfig
import ru.wintrade.R
import ru.wintrade.mvp.presenter.SignInPresenter
import ru.wintrade.mvp.view.SignInView
import ru.wintrade.ui.App

class SignInFragment : MvpAppCompatFragment(), SignInView {
    companion object {
        fun newInstance() = SignInFragment()
        const val RC_SIGN_IN = 0
        private const val TOKEN_ID = BuildConfig.TOKEN_ID
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @InjectPresenter
    lateinit var presenter: SignInPresenter

    @ProvidePresenter
    fun providePresenter() = SignInPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun init() {
        setDrawerLockMode()
        iv_close.setOnClickListener { requireActivity().finish() }
        entrance_registration_button.setOnClickListener { presenter.openRegistrationScreen() }
        entrance_enter_button.setOnClickListener { enterBtnClicked() }
        signInWithGoogle()
        entrance_enter_with_facebook_button.setOnClickListener {
            signOut()
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(TOKEN_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        entrance_enter_with_google_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun enterBtnClicked() {
        presenter.loginBtnClicked(
            et_sign_in_nickname.text.toString(),
            et_sign_in_password.text.toString()
        )
        (requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
    }

    private fun setDrawerLockMode() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        requireActivity().toolbar_blue.visibility = View.GONE
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        val account = completedTask.getResult(ApiException::class.java)
        val idToken = account?.idToken
        idToken?.let { presenter.post(idToken) }
    }

    override fun onStart() {
        super.onStart()
//        mGoogleSignInClient.silentSignIn()
//            .addOnCompleteListener(requireActivity()) { task ->
//                handleSignInResult(task)
//            }
    }

    override fun showToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }
}