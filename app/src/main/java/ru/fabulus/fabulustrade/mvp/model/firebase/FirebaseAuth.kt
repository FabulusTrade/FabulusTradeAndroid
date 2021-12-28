package ru.fabulus.fabulustrade.mvp.model.firebase

import io.reactivex.rxjava3.subjects.ReplaySubject

interface FirebaseAuth {
    val codeSuccessSubject: ReplaySubject<Boolean>

    fun verifyPhone(phone: String)
    fun checkCode(code: String)
}