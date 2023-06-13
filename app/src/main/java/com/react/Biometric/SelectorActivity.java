package com.react.Biometric;

import static com.react.Biometric.Constantes.OPTION_MODALIDAD;
import static com.react.Biometric.Constantes.USER_NAME;
import static com.react.Biometric.Constantes.USER_PASSWORD;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectorActivity extends AppCompatActivity {

    private String userName;
    private String userPassword;
    private Button btnEnrola;
    private Button btnValida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(getString(R.string.title_selector));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userName = getIntent().getStringExtra(USER_NAME);
        userPassword = getIntent().getStringExtra(USER_PASSWORD);
        btnEnrola = (Button) findViewById(R.id.btnEnrolamiento);
        btnValida = (Button) findViewById(R.id.btnValidacion);

        btnEnrola.setOnClickListener(view -> {
            SeleccionarMetodo(Constantes.ModalidadEnrola);
        });

        btnValida.setOnClickListener(view -> {
            SeleccionarMetodo(Constantes.ModalidadValida);
        });
    }

    public void SeleccionarMetodo(String Metodo) {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(USER_PASSWORD, userPassword);
        intent.putExtra(OPTION_MODALIDAD, Metodo);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("¿Esta seguro que desea salir?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(SelectorActivity.this, IniciarSesionActivity.class);
                        intent.putExtra(Constantes.CERRO_SESION, true);
                        startActivity(intent);
                    }
                }).create().show();
    }
}