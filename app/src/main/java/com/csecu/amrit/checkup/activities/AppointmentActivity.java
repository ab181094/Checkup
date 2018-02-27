package com.csecu.amrit.checkup.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.asyncTasks.UploadData;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.helpers.DataHelper;
import com.csecu.amrit.checkup.interfaces.AsyncResponse;
import com.csecu.amrit.checkup.interfaces.Receivable;
import com.csecu.amrit.checkup.models.Patient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity implements Receivable, AsyncResponse {
    EditText etName, etPhone, etAge;
    Spinner spGender;
    TextView tvDate;

    AllOperations operations;

    String name, phone, age, gender, date, reg, token = "1234";
    int month, day, year;

    UploadData uploadData = new UploadData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg = bundle.getString("reg");
        }
        operations = new AllOperations(this);
        uploadData.delegate = this;

        linkAll();
        setInitialValues();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllValues();
                if (checkAllValues()) {
                    Patient patient = new Patient();
                    patient.setName(name);
                    patient.setPhone(phone);
                    patient.setAge(age);
                    patient.setGender(gender);
                    patient.setDate(date);
                    patient.setReg(reg);
                    patient.setToken(token);

                    DataHelper dataHelper = new DataHelper(AppointmentActivity.this);
                    /*if (dataHelper.insertPatient(patient)) {
                        uploadData.execute(patient);
                        // operations.successToast("Appointment has been made");
                    } else {
                        // operations.errorToast("Failed");
                    }*/
                    try {
                        uploadData.execute(patient);
                    } catch (Exception e) {
                        operations.errorToast(e.toString());
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setInitialValues() {
        String[] genders = getResources().getStringArray(R.array.gender_arrays);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                R.layout.registration_spinner_item, R.id.spinner_item, genders);
        spGender.setAdapter(genderAdapter);

        final Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        String newDate = year + "-" + (month + 1) + "-" + day;
        tvDate.setText(newDate);
        tvDate.setVisibility(View.VISIBLE);
    }

    private void linkAll() {
        etName = findViewById(R.id.app_et_name);
        etPhone = findViewById(R.id.app_et_phone);
        etAge = findViewById(R.id.app_et_age);
        spGender = findViewById(R.id.app_spinner_gender);
        tvDate = findViewById(R.id.app_tv_date);
    }

    public void onDatePick(View view) {
        final Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        DatePickerDialog dialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String newDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        tvDate.setText(newDate);
                    }
                }, year, month, day);
        dialog.show();
    }

    @Override
    public void getAllValues() {
        name = operations.getEncodedString(name, etName);
        phone = operations.getEncodedString(phone, etPhone);
        age = operations.getEncodedString(age, etAge);
        gender = (String) spGender.getSelectedItem();
        date = operations.encodeString(tvDate.getText().toString().trim());
        token = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public boolean checkAllValues() {
        if (name.length() > 0 && phone.length() > 0) {
            return true;
        } else {
            if (name.length() == 0) {
                View focusView = etName;
                etName.setError("This field can't be Empty");
                focusView.requestFocus();
                return false;
            } else {
                View focusView = etPhone;
                etPhone.setError("This field can't be Empty");
                focusView.requestFocus();
                return false;
            }
        }
    }

    @Override
    public void processFinish(Object output) {
        operations.normalToast(String.valueOf(output));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppointmentActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();
    }
}