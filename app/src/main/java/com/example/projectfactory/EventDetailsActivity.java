package com.example.projectfactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventDetailsActivity extends AppCompatActivity {
    private TextView eventDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventDataTextView = findViewById(R.id.eventDataTextView);

        String apiUrl = getIntent().getStringExtra("apiUrl");

        new FetchEventDataTask().execute(apiUrl);
    }

    private class FetchEventDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String eventData = "";

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray jsonArray = new JSONArray(response.toString());

                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String distanciaP = jsonObject.getString("dados_distanciap");
                    String velocidadeMaxima = jsonObject.getString("dados_velocidademaxima");
                    String velocidadeMedia = jsonObject.getString("dados_velocidademedia");

                    eventData = "Distancia percorrida: " + distanciaP + "\n" +
                            "Velocidade máxima: " + velocidadeMaxima + "\n" +
                            "Velocidade média: " + velocidadeMedia;
                }

                reader.close();
                inputStream.close();
                connection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return eventData;
        }

        @Override
        protected void onPostExecute(String eventData) {
            eventDataTextView.setText(eventData);
        }
    }
}
