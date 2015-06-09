package com.example.myapplication.myimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by hui.zhang on 2015/6/5.
 */
public class MyImageView extends ImageView {

    private Animation animationScaleSmall;
    private Animation animationScaleNormal;
    private ClickListener mClickListener;
    private float yPoint;
    private boolean isMoveRun = false;
    private boolean isIntercept = false;

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        animationScaleSmall = AnimationUtils.loadAnimation(getContext(), R.anim.small_anim);
        animationScaleNormal = AnimationUtils.loadAnimation(getContext(), R.anim.big_anim);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isIntercept) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                isMoveRun = false;
                yPoint = event.getY();
                startAnimation(animationScaleSmall);
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoveRun) {
                    animationScaleSmall.cancel();
                    startAnimation(animationScaleNormal);
                    animationScaleNormal.setAnimationListener(new CustomAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!isMoveRun && mClickListener != null) {
                                mClickListener.onClick(MyImageView.this);
                            }
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_MOVE:
                ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
                int moveY = (int) (event.getY() - yPoint);
                if (!isMoveRun && Math.abs(moveY) > viewConfiguration.getScaledTouchSlop()) {
                    isMoveRun = true;
                    startAnimation(animationScaleNormal);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isIntercept) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    isMoveRun = true;
                }
                animationScaleSmall.cancel();
                startAnimation(animationScaleNormal);
                break;
        }
        return true;
    }

    public void setIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
    }

    /**
     * 定义一个接口
     */
    public interface ClickListener {
        void onClick(View v);
    }

    public void setOnClick(ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
