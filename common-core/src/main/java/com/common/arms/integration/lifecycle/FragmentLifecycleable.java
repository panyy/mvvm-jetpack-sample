package com.common.arms.integration.lifecycle;

import androidx.fragment.app.Fragment;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * ================================================
 * 让 {@link Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 * ================================================
 */
public interface FragmentLifecycleable extends Lifecycleable<FragmentEvent> {
}
