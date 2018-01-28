package com.ritvikkar.bandwith;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        Realm realm = ((MainApplication) getApplication()).getRealmItem();
        UserAccount account = realm.where(UserAccount.class).findFirst();

        if (account != null) {
            showProgressDialog();
            login(account.getEmail(), account.getPassword());
        }
        ButterKnife.bind(this);
    }

    private void registerClick() {
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
                    Toast.makeText(MainActivity.this, "Registration ok", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loginClock() {
        if (!isFormValid()) {
            return;
        }
        showProgressDialog();
        login(etEmail.getText().toString(), etPassword.getText().toString());
    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // open home screen activity
                }
                else {
                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            etPassword.setError("The password cannot be empty");
            return false;
        }
        if (!etPassword.getText().equals(etConfirmPassword.getText())) {
            etPassword.setError("Passwords don't match");
            etConfirmPassword.setText("");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        ((MainApplication) getApplication()).closeRealm();
        super.onDestroy();
    }
}
