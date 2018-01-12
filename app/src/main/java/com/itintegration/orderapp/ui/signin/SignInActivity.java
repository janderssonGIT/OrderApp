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
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.itintegration.orderapp.OrderApp;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.di.component.DaggerActivityComponent;
import com.itintegration.orderapp.di.module.ActivityModule;
import com.itintegration.orderapp.ui.home.HomeActivity;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.databinding.ActivitySignInBinding;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    @Inject
    DataManager mDataManager;

    private ActivitySignInBinding mBinding;
    private ActivityComponent activityComponent;

    //dagger 2
    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(OrderApp.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        //dagger 2
        getActivityComponent().inject(this);

        FrameLayout signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
        loadImage();
    }

    @Override
    public void onClick(View view) {
        signIn();
        load(view);
    }

    private void signIn() {
        TextInputEditText etUser = findViewById(R.id.emailEditText);
        TextInputEditText etPass = findViewById(R.id.passwordEditText);
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();

        //TODO : login with (not yet created) AsyncTask "LoginTask".
        //run task with parameters user & pass to confirm user access to app
    }


    //TODO : Call load() on successful LoginTask result.
    public void load(View view) {
        animateButtonWidth();
        // TODO : Put this progressDialog to live between "OnPreExecute" and "OnPostExecute" in the LoginTask. Remove from load()
        fadeOutTextAndShowProgressDialog();
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
                mBinding.signInButton.setVisibility(INVISIBLE);
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
        Glide.with(this).load("https://s-media-cache-ak0.pinimg.com/originals/08/05/b2/0805b2791432c4afd3147364e665dc58.jpg") // TODO : Make dynamic depending on user.
                .into(mBinding.background);
    }

}
