package com.qylk.code.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Translate3dAnimation extends Animation {
    private Camera mCamera;
    private float mFromDegree;
    private float mToDegree;
    private float mCenterX, mCenterY;
    private float mDepthZ;
    private boolean mReverse;

    public Translate3dAnimation(float mFromDegree, float mToDegree,
                                float mCenterX, float mCenterY, float depthZ, boolean mReverse) {
        this.mFromDegree = mFromDegree;
        this.mToDegree = mToDegree;
        this.mCenterX = mCenterX;
        this.mCenterY = mCenterY;
        this.mDepthZ = depthZ;
        this.mReverse = mReverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degree = mFromDegree + interpolatedTime
                * (mToDegree - mFromDegree);//当前角度

        final Camera camera = this.mCamera;
        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, interpolatedTime * mDepthZ);
        } else {
            camera.translate(0.0f, 0.0f, (1.0f - interpolatedTime) * mDepthZ);
        }
        camera.rotateY(degree);
        Matrix matrix = t.getMatrix();
        camera.getMatrix(matrix);
        camera.restore();
        final float centerX = this.mCenterX;
        final float centerY = this.mCenterY;
        // 由于缩放是以(0,0)为中心的,所以为了把界面的中心与(0,0)对齐,就要preTranslate(-centerX,
        // -centerY),
        // setScale完成后,调用postTranslate(centerX, centerY),再把图片移回来
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
