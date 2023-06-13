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

import com.react.Biometric.R;

public class RespuestaActivity extends AppCompatActivity {

    //BOTONES
    private Button btnTabCapturar;
    private Button btnTabInfo;
    private Button btnIniciarCaptura;
    private Button btnIniciarProceso;
    //LAYOUTS
    private LinearLayout TabInformacion;
    private LinearLayout TabEscaner;
    //CAMPOS DE TEXTO
    private TextView txt_estado;
    private TextView txt_documento;
    private TextView txt_identificacion;
    private TextView txt_nombres;
    private TextView txt_apellidos;
    private TextView txt_sexo;
    private TextView txt_fecha_nacimiento;
    private TextView txt_lugar_nacimiento;
    private TextView txt_nombre_madre;
    private TextView txt_nombre_padre;
    private TextView txt_vencimiento;
    //CAMPOS DE IMAGENES
    private ImageView img_foto;
    private ImageView img_firma;
    private ImageView img_documento_1;
    private ImageView img_documento_2;
    private ImageView img_huella_1;
    private ImageView img_huella_2;
    private ImageView img_huella_3;
    private ImageView img_huella_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);

        Toolbar toolbar = this.findViewById(R.id.toolbarMain);
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

        btnTabCapturar = this.findViewById(R.id.btnTabCapturar);
        btnTabInfo = this.findViewById(R.id.btnTabInfo);
        btnIniciarCaptura = this.findViewById(R.id.btnIniciarCaptura);
        btnIniciarProceso = this.findViewById(R.id.btnIniciarProceso);
        TabInformacion = this.findViewById(R.id.TabInformacion);
        TabEscaner = this.findViewById(R.id.TabEscaner);
        txt_estado = this.findViewById(R.id.txt_estado);
        txt_documento = this.findViewById(R.id.txt_documento);
        txt_identificacion = this.findViewById(R.id.txt_identificacion);
        txt_nombres = this.findViewById(R.id.txt_nombres);
        txt_apellidos = this.findViewById(R.id.txt_apellidos);
        txt_sexo = this.findViewById(R.id.txt_sexo);
        txt_fecha_nacimiento = this.findViewById(R.id.txt_fecha_nacimiento);
        txt_lugar_nacimiento = this.findViewById(R.id.txt_lugar_nacimiento);
        txt_nombre_madre = this.findViewById(R.id.txt_nombre_madre);
        txt_nombre_padre = this.findViewById(R.id.txt_nombre_padre);
        txt_vencimiento = this.findViewById(R.id.txt_vencimiento);
        img_foto = this.findViewById(R.id.img_foto);
        img_firma = this.findViewById(R.id.img_firma);
        img_documento_1 = this.findViewById(R.id.img_documento_1);
        img_documento_2 = this.findViewById(R.id.img_documento_2);
        img_huella_1 = this.findViewById(R.id.img_huella_1);
        img_huella_2 = this.findViewById(R.id.img_huella_2);
        img_huella_3 = this.findViewById(R.id.img_huella_3);
        img_huella_4 = this.findViewById(R.id.img_huella_4);

        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        TabInformacion.setVisibility(View.GONE);
        TabEscaner.setVisibility(View.VISIBLE);

        btnTabCapturar.setOnClickListener(view -> {
            btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
            btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
            btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
            btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
            TabInformacion.setVisibility(View.GONE);
            TabEscaner.setVisibility(View.VISIBLE);
        });

        btnTabInfo.setOnClickListener(view -> {
            btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
            btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
            btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
            btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
            TabInformacion.setVisibility(View.VISIBLE);
            TabEscaner.setVisibility(View.GONE);
        });

        btnIniciarCaptura.setOnClickListener(view -> {
            //LimpiarCampos();
            Toast toast = Toast.makeText(getApplicationContext(), "Funciona", Constantes.ToastDuration);
            toast.show();
        });

        btnIniciarProceso.setOnClickListener(view -> {

        });
    }

    private void LimpiarCampos()
    {
        txt_estado.setText(getString(R.string.document_pending));
        txt_documento.setText(getString(R.string.document_pending));
        txt_identificacion.setText(getString(R.string.document_pending));
        txt_nombres.setText(getString(R.string.document_pending));
        txt_apellidos.setText(getString(R.string.document_pending));
        txt_sexo.setText(getString(R.string.document_pending));
        txt_fecha_nacimiento.setText(getString(R.string.document_pending));
        txt_lugar_nacimiento.setText(getString(R.string.document_pending));
        txt_nombre_madre.setText(getString(R.string.document_pending));
        txt_nombre_padre.setText(getString(R.string.document_pending));
        txt_vencimiento.setText(getString(R.string.document_pending));

        img_foto.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        img_firma.setImageDrawable(getResources().getDrawable(R.drawable.writing));
        img_documento_1.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        img_documento_2.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        img_huella_1.setImageDrawable(getResources().getDrawable(R.drawable.finger));
        img_huella_2.setImageDrawable(getResources().getDrawable(R.drawable.finger));
        img_huella_3.setImageDrawable(getResources().getDrawable(R.drawable.finger));
        img_huella_4.setImageDrawable(getResources().getDrawable(R.drawable.finger));
    }
}