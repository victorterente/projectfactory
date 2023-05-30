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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdminUserManagement extends AppCompatActivity {

    private static final String API_URL = "https://projectfactory.fly.dev/api/users";
    private static final String TAG = AdminUserManagement.class.getSimpleName();

    private ListView usersListView;
    private List<String> userList;
    private List<Integer> userIdList;
    private ArrayAdapter<String> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);

        usersListView = findViewById(R.id.usersListView);
        Button acceptButton = findViewById(R.id.acceptButton);
        Button rejectButton = findViewById(R.id.rejectButton);

        userList = new ArrayList<>();
        userIdList = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, userList);
        usersListView.setAdapter(usersAdapter);
        usersListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Atualizar os botões com base na seleção do usuário
                acceptButton.setEnabled(position != ListView.INVALID_POSITION);
                rejectButton.setEnabled(position != ListView.INVALID_POSITION);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = usersListView.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    int selectedUserId = userIdList.get(selectedPosition);
                    updateUserData(selectedUserId, true, 1);
                    Toast.makeText(AdminUserManagement.this, "Usuário aceito: " + userList.get(selectedPosition), Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = usersListView.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    int selectedUserId = userIdList.get(selectedPosition);
                    updateUserData(selectedUserId, false, 0);
                    Toast.makeText(AdminUserManagement.this, "Usuário recusado: " + userList.get(selectedPosition), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FetchUsersTask fetchUsersTask = new FetchUsersTask();
        fetchUsersTask.execute(API_URL);
    }

    private void updateUserData(int userId, boolean userStatus, int userRoleId) {
        String updateUrl = API_URL + "/" + userId;
        Log.d(TAG, "Updating user with ID: " + userId);
        Log.d(TAG, "Update URL: " + updateUrl);

        try {
            JSONObject userJson = new JSONObject();
            userJson.put("user_status", userStatus);
            userJson.put("user_role_id", userRoleId);
            userJson.put("user_id", userId);

            UpdateUserTask updateUserTask = new UpdateUserTask();
            updateUserTask.execute(updateUrl, userJson.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error creating user JSON.", e);
        }
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
                    userIdList.clear();

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObject = usersArray.getJSONObject(i);

                        if (userObject.has("user_status")) {
                            Object userStatusObj = userObject.get("user_status");

                            if (userStatusObj instanceof Boolean) {
                                boolean userStatus = (boolean) userStatusObj;

                                if (!userStatus) {
                                    String userName = userObject.getString("user_name");
                                    int userId = userObject.getInt("user_id");
                                    userList.add(userName);
                                    userIdList.add(userId);
                                }
                            } else if (userStatusObj instanceof String) {
                                String userStatusStr = (String) userStatusObj;
                                boolean userStatus = Boolean.parseBoolean(userStatusStr);

                                if (!userStatus) {
                                    String userName = userObject.getString("user_name");
                                    int userId = userObject.getInt("user_id");
                                    userList.add(userName);
                                    userIdList.add(userId);
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

    private class UpdateUserTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            String userData = params[1];
            HttpURLConnection urlConnection = null;
            OutputStream outputStream = null;
            BufferedWriter writer = null;

            try {
                URL requestUrl = new URL(url);
                urlConnection = (HttpURLConnection) requestUrl.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                outputStream = urlConnection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(userData);
                writer.flush();

                int responseCode = urlConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException e) {
                Log.e(TAG, "Error updating user data.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing writer.", e);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing output stream.", e);
                    }
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                FetchUsersTask fetchUsersTask = new FetchUsersTask();
                fetchUsersTask.execute(API_URL);
            } else {
                Toast.makeText(AdminUserManagement.this, "Error updating user data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
