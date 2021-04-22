package com.common.arms.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.coder.zzq.smartshow.dialog.LoadingDialog
import com.common.arms.base.mvvm.BaseVMActivity
import com.common.arms.base.mvvm.BaseVMFragment
import com.common.arms.base.state.State
import com.common.arms.base.state.StateType

fun showLoadingDialog(activity: Activity? = null, fragment: Fragment? = null, message: String? = ""): LoadingDialog? {
    return LoadingDialog().middle().message(message).apply {
        if (activity != null && !activity.isFinishing) {
            showInActivity(activity)
        }
        if (fragment != null) {
            showInFragment(fragment)
        }
    }
}

fun dismissLoadingDialog(dialog: LoadingDialog?) {
    dialog?.dismiss()
}

/**
 * 显示加载框状态
 */
fun showLoadingState(loadState: MutableLiveData<State>, message: String? = "", showDialog: Boolean? = true) {
    loadState.postValue(State(StateType.LOADING, message, showDialog))
}

/**
 * 隐藏加载框状态
 */
fun hideLoadingState(activity: Activity? = null, fragment: Fragment? = null) {
    if (activity != null && activity is BaseVMActivity<*, *>) {
        activity.loadService.showSuccess()
        dismissLoadingDialog(activity.loadingDialog)
    }
    if (fragment != null && fragment is BaseVMFragment<*, *>) {
        fragment.loadService.showSuccess()
        dismissLoadingDialog(fragment.loadingDialog)
    }
}



