package com.example.projectfactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AdminUserManagement extends AppCompatActivity {

    private static final String API_URL = "https://projectfactory.fly.dev/api/users";
    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView usersListView;
    private Button acceptButton;
    private Button rejectButton;

    private List<String> userList;
    private ArrayAdapter<String> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);

        usersListView = findViewById(R.id.usersListView);
        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);

        userList = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        usersListView.setAdapter(usersAdapter);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = usersListView.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    String selectedUser = userList.get(selectedPosition);
                    // Lógica para aceitar a admissão de administrador do usuário selecionado
                    Toast.makeText(AdminUserManagement.this, "Usuário aceito: " + selectedUser, Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = usersListView.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    String selectedUser = userList.get(selectedPosition);
                    // Lógica para recusar a admissão de administrador do usuário selecionado
                    Toast.makeText(AdminUserManagement.this, "Usuário recusado: " + selectedUser, Toast.LENGTH_SHORT).show();
                }
            }
        });

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Atualizar os botões com base na seleção do usuário
                acceptButton.setEnabled(position != ListView.INVALID_POSITION);
                rejectButton.setEnabled(position != ListView.INVALID_POSITION);
            }
        });

        FetchUsersTask fetchUsersTask = new FetchUsersTask();
        fetchUsersTask.execute(API_URL);
    }

    private class FetchUsersTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String usersJson = null;

            try {
                URL requestUrl = new URL(url);
                urlConnection = (HttpURLConnection) requestUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                usersJson = buffer.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching users data.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing stream.", e);
                    }
                }
            }

            return usersJson;
        }

        @Override
        protected void onPostExecute(String usersJson) {
            if (usersJson != null) {
                try {
                    JSONArray usersArray = new JSONArray(usersJson);
                    userList.clear();

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObject = usersArray.getJSONObject(i);

                        if (userObject.has("user_status")) {
                            Object userStatusObj = userObject.get("user_status");

                            if (userStatusObj instanceof Boolean) {
                                boolean userStatus = (boolean) userStatusObj;

                                if (!userStatus) {
                                    String userName = userObject.getString("user_name");
                                    userList.add(userName);
                                }
                            } else if (userStatusObj instanceof String) {
                                String userStatusStr = (String) userStatusObj;
                                boolean userStatus = Boolean.parseBoolean(userStatusStr);

                                if (!userStatus) {
                                    String userName = userObject.getString("user_name");
                                    userList.add(userName);
                                }
                            }
                        }
                    }

                    usersAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing users data.", e);
                }
            } else {
                Toast.makeText(AdminUserManagement.this, "Error fetching users data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}