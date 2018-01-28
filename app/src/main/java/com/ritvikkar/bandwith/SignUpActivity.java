package com.ritvikkar.bandwith;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import io.realm.Realm;

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
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPassword)
    TextView tvPassword;
    @BindView(R.id.tvConfirmPassword)
    TextView tvConfirmPassword;

    private float height;

    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        setTheme(R.style.AppTheme_Cursor);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

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


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

    }

    @OnClick(R.id.btnContinue)
    public void registerClick() {
        if (!isFormValid()) {
            return;
        }
        showProgressDialog();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    FirebaseUser fbUser = task.getResult().getUser();
                    createUser(fbUser.getUid(), fbUser.getEmail());
                    fbUser.updateProfile(new UserProfileChangeRequest.Builder().
                            setDisplayName(etName.getText().toString()).build());
                    Realm realm = ((MainApplication) getApplication()).getRealmItem();
                    realm.beginTransaction();
                    UserAccount account = realm.createObject(UserAccount.class, fbUser.getUid());
                    account.setEmail(fbUser.getEmail());
                    account.setPassword(etPassword.getText().toString());
                    realm.commitTransaction();
                    Toast.makeText(SignUpActivity.this, "Registration ok", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String userId, String email) {
        User user = new User(userId, etName.getText().toString(), email);
        databaseRef.child("users").child(userId).setValue(user);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait for it...");
        }
        progressDialog.show();
    }


    private boolean isFormValid() {
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("The name cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
            etEmail.setError("Not a valid email");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("The password cannot be empty");
            return false;
        }
        if (etConfirmPassword.getText().length() < 6) {
            etConfirmPassword.setError("The password must be at least 6 characters long");
            return false;
        }
        if (!etPassword.getText().equals(etConfirmPassword.getText())) {
            etPassword.setError("Passwords don't match");
            etConfirmPassword.setText("");
            return false;
        }
        return true;
    }

    @OnFocusChange(R.id.etName)
    public void onNameClick() {
        if (etName.hasFocus()) {
            etName.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
            tvName.setVisibility(View.VISIBLE);
        }
        else if (etName.getText().toString().equals("")) {
            etName.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_error));
            tvName.setVisibility(View.INVISIBLE);
        }
    }

    @OnFocusChange(R.id.etEmail)
    public void onEmailClick() {
        if (etEmail.hasFocus()) {
            etEmail.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
            tvEmail.setVisibility(View.VISIBLE);
        }
        else if (etEmail.getText().toString().equals("")) {
            etEmail.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_error));
            tvEmail.setVisibility(View.INVISIBLE);
        }
    }

    @OnFocusChange(R.id.etPassword)
    public void onPasswordClick() {
        if (etPassword.hasFocus()) {
            etPassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
            tvPassword.setVisibility(View.VISIBLE);
        }
        else if (etPassword.getText().toString().equals("")) {
            etPassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_error));
            tvPassword.setVisibility(View.INVISIBLE);
        }
    }

    @OnFocusChange(R.id.etConfirmPassword)
    public void onConfirmPasswordClick() {
        if (etConfirmPassword.hasFocus()) {
            etConfirmPassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_darker));
            tvConfirmPassword.setVisibility(View.VISIBLE);
//            translatePage(true);
        }
        else {
            if (etConfirmPassword.getText().toString().equals("")) {
                etConfirmPassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_box_error));
                tvConfirmPassword.setVisibility(View.INVISIBLE);
//                translatePage(false);
            }
        }
    }

    private void translatePage(boolean up) {
        float translateHeight = -height / 28;
        float yDelta = 0;
        if (!up) {
            translateHeight = 0;
            yDelta = -height / 28;
        }
        TranslateAnimation anim = new TranslateAnimation( 0, 0, yDelta, translateHeight);

        anim.setDuration(400);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setStartOffset(300);
        layoutSignUp.startAnimation(anim);
    }

    @OnFocusChange(R.id.layoutSignUp)
    public void onLayoutClick() {
        if (layoutSignUp.hasFocus()) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
