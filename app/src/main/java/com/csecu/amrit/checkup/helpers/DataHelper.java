package com.csecu.amrit.checkup.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.models.Doctor;
import com.csecu.amrit.checkup.models.Patient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Amrit on 2/18/2018.
 */

public class DataHelper {
    private Context context;
    AllOperations operations;

    public DataHelper(Context context) {
        this.context = context;
        operations = new AllOperations(context);
    }

    public boolean insertDoctor(final Doctor doctor, final Uri uri) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference donorReference = database.getReference("Doctor");

        donorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(doctor.getPhone()).exists()) {

                } else {
                    donorReference.child(doctor.getPhone()).setValue(doctor);

                    if (uri != null) {
                        StorageReference reference = FirebaseStorage.getInstance().getReference();
                        StorageReference imageRef = reference.child("Photos").child(doctor.getImage());
                        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                operations.successToast("Image uploaded successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                operations.errorToast("Image upload failed");
                            }
                        });
                    } else {
                        operations.warningToast("You've not selected a photo");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                operations.errorToast(databaseError.toString());
            }
        });
        return true;
    }

    /*public boolean insertPatient(final Patient patient) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference donorReference = database.getReference("Patient");

        donorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(patient.getReg()).child(patient.getPhone()).exists()) {
                } else {
                    donorReference.child(patient.getReg()).child(patient.getPhone()).setValue(patient);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                operations.errorToast(databaseError.toString());
            }
        });
        return true;
    }*/
}
