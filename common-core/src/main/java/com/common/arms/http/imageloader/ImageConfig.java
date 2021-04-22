package com.common.arms.http.imageloader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * ================================================
 * 这里是图片加载配置信息的基类,定义一些所有图片加载框架都可以用的通用参数
 * 每个 {@link BaseImageLoaderStrategy} 应该对应一个 {@link ImageConfig} 实现类
 * ================================================
 */
public class ImageConfig {
    protected String url;
    protected ImageView imageView;
    protected Drawable placeholder;//占位符
    protected Drawable errorPic;//错误占位符

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Drawable getPlaceholder() {
        return placeholder;
    }

    public Drawable getErrorPic() {
        return errorPic;
    }
}
