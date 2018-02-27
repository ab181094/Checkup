package com.csecu.amrit.checkup.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.controllers.AllOperations;
import com.csecu.amrit.checkup.helpers.DataHelper;
import com.csecu.amrit.checkup.models.Doctor;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ImageActivity extends AppCompatActivity {
    Doctor doctor;
    ImageView imageView;
    private static final int GALLERY_INTENT = 2;
    AllOperations operations = new AllOperations(this);
    Uri uri;
    String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            doctor = bundle.getParcelable("doctor");
        }

        imageView = findViewById(R.id.image_imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHelper dataHelper = new DataHelper(ImageActivity.this);
                if (dataHelper.insertDoctor(doctor, uri)) {
                    operations.successToast("Registered");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ImageActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }, 2000);
                } else {
                    operations.errorToast("Failed");
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onTakeImage(View view) {
        if (mayRequestStorage()) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        } else {
            operations.errorToast("Set the permissions first");
        }
    }

    private boolean mayRequestStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            Snackbar.make(imageView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (uri != null) {
                imageView.setImageURI(uri);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        File file = new File(picturePath);
                        String filePath = file.getName();
                        picture = doctor.getPhone()
                                + filePath.substring(filePath.lastIndexOf("."));
                        doctor.setImage(picture);
                    }
                } catch (Exception e) {
                    operations.errorToast("" + e.toString());
                } finally {
                    if (cursor != null) {
                        cursor.close();
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
