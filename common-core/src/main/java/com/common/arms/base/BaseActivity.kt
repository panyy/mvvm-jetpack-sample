package com.common.arms.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.common.arms.base.delegate.IActivity
import com.common.arms.integration.cache.Cache
import com.common.arms.integration.cache.CacheType
import com.common.arms.integration.lifecycle.ActivityLifecycleable
import com.common.arms.utils.ArmsUtil
import com.dylanc.viewbinding.base.inflateBindingWithGeneric
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IActivity, ActivityLifecycleable {

    protected val TAG = this.javaClass.simpleName
    private var mCache: Cache<*, *>? = null
    protected lateinit var activity: AppCompatActivity
    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    lateinit var vBinding: VB

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = ArmsUtil.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE)
        }
        return mCache as Cache<String, Any>
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent> {
        return mLifecycleSubject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        vBinding = inflateBindingWithGeneric(layoutInflater)
        setContentView(vBinding.root)
        initVMData()
        eventObserve()
        bindViewClick()
        initData(savedInstanceState)
    }

    open fun initVMData() {}
    open fun eventObserve() {}
    open fun bindViewClick() {}

    /**
     * 这个 [Activity] 是否会使用 [Fragment], 框架会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回 `false`, 那意味着这个 [Activity] 不需要绑定 [Fragment], 那你再在这个 [Activity] 中绑定继承于 BaseFragment 的 [Fragment] 将不起任何作用
     *
     * @return 返回 `true` (默认为 `true`), 则需要使用 [Fragment]
     */
    override fun useFragment(): Boolean {
        return true
    }
}