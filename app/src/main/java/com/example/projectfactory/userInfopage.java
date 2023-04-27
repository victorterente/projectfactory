package com.example.projectfactory;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class userInfopage extends AppCompatActivity {

    private TextView tvEmailValue, tvPasswordValue, tvNameValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfopage);

        // get user id from preferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);

        // find layout views by id
        tvEmailValue = findViewById(R.id.tvEmailValue);
        tvPasswordValue = findViewById(R.id.tvPasswordValue);
        tvNameValue = findViewById(R.id.tvNameValue);

        // call API to get user information
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://projectfactory.fly.dev/api/users/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // get user information from API response
                            String userName = response.getString("user_name");
                            String userEmail = response.getString("user_email");
                            String userPassword = response.getString("user_password");

                            // populate user information layout with API data
                            tvEmailValue.setText(userEmail);
                            tvPasswordValue.setText(userPassword);
                            tvNameValue.setText(userName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle API error
                        Toast.makeText(userInfopage.this, "API error", Toast.LENGTH_SHORT).show();
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

        // add API request to Volley queue
        queue.add(jsonObjectRequest);
    }
}
