package ru.wintrade.ui.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityLifecycleCallback(val holder: ActivityHolder): Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        if (p0 is MainActivity)
            holder.activity = p0
    }

    override fun onActivityStarted(p0: Activity) {}

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {
        if (p0 is MainActivity)
            holder.activity = null
    }
}