package ru.fabulus.fabulustrade.util

import java.util.concurrent.atomic.AtomicInteger

private val c: AtomicInteger = AtomicInteger(0)
fun getNotificationId(): Int {
    return c.incrementAndGet()
}