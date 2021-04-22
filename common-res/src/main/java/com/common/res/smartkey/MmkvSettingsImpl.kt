package com.common.res.smartkey

import android.content.SharedPreferences
import com.russhwolf.settings.ExperimentalListener
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SettingsListener
import com.tencent.mmkv.MMKV

@OptIn(ExperimentalListener::class)
class MmkvSettingsImpl(name: String) : ObservableSettings {

    private val delegate = MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE)

    override fun remove(key: String): Unit =
        delegate?.removeValueForKey(key)!!

    override fun hasKey(key: String): Boolean =
        delegate?.containsKey(key)!!

    override fun putInt(key: String, value: Int) {
        delegate?.putInt(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        delegate?.getInt(key, defaultValue)!!

    override fun putLong(key: String, value: Long) {
        delegate?.putLong(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        delegate?.getLong(key, defaultValue)!!

    override fun putString(key: String, value: String) {
        delegate?.putString(key, value)
    }

    override fun getString(key: String, defaultValue: String): String =
        delegate?.getString(key, defaultValue) ?: defaultValue

    override fun putFloat(key: String, value: Float) {
        delegate?.putFloat(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float =
        delegate?.getFloat(key, defaultValue)!!

    override fun putDouble(key: String, value: Double) {
        delegate?.putLong(key, value.toRawBits())
    }

    override fun getDouble(key: String, defaultValue: Double): Double =
        Double.fromBits(delegate?.getLong(key, defaultValue.toRawBits())!!)

    override fun putBoolean(key: String, value: Boolean) {
        delegate?.putBoolean(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        delegate?.getBoolean(key, defaultValue)!!

    override fun clear() {
        delegate?.apply {
            for (key in delegate?.allKeys()!!) {
                removeValueForKey(key)
            }
        }
    }

    @ExperimentalListener
    override fun addListener(key: String, callback: () -> Unit): SettingsListener {
        val cache = Listener.Cache(delegate?.all?.get(key))
        val prefsListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, updatedKey: String ->
                if (updatedKey != key) return@OnSharedPreferenceChangeListener
                val prev = cache.value
                val current = delegate?.all?.get(key)
                if (prev != current) {
                    callback()
                    cache.value = current
                }
            }
        delegate?.registerOnSharedPreferenceChangeListener(prefsListener)
        return Listener(prefsListener)
    }

    @ExperimentalListener
    override fun removeListener(listener: SettingsListener) {
        val platformListener = listener as? Listener ?: return
        val listenerDelegate = platformListener.delegate
        delegate?.unregisterOnSharedPreferenceChangeListener(listenerDelegate)
    }

    @ExperimentalListener
    class Listener internal constructor(
        internal val delegate: SharedPreferences.OnSharedPreferenceChangeListener
    ) : SettingsListener {
        internal class Cache(var value: Any?)
    }

}
