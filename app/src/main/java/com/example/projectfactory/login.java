package com.example.projectfactory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.projectfactory.Downloaders.JSONArrayDownloader;

import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private JSONArray loginCredentials;
    private String USER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLoginButtonClick();

            }
        });
    }

    public void onLoginButtonClick() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        JSONObject users;

        // Check if email and password are not empty
        if (!email.isEmpty() && !password.isEmpty()) {
            boolean found = false;

            // JSON array downloader (liga a task)
            JSONArrayDownloader task = new JSONArrayDownloader();

            try {
                loginCredentials = task.execute("https://projectfactory.fly.dev/api/users").get();
                Log.e("credentials:", "" + loginCredentials);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            // Check if the login credentials array is not null
            if (loginCredentials != null) {
                // Check if the entered email and password exist in the login credentials array
                for (int i = 0; i < loginCredentials.length(); i++) {
                    try {
                        users = loginCredentials.getJSONObject(i);
                        String user_email = users.getString("email");
                        String user_password = users.getString("password");

                        if (email.equals(user_email) && password.equals(user_password)) {
                            found = true;
                            USER_ID = users.getString("id");
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // Display an error message if the login credentials array is null
                Toast.makeText(login.this, "Could not download login credentials", Toast.LENGTH_SHORT).show();
            }

            // If the entered email and password are found, start the next activity
            if (found) {
                Intent intent = new Intent(login.this, MainActivity.class);
                intent.putExtra("USER_ID", USER_ID);
                startActivity(intent);
            } else {
                // Display an error message if the entered email and password do not exist
                Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Display an error message if either the email or password is empty
            Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }
    }
}
