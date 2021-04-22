package com.common.res.app

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

class FragmentLifecycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Timber.i("%s - onFragmentAttached", f.toString())
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("%s - onFragmentCreated", f.toString())
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Timber.i("%s - onFragmentViewCreated", f.toString())
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("%s - onFragmentActivityCreated", f.toString())
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentStarted", f.toString())
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentResumed", f.toString())
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentPaused", f.toString())
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentStopped", f.toString())
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Timber.i("%s - onFragmentSaveInstanceState", f.toString())
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentViewDestroyed", f.toString())
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentDestroyed", f.toString())
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Timber.i("%s - onFragmentDetached", f.toString())
    }
}