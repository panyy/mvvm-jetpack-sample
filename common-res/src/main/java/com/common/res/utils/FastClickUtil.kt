package com.common.res.utils

object FastClickUtil {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private const val MIN_CLICK_DELAY_TIME = 500
    private var lastClickTime: Long = 0
    val isFastClick: Boolean
        get() {
            var flag = false
            val curClickTime = System.currentTimeMillis()
            if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                flag = true
            }
            lastClickTime = curClickTime
            return flag
        }
}