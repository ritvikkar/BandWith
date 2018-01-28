package com.ritvikkar.bandwith;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    TextView tvDate;
    SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

//        startActivity(new Intent(MainActivity.this, HomeActivity.class));
//        finish();

        ((MainApplication) getApplication()).openRealm();
        Realm realm = ((MainApplication) getApplication()).getRealmItem();
        UserAccount account = realm.where(UserAccount.class).findFirst();

        if (account != null) {
            login(account.getEmail(), account.getPassword());
        }

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.onboarding_birthdate);
                tvDate = findViewById(R.id.tvDate);
                format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
                Calendar cal = Calendar.getInstance();
                cal.set(cal.get(Calendar.YEAR) - 18, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                tvDate.setText(format.format(cal.getTime()));
                findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isLegalAge()) {
                            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
                    finish();
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

    private boolean isLegalAge() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        try {
            Date convertedDate = format.parse(tvDate.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(cal.get(Calendar.YEAR) - 18, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            if (convertedDate.compareTo(cal.getTime()) > 0) {
                tvDate.setText("Must be 18 or older");
                tvDate.setTextColor(Color.RED);
                return false;
            }
        } catch (Exception e) {
            tvDate.setText("Invalid Date");
            tvDate.setTextColor(Color.RED);
            return false;
        }
        return true;
    }

    public void showDatePicker(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(tvDate.getText().toString()));
        } catch (Exception e) {
            cal.set(cal.get(Calendar.YEAR) - 18, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
        newFragment.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0);
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        tvDate.setText(format.format(calendar.getTime()));
        tvDate.setTextColor(getResources().getColor(R.color.clear_blue));
    }

    @Override
    protected void onDestroy() {
        ((MainApplication) getApplication()).closeRealm();
        super.onDestroy();
    }
}
