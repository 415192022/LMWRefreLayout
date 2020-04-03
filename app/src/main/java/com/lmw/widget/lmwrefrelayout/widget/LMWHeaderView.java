package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.lmw.widget.lmwrefrelayout.R;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.widget.DefaultHeaderView;

public class LMWHeaderView extends DefaultHeaderView {

    public final String TAG = "GHeaderView";

    // 下拉箭头的转180°动画
    private RotateAnimation reverseUpAnimation;
    private RotateAnimation reverseDownAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    private View refreshing_icon;
    private View mHeader;

    public LMWHeaderView(Context context) {
        this(context, null);
    }

    public LMWHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LMWHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        reverseUpAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_up_anim);
        reverseDownAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_down_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating_anim);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        reverseUpAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
        LayoutInflater inflater = LayoutInflater.from(context);
        mHeader = inflater.inflate(R.layout.refresh_head, this, false);
        refreshing_icon = mHeader.findViewById(R.id.refreshing_icon);
        addView(mHeader);
    }

    @Override
    public void onStateChanged(State state) {
        TextView state_tv = (TextView) mHeader.findViewById(R.id.refresh_text);

        if (state == State.PREPARE_TO_REFRESH) {
            state_tv.setText("下拉刷新");
            Log.e("onIdle", "onPrepare");
        } else if (state == State.IDLE) {
            state_tv.setText("空");
            Log.e("onIdle", "停止加载动画");
        } else if (state == State.RELEASE_TO_REFRESH) {
            refreshing_icon.setVisibility(View.VISIBLE);
            refreshing_icon.clearAnimation();
            state_tv.setText("释放刷新");
            Log.e("onIdle", "onStart");
        } else if (state == State.REFRESHING) {
            refreshing_icon.setVisibility(View.VISIBLE);
            state_tv.setText("正在刷新...");
            Log.e("onIdle", "onHandling");
        } else if (state == State.RELEASE_PRE_FLOOR) {
            state_tv.setText("释放进入上一层");
        }
    }

}
