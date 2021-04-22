package com.common.res.utils

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * desc :封装一下RxJava, 更易用
 * author：panyy
 * date：2021/04/22
 */
object RxJavaUtil {
    fun <T> run(onRxAndroidListener: OnRxAndroidListener<T>) {
        Observable.create(ObservableOnSubscribe<T> { e ->
            try {
                val t: T? = onRxAndroidListener.doInBackground()
                if (t != null) {
                    e.onNext(t)
                } else {
                    e.onError(NullPointerException("on doInBackground result not null"))
                }
            } catch (throwable: Throwable) {
                e.onError(throwable)
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(object : Observer<T> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(result: T) {
                        onRxAndroidListener.onFinish(result)
                    }

                    override fun onError(e: Throwable) {
                        onRxAndroidListener.onError(e)
                    }

                    override fun onComplete() {}
                })
    }

    fun <T> runIO(onRxAndroidListener: OnRxAndroidListener<T>) {
        Observable.create(ObservableOnSubscribe<T> { e ->
            try {
                val t: T? = onRxAndroidListener.doInBackground()
                if (t != null) {
                    e.onNext(t)
                } else {
                    e.onError(NullPointerException("on doInBackground result not null"))
                }
            } catch (throwable: Throwable) {
                e.onError(throwable)
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(object : Observer<T> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(result: T) {
                        onRxAndroidListener.onFinish(result)
                    }

                    override fun onError(e: Throwable) {
                        onRxAndroidListener.onError(e)
                    }

                    override fun onComplete() {}
                })
    }

    @SuppressLint("CheckResult")
    fun loop(period: Long, listener: OnRxLoopListener): Disposable {
        return Observable.interval(period, TimeUnit.MILLISECONDS)
                .takeWhile { listener.takeWhile() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Long?>() {
                    override fun onNext(l: Long) {
                        listener.onExecute()
                    }

                    override fun onComplete() {
                        listener.onFinish()
                    }

                    override fun onError(e: Throwable) {
                        listener.onError(e)
                    }
                })
    }

    interface OnRxAndroidListener<T> {
        //在子线程执行
        @Throws(Throwable::class)
        fun doInBackground(): T

        //事件执行成功, 在主线程回调
        fun onFinish(result: T)

        //事件执行失败, 在主线程回调
        fun onError(e: Throwable?)
    }

    interface OnRxLoopListener {
        //是否循环
        @Throws(Exception::class)
        fun takeWhile(): Boolean

        //执行事件, 在主线程回调
        fun onExecute()

        //循环结束
        fun onFinish()

        //事件执行失败, 在主线程回调
        fun onError(e: Throwable?)
    }
}