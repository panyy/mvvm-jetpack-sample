package com.common.arms.base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.common.arms.base.state.State

/**
 * desc：ViewModel基类
 * author：panyy
 * date：2021/04/22
 */
open class BaseViewModel : ViewModel() {

    val loadState by lazy {
        MutableLiveData<State>()
    }

}
