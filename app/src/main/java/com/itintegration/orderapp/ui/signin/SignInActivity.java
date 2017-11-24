package com.itintegration.orderapp.ui.signin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.itintegration.orderapp.ui.home.HomeActivity;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.databinding.ActivitySignInBinding;

import com.bumptech.glide.Glide;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        //loadImage();
    }

    public void load(View view) {
        animateButtonWidth();
        // TODO : sign in through asyncTask
        fadeOutTextAndShowProgressDialog(); // TODO : to be used on conjunction with LoginTask?
        nextAction();
    }

    private void fadeOutTextAndShowProgressDialog() {
        mBinding.text.animate().alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showProgressDialog();
                    }
                })
                .start();
    }

    private void animateButtonWidth() {
        FrameLayout layoutEmail = findViewById(R.id.emailButton);
        layoutEmail.animate().alpha(0.0f);
        layoutEmail.animate().translationY(layoutEmail.getHeight());

        FrameLayout layoutPassword = findViewById(R.id.passwordButton);
        layoutPassword.animate().alpha(0.0f);
        layoutPassword.animate().translationY(layoutPassword.getHeight());

        ValueAnimator anim = ValueAnimator.ofInt(mBinding.signInButton.getMeasuredWidth(), getFabWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mBinding.signInButton.getLayoutParams();
                layoutParams.width = val;
                mBinding.signInButton.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void showProgressDialog() {
        mBinding.progressBar.setAlpha(1f);
        mBinding.progressBar
                .getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mBinding.progressBar.setVisibility(VISIBLE);
    }

    private void nextAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();
                fadeOutProgressDialog();
                delayedStartNextActivity();
            }
        }, 1000);
    }

    private void revealButton() {
        mBinding.signInButton.setElevation(0f);

        mBinding.reveal.setVisibility(VISIBLE);

        int cx = mBinding.reveal.getWidth();
        int cy = mBinding.reveal.getHeight();


        int x = (int) (getFabWidth() / 2 + mBinding.signInButton.getX());
        int y = (int) (getFabWidth() / 2 + mBinding.signInButton.getY());

        float finalRadius = Math.max(cx, cy) * 1.2f;

        Animator reveal = ViewAnimationUtils
                .createCircularReveal(mBinding.reveal, x, y, getFabWidth(), finalRadius);

        reveal.setDuration(450);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                reset(animation);
                finish();
            }

            private void reset(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.reveal.setVisibility(INVISIBLE);
                mBinding.text.setVisibility(VISIBLE);
                mBinding.text.setAlpha(1f);
                mBinding.signInButton.setElevation(4f);
                ViewGroup.LayoutParams layoutParams = mBinding.signInButton.getLayoutParams();
                layoutParams.width = (int) (getResources().getDisplayMetrics().density * 300);
                mBinding.signInButton.requestLayout();
            }
        });
        reveal.start();
    }

    private void fadeOutProgressDialog() {
        mBinding.progressBar.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        }).start();
    }

    private void delayedStartNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 1);
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    private void loadImage() {
        Glide.with(this).load("http://www.samsung-wallpapers.com/uploads/allimg/150531/1-150531151256.jpg") // TODO : Dynamic source or fixed?
                .into(mBinding.background);
    }






























}
