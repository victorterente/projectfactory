package com.example.projectfactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CriarEvento extends Activity {
    private static final String API_URL = "https://projectfactory.fly.dev/api/eventos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);
    }

    public void createEvent(View view) {
        EditText editStart = findViewById(R.id.edit_start);
        String start = editStart.getText().toString();

        EditText editEnd = findViewById(R.id.edit_end);
        String end = editEnd.getText().toString();

        EditText editName = findViewById(R.id.edit_name);
        String name = editName.getText().toString();

        EditText editLatitude = findViewById(R.id.edit_latitude);
        String latitude = editLatitude.getText().toString();

        EditText editLongitude = findViewById(R.id.edit_longitude);
        String longitude = editLongitude.getText().toString();

        EditText editDescription = findViewById(R.id.edit_description);
        String description = editDescription.getText().toString();

        EditText editDate = findViewById(R.id.edit_date);
        String date = editDate.getText().toString();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("evento_start", start);
            jsonParams.put("evento_end", end);
            jsonParams.put("evento_nome", name);
            jsonParams.put("evento_lat", latitude);
            jsonParams.put("evento_long", longitude);
            jsonParams.put("evento_descricao", description);
            jsonParams.put("evento_data", date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new CreateEventTask().execute(jsonParams.toString());
    }

    private class CreateEventTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(params[0].getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                return connection.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 200) {
                // Event created successfully
                Toast.makeText(CriarEvento.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                // Error occurred while creating event
                Toast.makeText(CriarEvento.this, "Error creating event", Toast.LENGTH_SHORT).show();
            }
        }
    }
}