package com.common.arms.base.mvvm

import androidx.viewbinding.ViewBinding
import com.coder.zzq.smartshow.dialog.LoadingDialog
import com.coder.zzq.smartshow.snackbar.SmartSnackbar
import com.common.arms.base.BaseActivity
import com.common.arms.base.state.StateType
import com.common.arms.callback.LoadingCallBack
import com.common.arms.utils.dismissLoadingDialog
import com.common.arms.utils.showLoadingDialog
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * desc：MVVM Activity基类
 * author：panyy
 * date：2021/04/22
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() {

    lateinit var viewModel: VM
    var loadingDialog: LoadingDialog? = null

    override fun initVMData() {
        viewModel = initVM()
        stateObserve()
    }

    abstract fun initVM(): VM

    val loadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(getTargetView()) { reload() }
    }

    open fun reload() = Unit

    open fun getTargetView(): Any {
        return activity
    }

    private fun stateObserve() {
        viewModel.loadState.observe(this, { response ->
            when (response?.code) {
                StateType.SUCCESS -> {
                    loadService.showSuccess()
                }
                StateType.LOADING -> {
                    if (response.showDialog == true) {
                        var loadingMessage = if (response.message.isNullOrEmpty()) "请稍等..." else response.message
                        loadingDialog = showLoadingDialog(activity = activity, message = loadingMessage)
                    } else {
                        loadService.showCallback(LoadingCallBack::class.java)
                    }
                }
                StateType.ERROR -> {
                    dismissLoadingDialog(loadingDialog)
                    SmartSnackbar.get(activity).show(response?.message)
                }
                StateType.EMPTY -> {
                }
            }
        })
    }

    override fun onDestroy() {
        dismissLoadingDialog(loadingDialog)
        super.onDestroy()
    }

}