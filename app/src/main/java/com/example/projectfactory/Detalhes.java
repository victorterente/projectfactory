package com.example.projectfactory;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Detalhes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
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
}