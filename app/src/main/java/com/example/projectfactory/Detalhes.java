package com.example.projectfactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Detalhes extends AppCompatActivity {
    private boolean userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);

        // Make API request to get user_status
        String url = "https://projectfactory.fly.dev/api/users/" + userId;
        new GetUserStatusTask().execute(url);
    }

    public void onClickUserInfo(View v) {
        Intent intent = new Intent(getApplicationContext(), userInfopage.class);
        startActivity(intent);
    }

    public void onClickEventList(View v) {
        Intent intent = new Intent(getApplicationContext(), eventlist.class);
        startActivity(intent);
    }

    public void onClickMeusEventos(View v) {
        Intent intent = new Intent(getApplicationContext(), meuseventos.class);
        startActivity(intent);
    }

    public void onClickMapaEventos(View v) {
        Intent intent = new Intent(getApplicationContext(), Maps.class);
        startActivity(intent);
    }

    public void onCLickAdminManagement(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminUserManagement.class);
        startActivity(intent);
    }

    public void onClickCriarEvento(View v) {
        Intent intent = new Intent(getApplicationContext(), criarevento.class);
        startActivity(intent);
    }

    private void updateButtonVisibility() {
        Button button1 = findViewById(R.id.buttonUserInfo);
        Button button2 = findViewById(R.id.buttonListaEventos);
        Button button3 = findViewById(R.id.buttonMeusEventos);
        Button button4 = findViewById(R.id.buttonMapaEventos);
        Button button5 = findViewById(R.id.adminManagementButton);
        Button button6 = findViewById(R.id.buttonCriarEvento);

        if (userStatus) {
            // Show all buttons
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            button5.setVisibility(View.VISIBLE);
            button6.setVisibility(View.VISIBLE);
        } else {
            // Show only the first three buttons
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.GONE);
            button5.setVisibility(View.GONE);
            button6.setVisibility(View.GONE);
        }
    }


    private class GetUserStatusTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            String url = urls[0];
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    return jsonObject.getBoolean("user_status");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            userStatus = result;
            updateButtonVisibility();
        }
    }
}
