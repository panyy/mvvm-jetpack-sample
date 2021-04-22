package com.common.arms.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.common.arms.base.delegate.IFragment
import com.common.arms.integration.cache.Cache
import com.common.arms.integration.cache.CacheType
import com.common.arms.integration.lifecycle.FragmentLifecycleable
import com.common.arms.utils.ArmsUtil
import com.dylanc.viewbinding.base.inflateBindingWithGeneric
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @[Fragment] 的三方库, 那你就需要自己自定义 @[Fragment]
 * 继承于这个特定的 @[Fragment], 然后再按照 [BaseFragment] 的格式, 将代码复制过去, 记住一定要实现[IFragment]
 * ================================================
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), IFragment, FragmentLifecycleable {

    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    lateinit var activity: AppCompatActivity
    private var mCache: Cache<*, *>? = null

    private var _binding: VB? = null
    val vBinding: VB get() = _binding!!

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = ArmsUtil.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE)
        }
        return mCache as Cache<String, Any>
    }

    override fun provideLifecycleSubject(): Subject<FragmentEvent> {
        return mLifecycleSubject
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity = getActivity() as AppCompatActivity
        _binding = inflateBindingWithGeneric(layoutInflater)
        return vBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initVMData()
        eventObserve()
        bindViewClick()
    }

    open fun initVMData() {}
    open fun eventObserve() {}
    open fun bindViewClick() {}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}