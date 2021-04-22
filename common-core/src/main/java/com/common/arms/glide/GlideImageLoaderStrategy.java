package com.common.arms.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.common.arms.di.module.GlobalConfigModule;
import com.common.arms.http.imageloader.BaseImageLoaderStrategy;
import com.common.arms.http.imageloader.ImageConfig;
import com.common.arms.http.imageloader.glide.GlideAppliesOptions;
import com.common.arms.http.imageloader.glide.GlideArms;
import com.common.arms.http.imageloader.glide.GlideRequest;
import com.common.arms.http.imageloader.glide.GlideRequests;
import com.common.arms.utils.Preconditions;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ================================================
 * 此类只是简单的实现了 Glide 加载的策略,方便快速使用,但大部分情况会需要应对复杂的场景
 * 这时可自行实现 {@link BaseImageLoaderStrategy} 和 {@link ImageConfig} 替换现有策略
 * ================================================
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {

    @Override
    public void loadImage(@Nullable Context ctx, @Nullable ImageConfigImpl config) {
        Preconditions.checkNotNull(ctx, "Context is required");
        Preconditions.checkNotNull(config, "ImageConfigImpl is required");
        Preconditions.checkNotNull(config.getImageView(), "ImageView is required");

        GlideRequests requests;

        requests = GlideArms.with(ctx);//如果context是activity则自动使用Activity的生命周期

        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl());

        switch (config.getCacheStrategy()) {
            //缓存策略
            case CacheStrategy.NONE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case CacheStrategy.RESOURCE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case CacheStrategy.DATA:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case CacheStrategy.AUTOMATIC:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }

        if (config.isCrossFade()) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade());
        }

        if (config.isCenterCrop()) {
            glideRequest.centerCrop();
        }

        if (config.isCircle()) {
            glideRequest.circleCrop();
        }

        if (config.isImageRadius()) {
            glideRequest.transform(new RoundedCorners(config.getImageRadius()));
        }

        if (config.isBlurImage()) {
            glideRequest.transform(new BlurTransformation(config.getBlurValue()));
        }

        if (config.getTransformation() != null) {//glide用它来改变图形的形状
            glideRequest.transform(config.getTransformation());
        }

        if (config.getPlaceholder() != null)//设置占位符
        {
            glideRequest.placeholder(config.getPlaceholder());
        }

        if (config.getErrorPic() != null)//设置错误的图片
        {
            glideRequest.error(config.getErrorPic());
        }

        if (config.getFallback() != null)//设置请求 url 为空图片
        {
            glideRequest.fallback(config.getFallback());
        }

        glideRequest
                .into(config.getImageView());
    }

    @Override
    public void clear(@Nullable final Context ctx, @Nullable ImageConfigImpl config) {
        Preconditions.checkNotNull(ctx, "Context is required");
        Preconditions.checkNotNull(config, "ImageConfigImpl is required");

        if (config.getImageView() != null) {
            GlideArms.get(ctx).getRequestManagerRetriever().get(ctx).clear(config.getImageView());
        }

        if (config.getImageViews() != null && config.getImageViews().length > 0) {//取消在执行的任务并且释放资源
            for (ImageView imageView : config.getImageViews()) {
                GlideArms.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }

        if (config.isClearDiskCache()) {//清除本地缓存
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    Glide.get(ctx).clearDiskCache();
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }

        if (config.isClearMemory()) {//清除内存缓存
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    Glide.get(ctx).clearMemory();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    @Override
    public void applyGlideOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        Timber.i("applyGlideOptions");
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        Timber.i("registerComponents");
    }
}
