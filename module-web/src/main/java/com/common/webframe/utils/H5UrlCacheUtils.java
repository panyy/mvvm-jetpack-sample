package com.common.webframe.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * desc :H5地址缓存工具类
 * author：panyy
 * date：2021/04/22
 */
public class H5UrlCacheUtils {

    /**
     * 反序列化, 将磁盘文件转化为对象
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Object readObject(Context context, String fileName) {
        Object object = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();
            fis.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 将对象序列化到磁盘文件中
     *
     * @param context
     * @param object
     * @param fileName
     */
    public static void writeObject(Context context, Object object, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加缓存
     *
     * @param context
     */
    public static void addAddress(Context context, String entity) {
        Object obj = readObject(context, "addressList");
        ArrayList<String> addressList;
        if (obj == null) {
            addressList = new ArrayList<>();
        } else {
            addressList = (ArrayList<String>) obj;
        }
        //如果存在则移除，然后添加
        Iterator<String> iterator = addressList.iterator();
        while (iterator.hasNext()) {
            String address = iterator.next();
            if (entity.equals(address)) {
                iterator.remove();
            }
        }
        addressList.add(0, entity);
        writeObject(context, addressList, "addressList");
    }

    /**
     * 移除缓存
     *
     * @param context
     */
    public static void removeAddress(Context context, String entity) {
        Object obj = readObject(context, "addressList");
        ArrayList<String> addressList;
        if (obj == null) {
            addressList = new ArrayList<>();
        } else {
            addressList = (ArrayList<String>) obj;
        }
        //如果存在则移除，然后添加
        Iterator<String> iterator = addressList.iterator();
        while (iterator.hasNext()) {
            String address = iterator.next();
            if (entity.equals(address)) {
                iterator.remove();
            }
        }
        writeObject(context, addressList, "addressList");
    }

    /**
     * 清空缓存列表
     *
     * @param context
     */
    public static void clearAddressList(Context context) {
        writeObject(context, new ArrayList<>(), "addressList");
    }

    /**
     * 获取缓存列表
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getAddressList(Context context) {
        Object obj = readObject(context, "addressList");
        ArrayList<String> addressList;
        if (obj == null) {
            addressList = new ArrayList<>();
        } else {
            addressList = (ArrayList<String>) obj;
        }
        return addressList;
    }


}
