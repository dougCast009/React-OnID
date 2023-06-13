package com.react.Biometric;

import static com.react.Biometric.Constantes.FORM_MODALIDAD;
import static com.react.Biometric.Constantes.OPTION_MODALIDAD;
import static com.react.Biometric.Constantes.USER_NAME;
import static com.react.Biometric.Constantes.USER_PASSWORD;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PrincipalActivity extends AppCompatActivity {

    private String userName;
    private String userPassword;
    private String userModalidad;

    private Button btnDocument;
    private Button btnFace;
    private Button btnFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        userName = getIntent().getStringExtra(USER_NAME);
        userPassword = getIntent().getStringExtra(USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(OPTION_MODALIDAD);

        btnDocument = (Button) findViewById(R.id.btnDocumento);
        btnFace = (Button) findViewById(R.id.btnFacial);
        btnFinger = (Button) findViewById(R.id.btnHuella);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        if (userModalidad.equals(Constantes.ModalidadEnrola))
        {
            toolbar.setTitle(getString(R.string.title_principal));
            btnDocument.setText(R.string.principal_btn_enroll_doc);
            btnFace.setText(R.string.principal_btn_enroll_face);
            btnFinger.setText(R.string.principal_btn_enroll_finger);
        }
        else
        {
            toolbar.setTitle(getString(R.string.title_principal_val));
            btnDocument.setText(R.string.principal_btn_verify_doc);
            btnFace.setText(R.string.principal_btn_verify_face);
            btnFinger.setText(R.string.principal_btn_verify_finger);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDocument.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainDocumento.class);
            intent.putExtra(USER_NAME, userName);
            intent.putExtra(USER_PASSWORD, userPassword);
            intent.putExtra(OPTION_MODALIDAD, userModalidad);
            intent.putExtra(FORM_MODALIDAD, Constantes.FormDocumento);
            startActivity(intent);
        });

        btnFace.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainDocumento.class);
            intent.putExtra(USER_NAME, userName);
            intent.putExtra(USER_PASSWORD, userPassword);
            intent.putExtra(OPTION_MODALIDAD, userModalidad);
            intent.putExtra(FORM_MODALIDAD, Constantes.FormFacial);
            startActivity(intent);
        });

        btnFinger.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainDocumento.class);
            intent.putExtra(USER_NAME, userName);
            intent.putExtra(USER_PASSWORD, userPassword);
            intent.putExtra(OPTION_MODALIDAD, userModalidad);
            intent.putExtra(FORM_MODALIDAD, Constantes.FormHuella);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectorActivity.class);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(USER_PASSWORD, userPassword);
        startActivity(intent);
    }
}