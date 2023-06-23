package com.react.biometric;

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

import com.regula.documentreader.api.params.rfid.authorization.PAResourcesIssuer;

public class FormularioActivity extends AppCompatActivity {

    private String userName;
    private String userPassword;
    private String userModalidad;
    private String userNID;
    private String userNombres;
    private String userApellidos;
    private EditText txtIdentificacion;
    private EditText txtNombres;
    private EditText txtApellidos;



    private void changeByFacialOrHuella(String formModalidad){
        Intent intent;
        if (formModalidad.equals(Constantes.FORMFACIAL))
        {
            intent = new Intent(this, MainFacial.class);
        }
        else
        {
            intent = new Intent(this, MainHuella.class);
        }
        intent.putExtra(Constantes.USER_NAME, userName);
        intent.putExtra(Constantes.USER_PASSWORD, userPassword);
        intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
        intent.putExtra(Constantes.USER_ID, userNID);
        intent.putExtra(Constantes.USER_PNAMES, userNombres);
        intent.putExtra(Constantes.USER_PLNAMES, userApellidos);
        intent.putExtra(Constantes.FORM_MODALIDAD, formModalidad);
        startActivity(intent);

    }
    private void configBtnContinuar(String formModalidad){

        userNID = txtIdentificacion.getText().toString();
        userNombres = txtNombres.getText().toString();
        userApellidos = txtApellidos.getText().toString();

        if (userNID.replace(" ","").length() > 0)
        {
            if (userNombres.replace(" ","").length() > 0 || !userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                if (userApellidos.replace(" ","").length() > 0 || !userModalidad.equals(Constantes.MODALIDADENROLA))
                {
                    changeByFacialOrHuella(formModalidad);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.form_error_apellidos), Constantes.TOASTDURATION);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.form_error_nombres), Constantes.TOASTDURATION);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.form_error_identificacion), Constantes.TOASTDURATION);
            toast.show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        String formModalidad;
        Button btnContinuar;

        userName = getIntent().getStringExtra(Constantes.USER_NAME);
        userPassword = getIntent().getStringExtra(Constantes.USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(Constantes.OPTION_MODALIDAD);
        formModalidad = getIntent().getStringExtra(Constantes.FORM_MODALIDAD);

        userNID = getIntent().getStringExtra(Constantes.USER_ID);
        userNombres = getIntent().getStringExtra(Constantes.USER_PNAMES);
        userApellidos = getIntent().getStringExtra(Constantes.USER_PLNAMES);

        txtIdentificacion = findViewById(R.id.txtPlaceHolder);
        txtNombres =  findViewById(R.id.txtNombres);
        txtApellidos =  findViewById(R.id.txtApellidos);
        TextView  lblNombres =  findViewById(R.id.lblNombres);
        TextView lblApellidos =  findViewById(R.id.lblApellidos);
        btnContinuar =  findViewById(R.id.btnContinuar);

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

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        btnContinuar.setOnClickListener(view -> configBtnContinuar(formModalidad));
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