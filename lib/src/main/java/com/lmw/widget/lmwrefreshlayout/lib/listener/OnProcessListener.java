package com.lmw.widget.lmwrefreshlayout.lib.listener;

import android.view.View;
import android.view.ViewGroup;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;

public interface OnProcessListener {


    /**
     * 布局刷新操作
     *
     * @param layout
     */
    void setLayout(ViewGroup layout, int targetY);

    /**
     * 完成
     *
     * @param v
      @param state 刷新或加载更多
     */
    void onFinish(View v, State state);

    /**
     * 用于获取拉取的距离
     *
     * @param header
     * @param footer
     * @param pullDistance
     */
    void onPull(View header, View footer, float pullDistance);
}
