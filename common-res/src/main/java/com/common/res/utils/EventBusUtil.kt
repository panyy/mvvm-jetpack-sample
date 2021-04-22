package com.common.res.utils

import androidx.lifecycle.LifecycleOwner
import com.jeremyliao.liveeventbus.LiveEventBus

fun eventBusPost(key: String, value: Any? = null) {
    LiveEventBus.get(key).post(value)
}

fun eventBusObserve(key: String, owner: LifecycleOwner, block: (it: Any?) -> Unit) {
    LiveEventBus.get(key).observe(owner, {
        block(it)
    })
}

fun eventBusObserveForever(key: String, block: (it: Any?) -> Unit) {
    LiveEventBus.get(key).observeForever {
        block(it)
    }
}

fun eventBusRemoveObserver(key: String, block: (it: Any?) -> Unit) {
    if (block != null) {
        LiveEventBus.get(key).removeObserver(block)
    }
}
