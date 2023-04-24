package com.example.projectfactory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projectfactory.LoginDetails.LoginDataSource;
import com.example.projectfactory.Downloaders.PostData;
import com.example.projectfactory.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button registar;
    private EditText email;
    private EditText pass;
    private EditText nome;
    String postBDate;
    ArrayList<String> listGender;
    ArrayAdapter<String> adapterGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.etFullName);
        email = findViewById(R.id.etEmail);
        pass = findViewById(R.id.etPassword);
        registar = findViewById(R.id.btnRegister);

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (nome.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Favor preencher o campo em vermelho", Toast.LENGTH_SHORT).show();
                        nome.setHintTextColor(Color.RED);
                    }
                    if (pass.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Favor preencher o campo em vermelho", Toast.LENGTH_SHORT).show();
                        pass.setHintTextColor(Color.RED);
                    }
                    if (email.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Favor preencher o campo em vermelho", Toast.LENGTH_SHORT).show();
                        email.setHintTextColor(Color.RED);
                    }
                    else {
                        Map<String, String> postData = new HashMap<>();
                        postData.put("user_name", nome.getText().toString());
                        postData.put("user_email", email.getText().toString());
                        postData.put("user_password", pass.getText().toString());

                        // Debugging statement
                        Log.d("PostData", "PostData: " + postData);



                        PostData task = new PostData(postData);
                        JSONArray arr = task.execute("https://projectfactory.fly.dev/api/users").get();
                        if (arr != null && arr.length() > 0) {
                            // handle successful post
                        } else {
                            // handle post failure
                        }


                        // Debugging statement
                        Log.d("JSONArray", "JSONArray: " + arr);

                        Toast.makeText(getApplicationContext(), "Welcome !"+ nome.getText().toString(), Toast.LENGTH_SHORT).show();

                        // Debugging statement
                        Log.d("Id", "ID: " + LoginDataSource.ID);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
