package com.common.arms.glide;

import android.graphics.Bitmap;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.common.arms.utils.FastBlur;

import java.security.MessageDigest;

/**
 * ================================================
 * 高斯模糊
 * ================================================
 */
public class BlurTransformation extends BitmapTransformation {
    public static final int DEFAULT_RADIUS = 15;
    private static final String ID = BlurTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);
    private int mRadius;

    public BlurTransformation(@IntRange(from = 0) int radius) {
        mRadius = radius;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return FastBlur.doBlur(toTransform, mRadius, true);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlurTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
