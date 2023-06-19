package com.react.Biometric;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RespuestaActivity extends AppCompatActivity {



    //LAYOUTS


    //CAMPOS DE TEXTO
    private TextView txtEstado;
    private TextView txtDocumento;
    private TextView txtIdentificacion;
    private TextView txtNombres;
    private TextView txtApellidos;
    private TextView txtSexo;
    private TextView txtFechaNacimiento;
    private TextView txtLugarNacimiento;
    private TextView txtNombreMadre;
    private TextView txtNombrePadre;
    private TextView txtVencimiento;
    //CAMPOS DE IMAGENES
    private ImageView imgFoto;
    private ImageView imgFirma;
    private ImageView imgDocumento1;
    private ImageView imgDocumento2;
    private ImageView imgHuella1;
    private ImageView imgHuella2;
    private ImageView imgHuella3;
    private ImageView imgHuella4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);

        Toolbar toolbar = this.findViewById(R.id.toolbarMain);
        toolbar.setTitle(getString(R.string.title_selector));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        Button btnTabCapturar = this.findViewById(R.id.btnTabCapturar);
        Button btnTabInfo = this.findViewById(R.id.btnTabInfo);
        Button btnIniciarCaptura = this.findViewById(R.id.btnIniciarCaptura);
        Button btnIniciarProceso = this.findViewById(R.id.btnIniciarProceso);
        LinearLayout tabInformacion = this.findViewById(R.id.TabInformacion);
        LinearLayout tabEscaner = this.findViewById(R.id.TabEscaner);
        txtEstado = this.findViewById(R.id.txtEstado);
        txtDocumento = this.findViewById(R.id.txtDocumento);
        txtIdentificacion = this.findViewById(R.id.txtIdentificacion);
        txtNombres = this.findViewById(R.id.txtNombres);
        txtApellidos = this.findViewById(R.id.txtApellidos);
        txtSexo = this.findViewById(R.id.txtSexo);
        txtFechaNacimiento = this.findViewById(R.id.txtFechaNacimiento);
        txtLugarNacimiento = this.findViewById(R.id.txtLugarNacimiento);
        txtNombreMadre = this.findViewById(R.id.txtNombreMadre);
        txtNombrePadre = this.findViewById(R.id.txtNombrePadre);
        txtVencimiento = this.findViewById(R.id.txtVencimiento);
        imgFoto = this.findViewById(R.id.img_foto);
        imgFirma = this.findViewById(R.id.img_firma);
        imgDocumento1 = this.findViewById(R.id.img_documento_1);
        imgDocumento2 = this.findViewById(R.id.img_documento_2);
        imgHuella1 = this.findViewById(R.id.img_huella_1);
        imgHuella2 = this.findViewById(R.id.img_huella_2);
        imgHuella3 = this.findViewById(R.id.img_huella_3);
        imgHuella4 = this.findViewById(R.id.img_huella_4);

        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        tabInformacion.setVisibility(View.GONE);
        tabEscaner.setVisibility(View.VISIBLE);

        btnTabCapturar.setOnClickListener(view -> {
            btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
            btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
            btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
            btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
            tabInformacion.setVisibility(View.GONE);
            tabEscaner.setVisibility(View.VISIBLE);
        });

        btnTabInfo.setOnClickListener(view -> {
            btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
            btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
            btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
            btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
            tabInformacion.setVisibility(View.VISIBLE);
            tabEscaner.setVisibility(View.GONE);
        });

        btnIniciarCaptura.setOnClickListener(view -> {
            //LimpiarCampos();
            Toast toast = Toast.makeText(getApplicationContext(), "Funciona", Constantes.TOASTDURATION);
            toast.show();
        });

        btnIniciarProceso.setOnClickListener(view -> {

        });
    }

    private void LimpiarCampos()
    {
        txtEstado.setText(getString(R.string.document_pending));
        txtDocumento.setText(getString(R.string.document_pending));
        txtIdentificacion.setText(getString(R.string.document_pending));
        txtNombres.setText(getString(R.string.document_pending));
        txtApellidos.setText(getString(R.string.document_pending));
        txtSexo.setText(getString(R.string.document_pending));
        txtFechaNacimiento.setText(getString(R.string.document_pending));
        txtLugarNacimiento.setText(getString(R.string.document_pending));
        txtNombreMadre.setText(getString(R.string.document_pending));
        txtNombrePadre.setText(getString(R.string.document_pending));
        txtVencimiento.setText(getString(R.string.document_pending));

        imgFoto.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        imgFirma.setImageDrawable(getResources().getDrawable(R.drawable.writing));
        imgDocumento1.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        imgDocumento2.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        imgHuella1.setImageDrawable(getResources().getDrawable(R.drawable.finger));
        imgHuella2.setImageDrawable(getResources().getDrawable(R.drawable.finger));
        imgHuella3.setImageDrawable(getResources().getDrawable(R.drawable.finger));
        imgHuella4.setImageDrawable(getResources().getDrawable(R.drawable.finger));
    }
}