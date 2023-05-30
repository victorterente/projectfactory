package com.example.projectfactory;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class EventInfoActivity extends AppCompatActivity {

    private TextView startTextView, nomeTextView, endTextView, dataTextView, descricaoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Retrieve the evento_id from the previous activity
        String eventoId = getIntent().getStringExtra("evento_id");

        // Initialize TextViews
        startTextView = findViewById(R.id.startTextView);
        nomeTextView = findViewById(R.id.nomeTextView);
        endTextView = findViewById(R.id.endTextView);
        dataTextView = findViewById(R.id.dataTextView);
        descricaoTextView = findViewById(R.id.descricaoTextView);

        // Fetch event details from the API
        fetchEventDetails(eventoId);
    }

    private void fetchEventDetails(String eventoId) {
        new Thread(() -> {
            try {
                URL url = new URL("https://projectfactory.fly.dev/api/eventos/" + eventoId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response from the API
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                }
                String response = stringBuilder.toString();

                // Parse the response to obtain event details
                JSONObject eventDetails = new JSONObject(response);

                // Extract the required parameters
                String eventoStart = eventDetails.getString("evento_start");
                String eventoNome = eventDetails.getString("evento_nome");
                String eventoEnd = eventDetails.getString("evento_end");
                String eventoData = eventDetails.getString("evento_data");
                String eventoDescricao = eventDetails.getString("evento_descricao");

                // Update the TextViews with the event details
                runOnUiThread(() -> {
                    startTextView.setText(eventoStart);
                    nomeTextView.setText(eventoNome);
                    endTextView.setText(eventoEnd);
                    dataTextView.setText(eventoData);
                    descricaoTextView.setText(eventoDescricao);
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
