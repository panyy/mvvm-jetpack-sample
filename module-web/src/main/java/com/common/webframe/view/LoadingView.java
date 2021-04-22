package com.common.webframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.common.webframe.R;

public class LoadingView extends LinearLayout {

    private ProgressBar progressBar;
    private TextView tvResult;
    private ImageView ivResult;
    private Context context;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_view, this, true);
        progressBar = view.findViewById(R.id.progressBar);
        tvResult = findViewById(R.id.tv_result);
        ivResult = findViewById(R.id.iv_result);
    }

    /**
     * loading
     */
    public void showLoading() {
        ivResult.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    /**
     * 成功
     */
    public void showSuccess() {
        ivResult.setImageResource(R.drawable.ic_load_success);
        ivResult.setVisibility(View.VISIBLE);
        progressBar.setVisibility(GONE);
    }

    /**
     * 失败
     */
    public void showFail() {
        ivResult.setImageResource(R.drawable.ic_load_fail);
        ivResult.setVisibility(View.VISIBLE);
        progressBar.setVisibility(GONE);
    }

    /**
     * 提示文字
     *
     * @param txt string
     */
    public void setText(String txt) {
        tvResult.setText(txt);
    }

    /**
     * 提示文字
     */
    public void setText(@StringRes int txtId) {
        tvResult.setText(txtId);
    }
}
