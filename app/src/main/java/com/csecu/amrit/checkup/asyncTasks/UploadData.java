package com.csecu.amrit.checkup.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.csecu.amrit.checkup.interfaces.AsyncResponse;
import com.csecu.amrit.checkup.models.Patient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UploadData extends AsyncTask {
    private String insert_url = "https://alittlelearning.000webhostapp.com/android/test/insert.php";
    public AsyncResponse delegate = null;
    private Context context;
    String line = null;

    public UploadData(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Patient patient = (Patient) objects[0];
        String name = patient.getName();
        String phone = patient.getPhone();
        String age = patient.getAge();
        String gender = patient.getGender();
        String date = patient.getDate();
        String reg = patient.getReg();
        String token = patient.getToken();
        try {
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8") + "&" +
                    URLEncoder.encode("age", "UTF-8") + "=" +
                    URLEncoder.encode(age, "UTF-8") + "&" +
                    URLEncoder.encode("gender", "UTF-8") + "=" +
                    URLEncoder.encode(gender, "UTF-8") + "&" +
                    URLEncoder.encode("day", "UTF-8") + "=" +
                    URLEncoder.encode(date, "UTF-8") + "&" +
                    URLEncoder.encode("reg", "UTF-8") + "=" +
                    URLEncoder.encode(reg, "UTF-8") + "&" +
                    URLEncoder.encode("token", "UTF-8") + "=" +
                    URLEncoder.encode(token, "UTF-8");

            URL url = new URL(insert_url);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();

            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return line;
        } catch (Exception e) {
            return line;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}
