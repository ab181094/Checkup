package com.csecu.amrit.checkup.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.interfaces.Receivable;
import com.csecu.amrit.checkup.models.Doctor;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity implements Receivable {
    EditText etName, etPhone, etPass, etRePass, etQua, etSpe, etCha, etMax, etFee;
    Spinner spSex;
    ToggleButton tgSun, tgMon, tgTue, tgWed, tgThu, tgFri, tgSat;
    TextView tvStart, tvEnd;

    String name, phone, pass, rePass, qua, spe, cha, patient, fee, sex, day, start, end;
    String reg = "123", token;
    AllOperations operations = new AllOperations(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!operations.isNetworkAvailable()) {
            operations.warningToast("Check your Internet Connection");
        }

        linkAll();
        setInitialValue();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllValues();
                if (checkAllValues()) {
                    Doctor doctor = new Doctor();
                    doctor.setName(name);
                    doctor.setPhone(phone);
                    doctor.setPassword(pass);
                    doctor.setQualification(qua);
                    doctor.setSpeciality(spe);
                    doctor.setChamber(cha);
                    doctor.setPatient(patient);
                    doctor.setFee(fee);
                    doctor.setSex(sex);
                    doctor.setDay(day);
                    doctor.setStart(start);
                    doctor.setEnd(end);
                    doctor.setRegistration(reg);
                    doctor.setToken(token);
                    Intent intent = new Intent(RegistrationActivity.this, ImageActivity.class);
                    intent.putExtra("doctor", doctor);
                    startActivity(intent);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setInitialValue() {
        String[] genders = getResources().getStringArray(R.array.gender_arrays);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                R.layout.registration_spinner_item, R.id.spinner_item, genders);
        spSex.setAdapter(genderAdapter);
    }

    private void linkAll() {
        etName = findViewById(R.id.reg_et_name);
        etPhone = findViewById(R.id.reg_et_phone);
        etPass = findViewById(R.id.reg_et_password);
        etRePass = findViewById(R.id.reg_et_re_password);
        etQua = findViewById(R.id.reg_et_qualification);
        etSpe = findViewById(R.id.reg_et_speciality);
        etCha = findViewById(R.id.reg_et_chamber);
        etMax = findViewById(R.id.reg_et_patient);
        etFee = findViewById(R.id.reg_et_fee);
        spSex = findViewById(R.id.reg_spinner_gender);
        tgSun = findViewById(R.id.reg_tg_sunday);
        tgMon = findViewById(R.id.reg_tg_monday);
        tgTue = findViewById(R.id.reg_tg_tuesday);
        tgWed = findViewById(R.id.reg_tg_wednesday);
        tgThu = findViewById(R.id.reg_tg_thursday);
        tgFri = findViewById(R.id.reg_tg_friday);
        tgSat = findViewById(R.id.reg_tg_saturday);
        tvStart = findViewById(R.id.reg_tv_startTime);
        tvEnd = findViewById(R.id.reg_tv_endTime);
    }

    public void onStartTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                start = selectedHour + ":" + selectedMinute;
                tvStart.setText(start);
                tvStart.setVisibility(View.VISIBLE);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void onEndTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                end = selectedHour + ":" + selectedMinute;
                tvEnd.setText(end);
                tvEnd.setVisibility(View.VISIBLE);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void getAllValues() {
        name = operations.getEncodedString(name, etName);
        phone = operations.getEncodedString(phone, etPhone);
        pass = operations.getEncodedString(pass, etPass);
        rePass = operations.getEncodedString(rePass, etRePass);
        qua = operations.getEncodedString(qua, etQua);
        spe = operations.getEncodedString(spe, etSpe);
        cha = operations.getEncodedString(cha, etCha);
        patient = operations.getEncodedString(patient, etMax);
        fee = operations.getEncodedString(fee, etFee);
        sex = (String) spSex.getSelectedItem();
        ArrayList<String> list = new ArrayList<>();
        if (tgSun.isChecked()) {
            list.add("Sunday");
        }
        if (tgMon.isChecked()) {
            list.add("Monday");
        }
        if (tgTue.isChecked()) {
            list.add("Tuesday");
        }
        if (tgWed.isChecked()) {
            list.add("Wednesday");
        }
        if (tgThu.isChecked()) {
            list.add("Thursday");
        }
        if (tgFri.isChecked()) {
            list.add("Friday");
        }
        if (tgSat.isChecked()) {
            list.add("Saturday");
        }
        day = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == (list.size() - 1)) {
                day = day + list.get(i);
            } else {
                day = day + list.get(i);
                day = day + ",";
            }
        }
        day = operations.encodeString(day);
        start = operations.encodeString(start);
        end = operations.encodeString(end);
        token = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public boolean checkAllValues() {
        if (pass.length() < 5) {
            View focusView = etPass;
            etPass.setError("Too short");
            focusView.requestFocus();
            return false;
        } else {
            if (!pass.equals(rePass)) {
                View focusView = etRePass;
                etRePass.setError("Passwords don't match");
                focusView.requestFocus();
                return false;
            } else {
                if (name.length() > 0 && phone.length() > 0 && pass.length() > 0 && spe.length() > 0) {
                    return true;
                } else {
                    if (name.length() == 0) {
                        View focusView = etName;
                        etName.setError("This field can't be Empty");
                        focusView.requestFocus();
                        return false;
                    } else if (phone.length() == 0) {
                        View focusView = etPhone;
                        etPhone.setError("This field can't be Empty");
                        focusView.requestFocus();
                        return false;
                    } else if (pass.length() == 0) {
                        View focusView = etPass;
                        etPass.setError("This field can't be Empty");
                        focusView.requestFocus();
                        return false;
                    } else {
                        View focusView = etSpe;
                        etSpe.setError("This field can't be Empty");
                        focusView.requestFocus();
                        return false;
                    }
                }
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
