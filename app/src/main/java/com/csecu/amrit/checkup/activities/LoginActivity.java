package com.csecu.amrit.checkup.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.interfaces.Receivable;
import com.csecu.amrit.checkup.models.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements Receivable {
    EditText etContact, etPassword;
    String contact = null, password = null;

    AllOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linkAll();
        operations = new AllOperations(this);

        if (!operations.isNetworkAvailable()) {
            operations.warningToast("Check your Internet Connection");
        }
    }

    private void linkAll() {
        etContact = findViewById(R.id.login_et_contact);
        etPassword = findViewById(R.id.login_et_password);
    }

    public void onLogin(View view) {
        getAllValues();
        boolean status = checkAllValues();
        if (status) {
            if (!operations.isNetworkAvailable()) {
                operations.errorToast("Check your Internet Connection");
            } else {
                checkDoctor();
            }
        }
    }

    private void checkDoctor() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference doctorReference = database.getReference("Doctor");

        doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(contact).exists()) {
                    Doctor doctor = dataSnapshot.child(contact).getValue(Doctor.class);
                    if (password.equals(doctor.getPassword())) {
                        operations.successToast("Sign-in successful");

                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
                        editor.putBoolean("status", true);
                        editor.apply();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }, 2000);
                    } else {
                        operations.errorToast("Password doesn't match. Try again");
                    }
                } else {
                    operations.errorToast("You are not registered");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                operations.errorToast(databaseError.toString());
            }
        });
    }

    @Override
    public void getAllValues() {
        contact = operations.getEncodedString(contact, etContact);
        password = operations.getEncodedString(password, etPassword);
    }

    @Override
    public boolean checkAllValues() {
        if (contact.length() > 0 && password.length() > 0) {
            return true;
        } else {
            if (contact.length() == 0) {
                View focusView = etContact;
                etContact.setError("This field can't be empty");
                focusView.requestFocus();
                return false;
            } else {
                View focusView = etPassword;
                etPassword.setError("This field can't be empty");
                focusView.requestFocus();
                return false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();
    }
}
