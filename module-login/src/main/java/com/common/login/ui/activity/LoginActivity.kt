package com.common.login.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.coder.zzq.smartshow.dialog.LoadingDialog
import com.common.arms.base.mvvm.BaseVMActivity
import com.common.arms.utils.dismissLoadingDialog
import com.common.login.databinding.ActivityLoginBinding
import com.common.login.vm.LoginViewModel
import com.common.res.router.RouterHub
import com.common.res.router.routerNavigation
import com.common.res.utils.bindViewClickListener
import org.koin.androidx.viewmodel.ext.android.getViewModel

@Route(path = RouterHub.PUBLIC_LOGIN)
class LoginActivity : BaseVMActivity<ActivityLoginBinding, LoginViewModel>() {

    private var dialog: LoadingDialog? = null

    override fun initVM(): LoginViewModel = getViewModel()

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun bindViewClick() {
        vBinding.apply {
            bindViewClickListener(btnLogin) {
                when (this) {
                    btnLogin -> {
                        userLogin()
                    }
                }
            }
        }
    }

    private fun userLogin() {
        vBinding.apply {
            var name = etMobile.text.toString()
            var password = etPassword.text.toString()
            viewModel.userLogin(name, password).observe(activity) {
                dismissLoadingDialog(dialog)
                routerNavigation(RouterHub.PUBLIC_MAIN)
                activity.finish()
            }
        }
    }

}
