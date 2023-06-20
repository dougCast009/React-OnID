package com.react.Biometric;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
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
    //LAYOUTS
    private LinearLayout tabInformacion;
    private LinearLayout tabEscaner;

    //VARIABLES DE PETICION
    private String peticionNID;
    private String peticionNombres;
    private String peticionApellidos;
    private String peticionSexo;
    private String peticionPais;
    private String peticionNacimiento;

    private LinearLayout mainContent;
    private LinearLayout tabRespuesta;
    private ImageView imgResultado;
    private TextView textoPrincipal;
    private Toolbar toolbar;
    private Boolean estadoDocumento = false;

    private String errorCode = "";
    private String message = "";

    //ANTERIORES
    private String userName;
    private String userPassword;
    private String userModalidad;
    private String metodo;
    private boolean captura;
    private String rostroCapturado;
    private AlertDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_facial);

        //Variables
        TextView txtContinuar;
        TextView txtNombres;
        TextView txtSexo;
        TextView txtFechaNacimiento;
        TextView txtIdentificacion;
        TextView txtApellidos;
        TextView txtCodPais;
        ImageView imgFoto;
        Button btnIniciarCaptura;
        Button btnIniciarProceso;

        btnTabCapturar = findViewById(R.id.btnTabCapturar);
        btnTabInfo = findViewById(R.id.btnTabInfo);
        btnIniciarCaptura = findViewById(R.id.btnIniciarCaptura);
        btnIniciarProceso = findViewById(R.id.btnIniciarProceso);
        tabInformacion = findViewById(R.id.TabInformacion);
        tabEscaner = findViewById(R.id.TabEscaner);
        txtIdentificacion = findViewById(R.id.txtIdentificacion);
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtSexo = findViewById(R.id.txtSexo);
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
        txtCodPais = findViewById(R.id.txtCodPais);
        imgFoto = findViewById(R.id.img_foto);

        userName = getIntent().getStringExtra(Constantes.USER_NAME);
        userPassword = getIntent().getStringExtra(Constantes.USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(Constantes.OPTION_MODALIDAD);
        peticionNID = getIntent().getStringExtra(Constantes.REQUEST_NID);
        peticionNombres = getIntent().getStringExtra(Constantes.REQUEST_NAME);
        peticionApellidos = getIntent().getStringExtra(Constantes.REQUEST_LAST_NAME);
        peticionSexo = getIntent().getStringExtra(Constantes.REQUEST_SEX);
        peticionPais = getIntent().getStringExtra(Constantes.REQUEST_COUNTRY);
        peticionNacimiento = getIntent().getStringExtra(Constantes.REQUEST_BIRTH);

        txtIdentificacion.setText(peticionNID);
        txtNombres.setText(peticionNombres);
        txtApellidos.setText(peticionApellidos);
        txtSexo.setText(peticionSexo);
        txtCodPais.setText(peticionPais);
        txtFechaNacimiento.setText(peticionNacimiento);

        mostrarTabCaptura();

        metodo = "99";
        captura = false;
        toolbar = findViewById(R.id.toolbarMain);
        if (userModalidad != null)
        {
            if (userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                metodo = Constantes.ENROLL_FACIAL;
                toolbar.setTitle(getString(R.string.title_rostro));
            }
            else
            {
                metodo = Constantes.VERI_FACIAL;
                toolbar.setTitle(getString(R.string.title_rostro_val));
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_modalidad), Constantes.TOASTDURATION);
            toast.show();
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        mainContent = findViewById(R.id.MainContent);
        tabRespuesta = findViewById(R.id.TabRespuesta);
        imgResultado = findViewById(R.id.img_resultado);
        txtContinuar = findViewById(R.id.txt_continuar);
        textoPrincipal = findViewById(R.id.texto_principal);

        btnIniciarCaptura.setOnClickListener(view -> {
            estadoDocumento = false;
            errorCode = "";
            message = "";
            captura = false;
            rostroCapturado = "";
            imgFoto.setImageResource(R.drawable.avatar);

            FaceSDK.Instance().startLiveness(this, livenessResponse -> {
                if (livenessResponse.getLiveness() == LivenessStatus.PASSED) {
                    captura = true;
                    rostroCapturado = this.getStringImage(livenessResponse.getBitmap());
                    imgFoto.setImageBitmap(livenessResponse.getBitmap());
                    mostrarTabInformacion();
                }
            });
        });

        btnIniciarProceso.setOnClickListener(view -> {
            if (captura)
            {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setTitle(getString(R.string.consentimiento_title));
                dlgAlert.setMessage(getString(R.string.consentimiento));
                dlgAlert.setCancelable(false);
                dlgAlert.setNegativeButton(getString(R.string.rechazar), null);
                dlgAlert.setPositiveButton(getString(R.string.aceptar), (dialog, whichButton) -> crearPeticion());
                dlgAlert.create().show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_face), Constantes.TOASTDURATION);
                toast.show();
            }
        });

        btnTabCapturar.setOnClickListener(view -> mostrarTabCaptura());

        btnTabInfo.setOnClickListener(view -> mostrarTabInformacion());

        txtContinuar.setOnClickListener(view -> {
            tabRespuesta.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.VISIBLE);

            if (Boolean.TRUE.equals(estadoDocumento))
            {
                String titulo = getString(R.string.matched);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainFacial.this);
                LayoutInflater inflater = getLayoutInflater();
                final View myView = inflater.inflate(R.layout.activity_main_orquestador, null);
                ImageView imgPrincipal = myView.findViewById(R.id.imagen);
                imgPrincipal.setImageResource(R.drawable.matched_size);
                TextView txtTitulo = myView.findViewById(R.id.texto_principal);
                txtTitulo.setText(titulo);
                TextView txtMensaje = myView.findViewById(R.id.texto);
                txtMensaje.setText(message);

                builder.setView(myView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.aceptar, (dialog, whichButton) -> {
                        });
                builder.create().show();
            }
            else
            {
                String titulo = getString(R.string.not_matched);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainFacial.this);
                LayoutInflater inflater = getLayoutInflater();
                final View myView = inflater.inflate(R.layout.activity_main_orquestador, null);
                ImageView imgPrincipal = myView.findViewById(R.id.imagen);
                imgPrincipal.setImageResource(R.drawable.not_matched_size);
                TextView txtTitulo = myView.findViewById(R.id.texto_principal);
                txtTitulo.setText(titulo);
                TextView txtMensaje = myView.findViewById(R.id.texto);
                txtMensaje.setText(message);

                builder.setView(myView)
                        .setCancelable(false)
                        .setPositiveButton(R.string.aceptar, (dialog, whichButton) -> {
                        });
                builder.create().show();
            }
        });
    }

    private void mostrarTabCaptura()
    {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        tabInformacion.setVisibility(View.GONE);
        tabEscaner.setVisibility(View.VISIBLE);
    }

    private void mostrarTabInformacion()
    {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        tabInformacion.setVisibility(View.VISIBLE);
        tabEscaner.setVisibility(View.GONE);
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
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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

    private void crearPeticion()
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
            request.setMethodAuth(metodo);
            request.setNID(peticionNID);

            if (Boolean.TRUE.equals(Constantes.ESDESARROLLO))
            {
                request.setCUSTOMERID("xpi");
                request.setPASS("$tr@!ght1928");
            }
            else
            {
                request.setCUSTOMERID(userName);
                request.setPASS(userPassword);
            }

            List<Biometria> biometrics = new ArrayList<>();
            Biometria rostro = new Biometria();
            rostro.setBiometryName("Face");
            rostro.setBiometryRawDataType("Jpeg");
            rostro.setRawData(rostroCapturado);
            biometrics.add(rostro);
            request.setBiometrics(biometrics);

            if (metodo.equals("30"))
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
            JsonObject jsonRequest = JsonParser.parseString(gson.toJson(request)).getAsJsonObject();
            realizarPeticion(jsonRequest);
        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_peticion), Constantes.TOASTDURATION);
            toast.show();
        }
    }

    private void realizarPeticion(JsonObject request)
    {
        try
        {
            InputStream privateCrt = getResources().openRawResource(R.raw.certificado_android_pfx);
            InputStream certChain = getResources().openRawResource(R.raw.certificado_android_pem);
            final HttpsPostRequest peticion = new HttpsPostRequest(request, this, privateCrt, certChain);
            peticion.execute(Constantes.URL_BASE);
        }
        catch (Exception ex)
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_realiza_peticion), Constantes.TOASTDURATION);
            toast.show();
        }
    }

    @Override
    public void obtenerRespuesta(Boolean success, String object) {
        runOnUiThread(this::dismissDialog);

        try {
            OrqResponse respuesta = ResponseManager.obtenerObjetoRespuesta(object);

            if (respuesta != null)
            {
                errorCode = respuesta.ObtenerInfoPersonaResult.ErrorCode;
                message = respuesta.ObtenerInfoPersonaResult.Message;

                if (errorCode.equals("00"))
                {
                    runOnUiThread(() -> {
                        estadoDocumento = true;
                        tabRespuesta.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.GONE);
                        mainContent.setVisibility(View.GONE);
                        textoPrincipal.setText(Boolean.TRUE.equals(estadoDocumento) ? getString(R.string.identidad_confirmada) : getString(R.string.identidad_no_confirmada));
                        imgResultado.setImageResource(Boolean.TRUE.equals(estadoDocumento) ? R.drawable.document_check : R.drawable.document_cross);
                    });
                }
                else
                {
                    runOnUiThread(() -> {
                        estadoDocumento = false;
                        tabRespuesta.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.GONE);
                        mainContent.setVisibility(View.GONE);
                        textoPrincipal.setText(Boolean.TRUE.equals(estadoDocumento) ? getString(R.string.identidad_confirmada) : getString(R.string.identidad_no_confirmada));
                        imgResultado.setImageResource(Boolean.TRUE.equals(estadoDocumento) ? R.drawable.document_check : R.drawable.document_cross);
                    });
                }
            }
            else
            {
                runOnUiThread(() -> {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_realiza_peticion), Constantes.TOASTDURATION);
                    toast.show();
                });
            }

        } catch (Exception e) {
            runOnUiThread(() -> {
                Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Constantes.TOASTDURATION);
                toast.show();
            });

        }
    }
}