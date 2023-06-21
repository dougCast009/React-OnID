package com.react.biometric;

import static com.react.biometric.Constantes.OPTION_MODALIDAD;
import static com.react.biometric.Constantes.USER_NAME;
import static com.react.biometric.Constantes.USER_PASSWORD;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SelectorActivity extends AppCompatActivity {

    private String userName;
    private String userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(getString(R.string.title_selector));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        userName = getIntent().getStringExtra(USER_NAME);
        userPassword = getIntent().getStringExtra(USER_PASSWORD);
        Button btnEnrola = (Button) findViewById(R.id.btnEnrolamiento);
        Button btnValida = (Button) findViewById(R.id.btnValidacion);

        btnEnrola.setOnClickListener(view -> seleccionarMetodo(Constantes.MODALIDADENROLA));

        btnValida.setOnClickListener(view -> seleccionarMetodo(Constantes.MODALIDADVALIDA));
    }

    public void seleccionarMetodo(String metodo) {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(USER_PASSWORD, userPassword);
        intent.putExtra(OPTION_MODALIDAD, metodo);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("¿Esta seguro que desea salir?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Intent intent = new Intent(SelectorActivity.this, IniciarSesionActivity.class);
                    intent.putExtra(Constantes.CERRO_SESION, true);
                    startActivity(intent);
                }).create().show();
    }
}