package com.csecu.amrit.checkup.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.models.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDetailsActivity extends AppCompatActivity {
    Doctor doctor;
    TextView tvName, tvPhone, tvGender, tvQua, tvSpe, tvReg, tvCha, tvDays, tvTime;
    CircleImageView imageView;
    AllOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            doctor = bundle.getParcelable("doctor");
        }
        operations = new AllOperations(this);

        linkAll();
        setValuesToAll();
    }

    private void setValuesToAll() {
        if (doctor.getImage() != null) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("Photos/" + doctor.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(DoctorDetailsActivity.this)
                            .load(uri)
                            .into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    operations.errorToast("Failed to load images");
                }
            });
        }
        tvName.setText(operations.decodeString(doctor.getName()));
        tvPhone.setText(operations.decodeString(doctor.getPhone()));
        tvGender.setText(operations.decodeString(doctor.getSex()));
        tvQua.setText(operations.decodeString(doctor.getQualification()));
        tvSpe.setText(operations.decodeString(doctor.getSpeciality()));
        tvReg.setText(operations.decodeString(doctor.getRegistration()));
        tvCha.setText(operations.decodeString(doctor.getChamber()));
        tvDays.setText(operations.decodeString(doctor.getDay()));
        tvTime.setText(operations.decodeString(doctor.getStart() + " - " + doctor.getEnd()));
    }

    private void linkAll() {
        imageView = findViewById(R.id.details_doctor_image);
        tvName = findViewById(R.id.details_tv_name);
        tvPhone = findViewById(R.id.details_tv_phone);
        tvGender = findViewById(R.id.details_tv_gender);
        tvQua = findViewById(R.id.details_tv_qualifications);
        tvSpe = findViewById(R.id.details_tv_speciality);
        tvReg = findViewById(R.id.details_tv_registration);
        tvCha = findViewById(R.id.details_tv_chamber);
        tvDays = findViewById(R.id.details_tv_days);
        tvTime = findViewById(R.id.details_tv_time);
    }

    public void onAppointment(View view) {
        Intent intent = new Intent(DoctorDetailsActivity.this, AppointmentActivity.class);
        intent.putExtra("reg", doctor.getRegistration());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();
    }
}
