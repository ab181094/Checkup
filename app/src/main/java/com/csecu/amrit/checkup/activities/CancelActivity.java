package com.csecu.amrit.checkup.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.asyncTasks.Notify;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.interfaces.AsyncResponse;
import com.csecu.amrit.checkup.models.Message;

import java.util.Calendar;

public class CancelActivity extends AppCompatActivity implements AsyncResponse {
    TextView tvDate;
    EditText etMessage;
    AllOperations operations = new AllOperations(this);
    String reg = "123", date, mes;

    Notify notify = new Notify(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notify.delegate = this;

        tvDate = findViewById(R.id.cancel_tv_date);
        etMessage = findViewById(R.id.cancel_et_message);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvDate.getText() == null || tvDate.getText().length() == 0) {
                    operations.errorToast("Select date first");
                } else {
                    if (etMessage.getText() == null || etMessage.getText().length() == 0) {
                        operations.errorToast("Enter message");
                    } else {
                        date = tvDate.getText().toString().trim();
                        mes = etMessage.getText().toString().trim();
                        Message message = new Message(reg, date, mes);
                        try {
                            notify.execute(message);
                        } catch (Exception e) {
                            operations.errorToast(e.toString());
                        }
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onDatePick(View view) {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        DatePickerDialog dialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String newDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        tvDate.setText(newDate);
                        tvDate.setVisibility(View.VISIBLE);
                    }
                }, year, month, day);
        dialog.show();
    }

    @Override
    public void processFinish(Object output) {
        operations.normalToast(String.valueOf(output));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();
    }
}
