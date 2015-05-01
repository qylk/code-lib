package com.qylk.code.demos;

import com.qylk.code.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import com.qylk.code.animation.Translate3dAnimation;
public class test3dAnimation extends Activity implements OnClickListener {
	private ImageView image;
	private boolean mReversed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		image = new ImageView(this);
		image.setImageResource(R.drawable.icon);
		addContentView(image, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		image.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (mReversed)
			applyRotation(180.0f, 90.0f);// 已经翻转的情况下，从180度转回到90度
		else
			applyRotation(0.0f, 90.0f);// 从0度转到90度
	}

	private void applyRotation(float fromDegree, float toDegree) {
		float centerX = image.getWidth() / 2;
		float centerY = image.getHeight() / 2;
		Translate3dAnimation animation = new Translate3dAnimation(fromDegree,
				toDegree, centerX, centerY, 310.0f, true);
		animation.setFillAfter(true);
		animation.setDuration(500);
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setAnimationListener(new AnimationListener());
		image.startAnimation(animation);
	}

	/**
	 * 后半段动画,转90度
	 */
	private final Runnable NextHalfRotation = new Runnable() {

		@Override
		public void run() {
			float centerX = image.getWidth() / 2;
			float centerY = image.getHeight() / 2;
			Translate3dAnimation animation;
			if (mReversed)
				animation = new Translate3dAnimation(90.0f, 0.0f, centerX,
						centerY, 310.0f, false);// 转回到0度
			else
				animation = new Translate3dAnimation(90.0f, 180.0f, centerX,
						centerY, 310.0f, false);// 继续转90度
			animation.setDuration(500);
			animation.setFillAfter(true);
			animation.setInterpolator(new DecelerateInterpolator());
			image.startAnimation(animation);
			mReversed = !mReversed;
		}
	};

	private class AnimationListener implements Animation.AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			image.post(NextHalfRotation);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	}
}
