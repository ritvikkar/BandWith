package com.ritvikkar.bandwith;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.layoutSignUp)
    LinearLayout layoutSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        setTheme(R.style.AppTheme_Cursor);

        etName.setHint("Full Name");
        etName.setHintTextColor(getResources().getColor(R.color.powder_blue));
        etEmail.setHint("Email");
        etEmail.setHintTextColor(getResources().getColor(R.color.powder_blue));
        etPassword.setHint("Password");
        etPassword.setHintTextColor(getResources().getColor(R.color.powder_blue));
        etConfirmPassword.setHint("Confirm Password");
        etConfirmPassword.setHintTextColor(getResources().getColor(R.color.powder_blue));

        layoutSignUp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                layoutSignUp.getWindowVisibleDisplayFrame(r);

                int heightDiff = layoutSignUp.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff < 100) {
                    layoutSignUp.requestFocus();
                }
            }
        });
    }

    @OnFocusChange(R.id.etName)
    public void onNameClick() {
        if (etName.hasFocus()) {
            etName.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
        }
    }

    @OnFocusChange(R.id.etEmail)
    public void onEmailClick() {
        if (etEmail.hasFocus()) {
            etEmail.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
        }
    }

    @OnFocusChange(R.id.etPassword)
    public void onPasswordClick() {
        if (etPassword.hasFocus()) {
            etPassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
        }
    }

    @OnFocusChange(R.id.etConfirmPassword)
    public void onConfirmPasswordClick() {
        if (etConfirmPassword.hasFocus()) {
            etConfirmPassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
        }
    }

    @OnFocusChange(R.id.layoutSignUp)
    public void layoutClicked() {
        if (layoutSignUp.hasFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
