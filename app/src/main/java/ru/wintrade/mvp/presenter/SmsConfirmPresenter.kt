package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.firebase.FirebaseAuth
import ru.wintrade.mvp.view.SmsConfirmView
import javax.inject.Inject

class SmsConfirmPresenter(val phone: String): MvpPresenter<SmsConfirmView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var auth: FirebaseAuth

    private var code = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        auth.codeSuccessSubject.subscribe(getCodeSuccessObserver())
        auth.verifyPhone(phone)
    }

    fun confirmClicked() {
        auth.checkCode(code)
    }

    fun resendClicked() {
        auth.verifyPhone(phone)
    }

    fun codeChanged(code: String) {
        this.code = code
    }

    private fun getCodeSuccessObserver() = object : Observer<Boolean> {
        override fun onSubscribe(d: Disposable?) {}

        override fun onNext(t: Boolean) {
            if (t)
                viewState.showToast("Ок")
            else
                viewState.showToast("Ошибка")
        }

        override fun onError(e: Throwable?) {}

        override fun onComplete() {}

    }
}