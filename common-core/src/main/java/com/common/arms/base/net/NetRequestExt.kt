package com.common.arms.base.net

import androidx.lifecycle.MutableLiveData
import com.common.arms.base.state.State
import com.common.arms.base.state.StateType

/**
 * 发起网络请求
 */
suspend fun <T> apiCall(call: suspend () -> BaseResponse<T>): BaseResponse<T> {
    return try {
        var response = call()
        if (response.errorCode == 0) {
            response.state = State(StateType.SUCCESS)
        } else {
            var message: String = response.errorMsg ?: "未知错误"
            response.state = State(StateType.ERROR, message)
        }
        response
    } catch (e: Exception) {
        e.printStackTrace()
        BaseResponse(state = State(StateType.ERROR, "网络出现问题啦"))
    }
}

/**
 * 发起网络请求并POST请求状态
 */
suspend fun <T> apiCallWithState(call: suspend () -> BaseResponse<T>, loadState: MutableLiveData<State>? = null): T? {
    var result = apiCall(call)
    if (result.state?.code == StateType.SUCCESS) {
        return result.data
    } else {
        loadState?.postValue(result.state)
    }
    return null
}

/**
 * 发起上传请求
 */
suspend fun <T> apiCallUpload(call: suspend () -> T, loadState: MutableLiveData<State>? = null): T? {
    var result = try {
        var response: BaseResponse<T> = BaseResponse(state = State(StateType.SUCCESS))
        var result = call()
        response.data = result
        response
    } catch (e: Exception) {
        e.printStackTrace()
        BaseResponse(state = State(StateType.ERROR, "网络出现问题啦"))
    }
    if (result.state?.code == StateType.SUCCESS) {
        return result.data
    } else {
        loadState?.postValue(result.state)
    }
    return null
}

/**
 * 返回结果包装类
 */
class BaseResponse<T>(
        var errorCode: Int? = -1,
        var errorMsg: String? = null,
        var data: T? = null,
        var state: State? = null,
        var isRefresh: Boolean = false,
)