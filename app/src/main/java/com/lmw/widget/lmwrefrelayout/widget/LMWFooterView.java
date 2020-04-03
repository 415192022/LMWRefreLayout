package com.lmw.widget.lmwrefrelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.lmw.widget.lmwrefrelayout.R;
import com.lmw.widget.lmwrefreshlayout.lib.model.State;
import com.lmw.widget.lmwrefreshlayout.lib.widget.DefaultFooterView;

public class LMWFooterView extends DefaultFooterView {
    private static final String TAG = "GFooterView";
    // 下拉箭头的转180°动画
    private RotateAnimation reverseUpAnimation;
    private RotateAnimation reverseDownAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;
    private View loading_icon;
    private View mFooter;

    public LMWFooterView(Context context) {
        this(context, null);
    }

    public LMWFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LMWFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mFooter = inflater.inflate(R.layout.load_more, this, false);
        loading_icon = mFooter.findViewById(R.id.loading_icon);
        addView(mFooter);
    }


    @Override
    public void onStateChanged(State state) {
        TextView state_tv = (TextView) mFooter.findViewById(R.id.load_text);

        if (state == State.PREPARE_TO_LOAD){
            state_tv.setText("加载");
        }else if(state == State.IDLE){
            state_tv.setText("空");
            mFooter.findViewById(R.id.pullup_icon).setVisibility(View.VISIBLE);
        }else if(state == State.RELEASE_TO_LOAD){
            state_tv.setText("释放加载");
            mFooter.findViewById(R.id.pullup_icon).setVisibility(View.VISIBLE);
        }else if(state == State.LOADING){
            state_tv.setText("正在加载...");
            mFooter.findViewById(R.id.pullup_icon).setVisibility(View.GONE);
        }else if(state == State.RELEASE_NEXT_FLOOR){
            state_tv.setText("释放进入下一层");
        }
    }

}
