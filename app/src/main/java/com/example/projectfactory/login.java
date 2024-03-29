package com.example.projectfactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mEmailEditText = findViewById(R.id.etEmail);
        mPasswordEditText = findViewById(R.id.etPassword);
        mLoginButton = findViewById(R.id.btnLogin);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                // Make POST request
                String url = "https://projectfactory.fly.dev/api/users/login";
                JSONObject data = new JSONObject();
                try {
                    data.put("email", email);
                    data.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Get user id from response
                                    int userId = response.getInt("user_id");

                                    // Save user id to preferences
                                    SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt("userId", userId);
                                    editor.apply();

                                    // Handle success response
                                    Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                    // Log user id
                                    Log.d("Login", "User id: " + userId);

                                    // Start user information activity

                                    Intent intent = new Intent(login.this, Detalhes.class);



                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response
                                Toast.makeText(login.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        // Set headers
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                // Add request to queue
                Volley.newRequestQueue(login.this).add(request);
            }
        });
    }

    public void onRegisterButtonClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}