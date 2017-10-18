package com.flipboard.upgrade.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.flipboard.upgrade.R;

/**
 * Created by Administrator on 2017/10/13.
 */

public class FlipUpgradeView extends View {
    private static final String TAG = "FlipUpgradeView";

    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private Camera mCamera = new Camera();
    private int mZDegree, mRightYDegree, mTopYDegree;
    private Bitmap mFlipBitmap;
    private int mCenterX, mCenterY, x, y;
    private Runnable mInitTask = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "onAnimationEnd: ");
            mZDegree = 0;
            mRightYDegree = 0;
            mTopYDegree = 0;
            invalidate();
        }
    };


    public FlipUpgradeView(Context context) {
        super(context);
    }

    public FlipUpgradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mFlipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fg_flipboard);
        ObjectAnimator rightUpAnimator = ObjectAnimator.ofInt(this, "rightYDegree", 0, -45);
        rightUpAnimator.setDuration(1000);
        ObjectAnimator zRotateAnimator = ObjectAnimator.ofInt(this, "zDegree", 0, -270);
        zRotateAnimator.setDuration(1500);
        zRotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator topUpAnimator = ObjectAnimator.ofInt(this, "topYDegree", 0, 30);
        topUpAnimator.setDuration(1000);
        mAnimatorSet.playSequentially(rightUpAnimator, zRotateAnimator, topUpAnimator);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(mInitTask, 500);
            }
        });
    }

    public void startAnimator() {
        if (mAnimatorSet.isRunning()) {
            mAnimatorSet.end();
        }
        mAnimatorSet.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimatorSet.end();
        removeCallbacks(mInitTask);
    }

    @SuppressWarnings("unused")
    public void setRightYDegree(int degree) {
        mRightYDegree = degree;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setTopYDegree(int degree) {
        mTopYDegree = degree;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setZDegree(int degree) {
        mZDegree = degree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initLocArgs();
        drawHalfChanged(canvas);
        drawHalfNotChanged(canvas);
    }

    /**
     * 绘制变化的一半
     */
    private void drawHalfNotChanged(Canvas canvas) {
        canvas.save();
        mCamera.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(mZDegree);
        canvas.clipRect(-mCenterX, -mCenterY, 0, mCenterY);
        mCamera.rotateY(mTopYDegree);
        mCamera.applyToCanvas(canvas);
        canvas.rotate(-mZDegree);
        canvas.translate(-mCenterX, -mCenterY);
        mCamera.restore();
        canvas.drawBitmap(mFlipBitmap, x, y, null);
        canvas.restore();
    }

    /**
     * 绘制没有变化的一半
     */
    private void drawHalfChanged(Canvas canvas) {
        canvas.save();
        mCamera.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(mZDegree);
        canvas.clipRect(0, -mCenterY, mCenterX, mCenterY);
        mCamera.rotateY(mRightYDegree);
        mCamera.applyToCanvas(canvas);
        canvas.rotate(-mZDegree);
        canvas.translate(-mCenterX, -mCenterY);
        mCamera.restore();
        canvas.drawBitmap(mFlipBitmap, x, y, null);
        canvas.restore();
    }

    /**
     * 初始化参数
     */
    private void initLocArgs() {
        if (mCenterX == 0) {
            mCenterX = getWidth() >> 1;
            mCenterY = getHeight() >> 1;
            x = mCenterX - (mFlipBitmap.getWidth() >> 1);
            y = mCenterY - (mFlipBitmap.getHeight() >> 1);
        }
    }
}
