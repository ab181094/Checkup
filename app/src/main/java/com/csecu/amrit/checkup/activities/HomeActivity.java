package com.csecu.amrit.checkup.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.adapters.HomeAdapter;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.models.Icon;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Icon> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = findViewById(R.id.home_gridView);
        list = new ArrayList<>();
        list.add(new Icon(R.drawable.ic_person, "Doctor"));
        list.add(new Icon(R.drawable.ic_person, "Diagnostic Center"));
        list.add(new Icon(R.drawable.ic_person, "Sign in"));
        list.add(new Icon(R.drawable.ic_person, "Sign up"));
        list.add(new Icon(R.drawable.ic_person, "Cancel Appointment"));
        list.add(new Icon(R.drawable.ic_person, "Sign out"));

        HomeAdapter adapter = new HomeAdapter(this, list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(HomeActivity.this, DoctorsListActivity.class);
                    startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(HomeActivity.this, DoctorsListActivity.class);
                    startActivity(intent);
                } else if (i == 2) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if (i == 3) {
                    Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                } else if (i == 4) {
                    SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                    boolean status = prefs.getBoolean("status", false);
                    if (status) {
                        Intent intent = new Intent(HomeActivity.this, CancelActivity.class);
                        startActivity(intent);
                    } else {
                        AllOperations operations = new AllOperations(HomeActivity.this);
                        operations.errorToast("You are not logged in");
                    }
                } else if (i == 5) {
                    SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
                    editor.putBoolean("status", false);
                    editor.apply();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();
    }
}
