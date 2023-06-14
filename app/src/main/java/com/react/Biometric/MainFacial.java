package com.react.Biometric;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.react.Biometric.interfaces.CustomCallback;
import com.react.Biometric.orquestador.Biometria;
import com.react.Biometric.orquestador.OrqResponse;
import com.react.Biometric.orquestador.Peticion;
import com.react.Biometric.utilidades.BaseActivity;
import com.react.Biometric.utilidades.HttpsPostRequest;
import com.react.Biometric.utilidades.ResponseManager;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.enums.LivenessStatus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainFacial extends BaseActivity implements CustomCallback
{
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
    private TextView txt_domicilio;
    private TextView txt_nombre_madre;
    private TextView txt_nombre_padre;
    private TextView txt_vencimiento;
    private TextView txt_cod_pais;
    //CAMPOS DE IMAGENES
    private ImageView img_foto;
    private ImageView img_firma;
    private ImageView img_documento_1;
    private ImageView img_documento_2;
    //VARIABLES DE PETICION
    private String peticionNID;
    private String peticionNombres;
    private String peticionApellidos;
    private String peticionSexo;
    private String peticionPais;
    private String peticionNacimiento;
    private String peticionDoc;
    private String peticionFirma;
    private String peticionFoto;

    private LinearLayout MainContent;
    private LinearLayout TabRespuesta;
    private ImageView img_resultado;
    private TextView txt_continuar;
    private TextView texto_principal;
    private Toolbar toolbar;
    private Boolean EstadoDocumento = false;

    private String ErrorCode = "";
    private String Message = "";

    //ANTERIORES
    private String userName;
    private String userPassword;
    private String userModalidad;
    private String Metodo;
    private boolean Captura;
    private String RostroCapturado;
    private AlertDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_facial);

        btnTabCapturar = findViewById(R.id.btnTabCapturar);
        btnTabInfo = findViewById(R.id.btnTabInfo);
        btnIniciarCaptura = findViewById(R.id.btnIniciarCaptura);
        btnIniciarProceso = findViewById(R.id.btnIniciarProceso);
        TabInformacion = findViewById(R.id.TabInformacion);
        TabEscaner = findViewById(R.id.TabEscaner);
        txt_estado = findViewById(R.id.txt_estado);
        txt_documento = findViewById(R.id.txt_documento);
        txt_identificacion = findViewById(R.id.txt_identificacion);
        txt_nombres = findViewById(R.id.txt_nombres);
        txt_apellidos = findViewById(R.id.txt_apellidos);
        txt_sexo = findViewById(R.id.txt_sexo);
        txt_fecha_nacimiento = findViewById(R.id.txt_fecha_nacimiento);
        txt_lugar_nacimiento = findViewById(R.id.txt_lugar_nacimiento);
        txt_domicilio = findViewById(R.id.txt_domicilio);
        txt_nombre_madre = findViewById(R.id.txt_nombre_madre);
        txt_nombre_padre = findViewById(R.id.txt_nombre_padre);
        txt_vencimiento = findViewById(R.id.txt_vencimiento);
        txt_cod_pais = findViewById(R.id.txt_cod_pais);
        img_foto = findViewById(R.id.img_foto);
        img_firma = findViewById(R.id.img_firma);
        img_documento_1 = findViewById(R.id.img_documento_1);
        img_documento_2 = findViewById(R.id.img_documento_2);

        userName = getIntent().getStringExtra(Constantes.USER_NAME);
        userPassword = getIntent().getStringExtra(Constantes.USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(Constantes.OPTION_MODALIDAD);
        peticionNID = getIntent().getStringExtra(Constantes.REQUEST_NID);
        peticionNombres = getIntent().getStringExtra(Constantes.REQUEST_NAME);
        peticionApellidos = getIntent().getStringExtra(Constantes.REQUEST_LAST_NAME);
        peticionSexo = getIntent().getStringExtra(Constantes.REQUEST_SEX);
        peticionPais = getIntent().getStringExtra(Constantes.REQUEST_COUNTRY);
        peticionNacimiento = getIntent().getStringExtra(Constantes.REQUEST_BIRTH);

        txt_identificacion.setText(peticionNID);
        txt_nombres.setText(peticionNombres);
        txt_apellidos.setText(peticionApellidos);
        txt_sexo.setText(peticionSexo);
        txt_cod_pais.setText(peticionPais);
        txt_fecha_nacimiento.setText(peticionNacimiento);

        MostrarTabCaptura();

        Metodo = "99";
        Captura = false;
        toolbar = findViewById(R.id.toolbarMain);
        if (userModalidad != null)
        {
            if (userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                Metodo = Constantes.enroll_facial;
                toolbar.setTitle(getString(R.string.title_rostro));
            }
            else
            {
                Metodo = Constantes.veri_facial;
                toolbar.setTitle(getString(R.string.title_rostro_val));
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_modalidad), Constantes.ToastDuration);
            toast.show();
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

        MainContent = findViewById(R.id.MainContent);
        TabRespuesta = findViewById(R.id.TabRespuesta);
        img_resultado = findViewById(R.id.img_resultado);
        txt_continuar = findViewById(R.id.txt_continuar);
        texto_principal = findViewById(R.id.texto_principal);

        btnIniciarCaptura.setOnClickListener(view -> {
            EstadoDocumento = false;
            ErrorCode = "";
            Message = "";
            Captura = false;
            RostroCapturado = "";
            img_foto.setImageResource(R.drawable.avatar);

            FaceSDK.Instance().startLiveness(this, livenessResponse -> {
                if (livenessResponse.getLiveness() == LivenessStatus.PASSED) {
                    Captura = true;
                    RostroCapturado = this.getStringImage(livenessResponse.getBitmap());
                    img_foto.setImageBitmap(livenessResponse.getBitmap());
                    MostrarTabInformacion();
                }
            });
        });

        btnIniciarProceso.setOnClickListener(view -> {
            if (Captura)
            {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setTitle(getString(R.string.consentimiento_title));
                dlgAlert.setMessage(getString(R.string.consentimiento));
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton(getString(R.string.rechazar), null);
                dlgAlert.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        CrearPeticion();
                    }
                });
                dlgAlert.create().show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_face), Constantes.ToastDuration);
                toast.show();
            }
        });

        btnTabCapturar.setOnClickListener(view -> {
            MostrarTabCaptura();
        });

        btnTabInfo.setOnClickListener(view -> {
            MostrarTabInformacion();
        });

        txt_continuar.setOnClickListener(view -> {
            TabRespuesta.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            MainContent.setVisibility(View.VISIBLE);

            if (EstadoDocumento)
            {
                String Titulo = getString(R.string.matched);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainFacial.this);
                LayoutInflater inflater = getLayoutInflater();
                final View myView = inflater.inflate(R.layout.activity_main_orquestador, null);
                ImageView imgPrincipal = myView.findViewById(R.id.imagen);
                imgPrincipal.setImageResource(R.drawable.matched_size);
                TextView txtTitulo = myView.findViewById(R.id.texto_principal);
                txtTitulo.setText(Titulo);
                TextView txtMensaje = myView.findViewById(R.id.texto);
                txtMensaje.setText(Message);

                builder.setView(myView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                builder.create().show();
            }
            else
            {
                String Titulo = getString(R.string.not_matched);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainFacial.this);
                LayoutInflater inflater = getLayoutInflater();
                final View myView = inflater.inflate(R.layout.activity_main_orquestador, null);
                ImageView imgPrincipal = myView.findViewById(R.id.imagen);
                imgPrincipal.setImageResource(R.drawable.not_matched_size);
                TextView txtTitulo = myView.findViewById(R.id.texto_principal);
                txtTitulo.setText(Titulo);
                TextView txtMensaje = myView.findViewById(R.id.texto);
                txtMensaje.setText(Message);

                builder.setView(myView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void MostrarTabCaptura()
    {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        TabInformacion.setVisibility(View.GONE);
        TabEscaner.setVisibility(View.VISIBLE);
    }

    private void MostrarTabInformacion()
    {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        TabInformacion.setVisibility(View.VISIBLE);
        TabEscaner.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(Constantes.USER_NAME, userName);
        intent.putExtra(Constantes.USER_PASSWORD, userPassword);
        intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
        startActivity(intent);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    protected void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void showDialog(String msg) {
        dismissDialog();
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.simple_dialog, null);
        builderDialog.setTitle(msg);
        builderDialog.setView(dialogView);
        builderDialog.setCancelable(false);
        loadingDialog = builderDialog.show();
    }

    private void CrearPeticion()
    {
        try
        {
            showDialog("Procesando");

            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Peticion request = new Peticion();
            request.setMethodAuth(Metodo);
            request.setNID(peticionNID);

            if (Constantes.EsDesarrollo)
            {
                request.setCUSTOMERID("xpi");
                request.setPASS("$tr@!ght1928");
            }
            else
            {
                request.setCUSTOMERID(userName);
                request.setPASS(userPassword);
            }

            List<Biometria> Biometrics = new ArrayList<>();
            Biometria rostro = new Biometria();
            rostro.setBiometryName("Face");
            rostro.setBiometryRawDataType("Jpeg");
            rostro.setRawData(RostroCapturado);
            Biometrics.add(rostro);
            request.setBiometrics(Biometrics);

            if (Metodo == "30")
            {
                request.setNombres(peticionNombres);
                request.setApellidos(peticionApellidos);
                if (peticionSexo.equals("M") || peticionSexo.equals("F"))
                {
                    request.setSexo(peticionSexo);
                }
                request.setNacionalidad(peticionPais);
                request.setFechaNacimiento(peticionNacimiento);
            }

            Gson gson = new Gson();
            JsonObject JsonRequest = JsonParser.parseString(gson.toJson(request)).getAsJsonObject();
            RealizarPeticion(JsonRequest);
        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_peticion), Constantes.ToastDuration);
            toast.show();
        }
    }

    private void RealizarPeticion(JsonObject Request)
    {
        try
        {
            InputStream privateCrt = getResources().openRawResource(R.raw.certificado_android_pfx);
            InputStream certChain = getResources().openRawResource(R.raw.certificado_android_pem);
            final HttpsPostRequest peticion = new HttpsPostRequest(Request, this, privateCrt, certChain);
            peticion.execute(Constantes.URL_BASE);
        }
        catch (Exception ex)
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_realiza_peticion), Constantes.ToastDuration);
            toast.show();
        }
    }

    @Override
    public void ObtenerRespuesta(Boolean success, String object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
            }
        });

        try {
            OrqResponse Respuesta = ResponseManager.ObtenerObjetoRespuesta(object);

            if (Respuesta != null)
            {
                ErrorCode = Respuesta.ObtenerInfoPersonaResult.ErrorCode;
                Message = Respuesta.ObtenerInfoPersonaResult.Message;

                if (ErrorCode.equals("00"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EstadoDocumento = true;
                            TabRespuesta.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.GONE);
                            MainContent.setVisibility(View.GONE);
                            texto_principal.setText(EstadoDocumento ? getString(R.string.identidad_confirmada) : getString(R.string.identidad_no_confirmada));
                            img_resultado.setImageResource(EstadoDocumento ? R.drawable.document_check : R.drawable.document_cross);
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EstadoDocumento = false;
                            TabRespuesta.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.GONE);
                            MainContent.setVisibility(View.GONE);
                            texto_principal.setText(EstadoDocumento ? getString(R.string.identidad_confirmada) : getString(R.string.identidad_no_confirmada));
                            img_resultado.setImageResource(EstadoDocumento ? R.drawable.document_check : R.drawable.document_cross);
                        }
                    });
                }
            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_realiza_peticion), Constantes.ToastDuration);
                        toast.show();
                    }
                });
            }

        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Constantes.ToastDuration);
                    toast.show();
                }
            });
        }
    }
}