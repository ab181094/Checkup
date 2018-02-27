package com.csecu.amrit.checkup.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.models.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Doctor> doctorsList;
    private AllOperations operations;
    private Context context;

    public CustomListAdapter(Context context, ArrayList<Doctor> doctorsList) {
        this.doctorsList = doctorsList;
        operations = new AllOperations(context);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        Doctor doctor = doctorsList.get(position);

        String name = doctor.getName();
        String qua = doctor.getQualification();
        String spe = doctor.getSpeciality();

        name = operations.decodeString(name);
        qua = operations.decodeString(qua);
        spe = operations.decodeString(spe);

        viewHolder.tvName.setText(name);
        viewHolder.tvQua.setText(qua);
        viewHolder.tvSpe.setText(spe);

        if (doctor.getImage() != null) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("Photos/" + doctor.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .into(viewHolder.imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    operations.errorToast("Failed to load images");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item,
                parent, false);
        return new MyViewHolder(view);
    }

    public Doctor getItem(int position) {
        return doctorsList.get(position);
    }

    /**
     * View holder class
     */
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private TextView tvName;
        private TextView tvQua;
        private TextView tvSpe;

        private MyViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.doctor_image);
            tvName = view.findViewById(R.id.doctor_name);
            tvQua = view.findViewById(R.id.doctor_qualification);
            tvSpe = view.findViewById(R.id.doctor_speciality);
        }
    }
}