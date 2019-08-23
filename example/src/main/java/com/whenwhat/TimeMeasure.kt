package com.whenwhat

import android.util.Log

object TimeMeasure {

    private class TimeWrapper(var time: Long?)

    private val map = HashMap<String, TimeWrapper>()

    fun start(name: String) {
        val wrapper = TimeWrapper(null)
        map[name] = wrapper
        wrapper.time = System.nanoTime()
    }

    fun stop(name: String) {
        val time = System.nanoTime()
        val startTime = map[name]?.time ?: return
        Log.d("TimeMeasure", "$name - ${time - startTime}")
    }
}