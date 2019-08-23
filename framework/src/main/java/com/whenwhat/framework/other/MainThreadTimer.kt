package com.whenwhat.framework.other

import android.os.Handler
import android.os.Looper

private class RepeatableAction(
        private val handler: Handler,
        private val action: () -> Unit,
        private val period: Long
) : Runnable {
    private var isCanceled = false

    override fun run() {
        if (isCanceled)
            return
        action()
        handler.postDelayed(this, period)
    }

    fun cancel() {
        isCanceled = true
        handler.removeCallbacksAndMessages(null)
    }
}

fun mainThreadTimer(period: Long, action: () -> Unit): Subscription {
    val handler = Handler(Looper.getMainLooper())
    val repeatableAction = RepeatableAction(handler, action, period)
    handler.post(repeatableAction)
    return object : Subscription {
        override fun unSubscribe() {
            repeatableAction.cancel()
        }
    }
}

fun postDelayed(delay: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, delay)
}