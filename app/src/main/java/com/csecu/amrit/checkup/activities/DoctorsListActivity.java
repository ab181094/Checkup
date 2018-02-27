package com.csecu.amrit.checkup.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.adapters.CustomListAdapter;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.listener.RecyclerItemListener;
import com.csecu.amrit.checkup.models.Doctor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DoctorsListActivity extends AppCompatActivity {
    ArrayList<Doctor> doctorList;
    AllOperations operations;

    RecyclerView recyclerView;
    CustomListAdapter adapter;

    private DatabaseReference donorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.doctor_list_recyclerView);
        operations = new AllOperations(this);

        doctorList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (!operations.isNetworkAvailable()) {
            operations.warningToast("Check your Internet Connection");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        donorRef = database.getReference("Doctor");
        donorRef.keepSynced(true);
        loadList();

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(),
                recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                Doctor doctor = adapter.getItem(position);
                Intent intent = new Intent(DoctorsListActivity.this, DoctorDetailsActivity.class);
                intent.putExtra("doctor", doctor);
                startActivity(intent);
            }

            @Override
            public void onLongClickItem(View v, int position) {

            }
        }));
    }

    private void loadList() {
        donorRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                doctorList.add(dataSnapshot.getValue(Doctor.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                int index = getItemIndex(doctor);
                doctorList.set(index, doctor);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                int index = getItemIndex(doctor);
                doctorList.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new CustomListAdapter(DoctorsListActivity.this, doctorList);
        recyclerView.setAdapter(adapter);
    }

    private int getItemIndex(Doctor doctor) {
        int index = -1;
        for (int i = 0; i < doctorList.size(); i++) {
            if (doctorList.get(i).getName().equals(doctor.getName())) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();
    }
}
