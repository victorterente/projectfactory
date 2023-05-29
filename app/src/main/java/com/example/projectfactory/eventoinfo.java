package com.example.projectfactory;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class eventoinfo extends AppCompatActivity {

    private TextView textViewEventInfo;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventoinfo);

        textViewEventInfo = findViewById(R.id.textViewEventInfo);
        registerButton = findViewById(R.id.buttonRegister);

        String eventoId = getIntent().getStringExtra("evento_id");
        String eventDetailsUrl = "https://projectfactory.fly.dev/api/eventos/" + eventoId;

        new FetchEventDetailsTask().execute(eventDetailsUrl);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                int userId = preferences.getInt("userId", 0);
                if (userId != 0) {
                    registerEvent(userId, eventoId);
                } else {
                    Toast.makeText(eventoinfo.this, "User ID not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class FetchEventDetailsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                result = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject eventDetails = new JSONObject(result);
                    String eventName = eventDetails.getString("evento_nome");
                    String eventDescription = eventDetails.getString("evento_descricao");
                    String eventLat = eventDetails.getString("evento_lat");
                    String eventLong = eventDetails.getString("evento_long");
                    String eventData = eventDetails.getString("evento_data");
                    String eventStart = eventDetails.getString("evento_start");
                    String eventEnd = eventDetails.getString("evento_end");

                    String eventInfo = "Nome: " + eventName
                            + "\n\nDescrição: " + eventDescription
                            + "\n\nLatitude: " + eventLat
                            + "\nLongitude: " + eventLong
                            + "\nData: " + eventData
                            + "\nInício: " + eventStart
                            + "\nFim: " + eventEnd;

                    textViewEventInfo.setText(eventInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }

        private void showErrorMessage() {
            Toast.makeText(eventoinfo.this, "Erro ao obter detalhes do evento.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerEvent(int userId, String eventoId) {
        String registerUrl = "https://projectfactory.fly.dev/api/inscricoes";
        new RegisterEventTask().execute(registerUrl, String.valueOf(userId), eventoId);
    }

    private class RegisterEventTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String registerUrl = params[0];
            String userId = params[1];
            String eventoId = params[2];

            String result = null;
            try {
                URL url = new URL(registerUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                JSONObject requestBody = new JSONObject();
                requestBody.put("inscricao_user_id", userId);
                requestBody.put("inscricao_evento_id", eventoId);

                connection.setDoOutput(true);
                connection.getOutputStream().write(requestBody.toString().getBytes());

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                result = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    Toast.makeText(eventoinfo.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }

        private void showErrorMessage() {
            Toast.makeText(eventoinfo.this, "Erro ao registrar evento.", Toast.LENGTH_SHORT).show();
        }
    }
}
