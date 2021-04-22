package com.common.res.utils

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import com.common.arms.integration.AppManager
import java.io.IOException
import java.util.*


/**
 * desc :定位相关工具类
 * author：panyy
 * date：2021/04/22
 */
class LocationUtil private constructor() {
    private class MyLocationListener : LocationListener {
        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        override fun onLocationChanged(location: Location) {
            if (mListener != null) {
                mListener!!.onLocationChanged(location)
            }
        }

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            if (mListener != null) {
                mListener!!.onStatusChanged(provider, status, extras)
            }
            when (status) {
                LocationProvider.AVAILABLE -> Log.d("LocationUtils", "当前GPS状态为可见状态")
                LocationProvider.OUT_OF_SERVICE -> Log.d("LocationUtils", "当前GPS状态为服务区外状态")
                LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d("LocationUtils", "当前GPS状态为暂停服务状态")
            }
        }

        /**
         * provider被enable时触发此函数，比如GPS被打开
         */
        override fun onProviderEnabled(provider: String) {}

        /**
         * provider被disable时触发此函数，比如GPS被关闭
         */
        override fun onProviderDisabled(provider: String) {}
    }

    interface OnLocationChangeListener {
        fun isProviderEnabled(enable: Boolean?)

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        fun getLastKnownLocation(location: Location?)

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        fun onLocationChanged(location: Location?)

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) //位置状态发生改变
    }

    companion object {
        private const val TWO_MINUTES = 1000 * 60 * 2
        private var mListener: OnLocationChangeListener? = null
        private var myLocationListener: MyLocationListener? = null
        private var mLocationManager: LocationManager? = null
        //    /**
        //     * you have to check for Location Permission before use this method
        //     * add this code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> to your Manifest file.
        //     * you have also implement LocationListener and passed it to the method.
        //     *
        //     * @param Context
        //     * @param LocationListener
        //     * @return {@code Location}
        //     */
        //
        //    @SuppressLint("MissingPermission")
        //    public static Location getLocation(Context context, LocationListener listener) {
        //        Location location = null;
        //        try {
        //            mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        //            if (!isLocationEnabled()) {
        //                //no Network and GPS providers is enabled
        //                Toast.makeText(context
        //                        , " you have to open GPS or INTERNET"
        //                        , Toast.LENGTH_LONG)
        //                        .show();
        //            } else {
        //                if (isLocationEnabled()) {
        //                    mLocationManager.requestLocationUpdates(
        //                            LocationManager.NETWORK_PROVIDER,
        //                            MIN_TIME_BETWEEN_UPDATES,
        //                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
        //                            listener);
        //
        //                    if (mLocationManager != null) {
        //                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //                        if (location != null) {
        //                            mLocationManager.removeUpdates(listener);
        //                            return location;
        //                        }
        //                    }
        //                }
        //                //when GPS is enabled.
        //                if (isGpsEnabled()) {
        //                    if (location == null) {
        //                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        //                                MIN_TIME_BETWEEN_UPDATES,
        //                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
        //                                listener);
        //
        //                        if (mLocationManager != null) {
        //                            location =
        //                                    mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //                            if (location != null) {
        //                                mLocationManager.removeUpdates(listener);
        //                                return location;
        //                            }
        //                        }
        //                    }
        //                }
        //
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //
        //        return location;
        //    }
        /**
         * 判断Gps是否可用
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val isGpsEnabled: Boolean
            get() {
                val lm = AppManager.getAppManager().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            }

        /**
         * 判断定位是否可用
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val isLocationEnabled: Boolean
            get() {
                val lm = AppManager.getAppManager().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                return (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        || lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            }

        /**
         * 打开Gps设置界面
         */
        fun openGpsSettings() {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            AppManager.getAppManager().application.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        /**
         * 注册
         *
         * 使用完记得调用[.unregister]
         *
         * 需添加权限 `<uses-permission android:name="android.permission.INTERNET" />`
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />`
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`
         *
         * 如果`minDistance`为0，则通过`minTime`来定时更新；
         *
         * `minDistance`不为0，则以`minDistance`为准；
         *
         * 两者都为0，则随时刷新。
         *
         * @param minTime     位置信息更新周期（单位：毫秒）
         * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
         * @param listener    位置刷新的回调接口
         * @return `true`: 初始化成功<br></br>`false`: 初始化失败
         */
        @RequiresPermission(permission.ACCESS_FINE_LOCATION)
        fun register(minTime: Long, minDistance: Long, listener: OnLocationChangeListener?): Boolean {
            if (listener == null) return false
            mListener = listener
            mLocationManager = AppManager.getAppManager().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    && !mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("LocationUtils", "无法定位，请打开定位服务")
                mListener?.isProviderEnabled(false)
                return false
            }
            mListener?.isProviderEnabled(true)
            val provider = mLocationManager!!.getBestProvider(criteria, true)
            val location = mLocationManager!!.getLastKnownLocation(provider!!)
            if (location != null) listener.getLastKnownLocation(location)
            if (myLocationListener == null) myLocationListener = MyLocationListener()
            mLocationManager!!.requestLocationUpdates(provider, minTime, minDistance.toFloat(), myLocationListener!!)
            return true
        }

        /**
         * 注销
         */
        @RequiresPermission(permission.ACCESS_COARSE_LOCATION)
        fun unregister() {
            if (mLocationManager != null) {
                if (myLocationListener != null) {
                    mLocationManager!!.removeUpdates(myLocationListener!!)
                    myLocationListener = null
                }
                mLocationManager = null
            }
            if (mListener != null) {
                mListener = null
            }
        }// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        // 设置是否要求速度
        // 设置是否允许运营商收费
        // 设置是否需要方位信息
        // 设置是否需要海拔信息
        // 设置对电源的需求
        /**
         * 设置定位参数
         *
         * @return [Criteria]
         */
        private val criteria: Criteria
            private get() {
                val criteria = Criteria()
                // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
                criteria.accuracy = Criteria.ACCURACY_FINE
                // 设置是否要求速度
                criteria.isSpeedRequired = false
                // 设置是否允许运营商收费
                criteria.isCostAllowed = false
                // 设置是否需要方位信息
                criteria.isBearingRequired = false
                // 设置是否需要海拔信息
                criteria.isAltitudeRequired = false
                // 设置对电源的需求
                criteria.powerRequirement = Criteria.POWER_LOW
                return criteria
            }

        /**
         * 根据经纬度获取地理位置
         *
         * @param latitude  纬度
         * @param longitude 经度
         * @return [Address]
         */
        fun getAddress(latitude: Double, longitude: Double): Address? {
            val geocoder = Geocoder(AppManager.getAppManager().application, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses.size > 0) return addresses[0]
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 根据经纬度获取所在国家
         *
         * @param latitude  纬度
         * @param longitude 经度
         * @return 所在国家
         */
        fun getCountryName(latitude: Double, longitude: Double): String {
            val address = getAddress(latitude, longitude)
            return if (address == null) "unknown" else address.countryName
        }

        /**
         * 根据经纬度获取所在地
         *
         * @param latitude  纬度
         * @param longitude 经度
         * @return 所在地
         */
        fun getLocality(latitude: Double, longitude: Double): String {
            val address = getAddress(latitude, longitude)
            return if (address == null) "unknown" else address.locality
        }

        /**
         * 根据经纬度获取所在街道
         *
         * @param latitude  纬度
         * @param longitude 经度
         * @return 所在街道
         */
        fun getStreet(latitude: Double, longitude: Double): String {
            val address = getAddress(latitude, longitude)
            return if (address == null) "unknown" else address.getAddressLine(0)
        }

        /**
         * 是否更好的位置
         *
         * @param newLocation         The new Location that you want to evaluate
         * @param currentBestLocation The current Location fix, to which you want to compare the new one
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isBetterLocation(newLocation: Location, currentBestLocation: Location?): Boolean {
            if (currentBestLocation == null) {
                // A new location is always better than no location
                return true
            }

            // Check whether the new location fix is newer or older
            val timeDelta = newLocation.time - currentBestLocation.time
            val isSignificantlyNewer = timeDelta > TWO_MINUTES
            val isSignificantlyOlder = timeDelta < -TWO_MINUTES
            val isNewer = timeDelta > 0

            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved
            if (isSignificantlyNewer) {
                return true
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false
            }

            // Check whether the new location fix is more or less accurate
            val accuracyDelta = (newLocation.accuracy - currentBestLocation.accuracy).toInt()
            val isLessAccurate = accuracyDelta > 0
            val isMoreAccurate = accuracyDelta < 0
            val isSignificantlyLessAccurate = accuracyDelta > 200

            // Check if the old and new location are from the same provider
            val isFromSameProvider = isSameProvider(newLocation.provider, currentBestLocation.provider)

            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true
            } else if (isNewer && !isLessAccurate) {
                return true
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true
            }
            return false
        }

        /**
         * 是否相同的提供者
         *
         * @param provider0 提供者1
         * @param provider1 提供者2
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isSameProvider(provider0: String?, provider1: String?): Boolean {
            return if (provider0 == null) {
                provider1 == null
            } else provider0 == provider1
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}