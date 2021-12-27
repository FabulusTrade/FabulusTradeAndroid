package ru.fabulus.fabulustrade.mvp.model.schedulers

import io.reactivex.rxjava3.core.Scheduler

interface ISchedulers {

    fun mainThread(): Scheduler

    fun io(): Scheduler
}