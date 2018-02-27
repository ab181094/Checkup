package com.csecu.amrit.checkup.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLDecoder;
import java.net.URLEncoder;

import es.dmoral.toasty.Toasty;

/**
 * Created by Amrit on 2/18/2018.
 */

public class AllOperations {
    Context context;

    public AllOperations(Context context) {
        this.context = context;
    }

    public void successToast(String s) {
        Toasty.success(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void errorToast(String s) {
        Toasty.error(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void infoToast(String s) {
        Toasty.info(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void warningToast(String s) {
        Toasty.warning(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void normalToast(String s) {
        Toasty.normal(context, s).show();
    }

    public String getEncodedString(String string, EditText editText) {
        string = editText.getText().toString().trim();
        return encodeString(string);
    }

    public String encodeString(String string) {
        try {
            string = URLEncoder.encode(string, "UTF-8");
        } catch (Exception e) {
            errorToast("" + e.toString());
        }
        return string;
    }

    public String decodeString(String string) {
        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (Exception e) {
            errorToast("" + e.toString());
        }
        return string;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
