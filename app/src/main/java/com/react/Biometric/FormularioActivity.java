package com.react.Biometric;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FormularioActivity extends AppCompatActivity {

    private String userName;
    private String userPassword;
    private String userModalidad;
    private String formModalidad;
    private Button btnContinuar;

    private EditText txtIdentificacion;
    private EditText txtNombres;
    private EditText txtApellidos;
    private TextView lblNombres;
    private TextView lblApellidos;

    private String userNID;
    private String userNombres;
    private String userApellidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        userName = getIntent().getStringExtra(Constantes.USER_NAME);
        userPassword = getIntent().getStringExtra(Constantes.USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(Constantes.OPTION_MODALIDAD);
        formModalidad = getIntent().getStringExtra(Constantes.FORM_MODALIDAD);

        userNID = getIntent().getStringExtra(Constantes.USER_ID);
        userNombres = getIntent().getStringExtra(Constantes.USER_PNAMES);
        userApellidos = getIntent().getStringExtra(Constantes.USER_PLNAMES);

        txtIdentificacion = (EditText) findViewById(R.id.txtPlaceHolder);
        txtNombres = (EditText) findViewById(R.id.txtNombres);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        lblNombres = (TextView) findViewById(R.id.lblNombres);
        lblApellidos = (TextView) findViewById(R.id.lblApellidos);
        btnContinuar = (Button) findViewById(R.id.btnContinuar);

        if (userNID != null && !userNID.equals(""))
        {
            txtIdentificacion.setText(userNID);
            txtNombres.setText(userNombres);
            txtApellidos.setText(userApellidos);
        }

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        if (!userModalidad.equals(Constantes.MODALIDADENROLA))
        {
            toolbar.setTitle(getString(R.string.title_formulario_val));
            lblNombres.setVisibility(View.GONE);
            txtNombres.setVisibility(View.GONE);
            lblApellidos.setVisibility(View.GONE);
            txtApellidos.setVisibility(View.GONE);
        }
        else
        {
            toolbar.setTitle(getString(R.string.title_formulario));
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

        btnContinuar.setOnClickListener(view -> {

            userNID = txtIdentificacion.getText().toString();
            userNombres = txtNombres.getText().toString();
            userApellidos = txtApellidos.getText().toString();

            if (userNID.replace(" ","").length() > 0)
            {
                if (userNombres.replace(" ","").length() > 0 || !userModalidad.equals(Constantes.MODALIDADENROLA))
                {
                    if (userApellidos.replace(" ","").length() > 0 || !userModalidad.equals(Constantes.MODALIDADENROLA))
                    {
                        if (formModalidad.equals(Constantes.FORMFACIAL))
                        {
                            Intent intent = new Intent(this, MainFacial.class);
                            intent.putExtra(Constantes.USER_NAME, userName);
                            intent.putExtra(Constantes.USER_PASSWORD, userPassword);
                            intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
                            intent.putExtra(Constantes.USER_ID, userNID);
                            intent.putExtra(Constantes.USER_PNAMES, userNombres);
                            intent.putExtra(Constantes.USER_PLNAMES, userApellidos);
                            intent.putExtra(Constantes.FORM_MODALIDAD, formModalidad);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(this, MainHuella.class);
                            intent.putExtra(Constantes.USER_NAME, userName);
                            intent.putExtra(Constantes.USER_PASSWORD, userPassword);
                            intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
                            intent.putExtra(Constantes.USER_ID, userNID);
                            intent.putExtra(Constantes.USER_PNAMES, userNombres);
                            intent.putExtra(Constantes.USER_PLNAMES, userApellidos);
                            intent.putExtra(Constantes.FORM_MODALIDAD, formModalidad);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.form_error_apellidos), Constantes.ToastDuration);
                        toast.show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.form_error_nombres), Constantes.ToastDuration);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.form_error_identificacion), Constantes.ToastDuration);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(Constantes.USER_NAME, userName);
        intent.putExtra(Constantes.USER_PASSWORD, userPassword);
        intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
        startActivity(intent);
    }
}