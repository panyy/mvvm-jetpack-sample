package com.common.arms.base.state

/**
 * desc：状态对象
 * author：panyy
 * date：2021/04/22
 */
data class State(var code: StateType, var message: String? = null, var showDialog: Boolean? = true)