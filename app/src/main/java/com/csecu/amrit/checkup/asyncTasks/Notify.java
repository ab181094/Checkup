package com.csecu.amrit.checkup.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.csecu.amrit.checkup.interfaces.AsyncResponse;
import com.csecu.amrit.checkup.models.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Amrit on 2/21/2018.
 */

public class Notify extends AsyncTask {
    private String insert_url = "https://alittlelearning.000webhostapp.com/android/test/send.php";
    public AsyncResponse delegate = null;
    private Context context;

    public Notify(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Message message = (Message) objects[0];
        String date = message.getDate();
        String reg = message.getReg();
        String mes = message.getString();

        try {
            String data = URLEncoder.encode("day", "UTF-8") + "=" +
                    URLEncoder.encode(date, "UTF-8") + "&" +
                    URLEncoder.encode("reg", "UTF-8") + "=" +
                    URLEncoder.encode(reg, "UTF-8") + "&" +
                    URLEncoder.encode("mes", "UTF-8") + "=" +
                    URLEncoder.encode(mes, "UTF-8");

            URL url = new URL(insert_url);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return "Success!";
        } catch (Exception e) {
            return "Failed";
        }
    }
}
