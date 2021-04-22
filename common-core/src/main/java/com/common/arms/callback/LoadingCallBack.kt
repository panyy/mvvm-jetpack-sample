package com.common.arms.callback

import android.content.Context
import android.view.View
import com.common.arms.R
import com.kingja.loadsir.callback.Callback

class LoadingCallBack : Callback() {

    override fun onCreateView(): Int = R.layout.base_layout_loading

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
    
}