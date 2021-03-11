package ru.wintrade.ui.firebase

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.wintrade.ui.activity.ActivityHolder
import java.util.concurrent.TimeUnit

class AndroidFirebaseAuth(val holder: ActivityHolder) :
    ru.wintrade.mvp.model.firebase.FirebaseAuth {
    private var token: PhoneAuthProvider.ForceResendingToken? = null
    private var verificationId: String? = null
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance().apply {
            this.languageCode = "ru"
        }
    }
    override val codeSuccessSubject = ReplaySubject.create<Boolean>()

    override fun verifyPhone(phone: String) {
        holder.activity?.let { activity ->
            var options: PhoneAuthOptions? = null
            token?.let {
                options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(getSmsCodeCallback())
                    .setForceResendingToken(it)
                    .build()
            } ?: let {
                options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(getSmsCodeCallback())
                    .build()
            }

            PhoneAuthProvider.verifyPhoneNumber(options!!)
        }
    }

    override fun checkCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful)
                codeSuccessSubject.onNext(true)
            else {
                codeSuccessSubject.onNext(false)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
            }
        }
    }


    private fun getSmsCodeCallback() =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                codeSuccessSubject.onNext(true)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                codeSuccessSubject.onNext(false)
                if (e is FirebaseAuthInvalidCredentialsException) {

                } else if (e is FirebaseTooManyRequestsException) {

                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@AndroidFirebaseAuth.verificationId = verificationId
                this@AndroidFirebaseAuth.token = token
            }
        }
}