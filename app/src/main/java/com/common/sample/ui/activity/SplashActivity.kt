package com.common.sample.ui.activity

import android.os.Bundle
import com.common.arms.base.BaseActivity
import com.common.res.immersionbar.BindFullScreen
import com.common.res.router.RouterHub
import com.common.res.router.routerNavigation
import com.common.res.utils.checkLogin
import com.common.sample.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(), BindFullScreen {

    override fun initData(savedInstanceState: Bundle?) {
        checkLogin {
            if (it) {
                routerNavigation(RouterHub.PUBLIC_MAIN)
            } else {
                routerNavigation(RouterHub.PUBLIC_LOGIN)
            }
            finish()
        }
    }

}