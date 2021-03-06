package com.common.webframe.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.common.webframe.R;

public class ProgressDWebView extends DWebView {
    private ProgressBar mProgressBar;

    public ProgressDWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
        mProgressBar.setLayoutParams(layoutParams);
        Drawable drawable = context.getResources().getDrawable(R.drawable.layer_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new MyWebChromeClient());
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null) return false;
            try {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
            } catch (Exception e) {//??????crash (???????????????????????????????????????scheme?????????url???APP, ?????????crash)
                return true;//???????????????app????????????true?????????????????????????????????????????????????????????????????????????????????
            }
            //????????????true??????????????????WebView????????????false??????????????????????????????????????????
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        ViewGroup.LayoutParams lp = mProgressBar.getLayoutParams();
        if (lp instanceof AbsoluteLayout.LayoutParams) {
            AbsoluteLayout.LayoutParams flp = (AbsoluteLayout.LayoutParams) lp;
            flp.x = l;
            flp.y = t;
            mProgressBar.setLayoutParams(lp);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

}