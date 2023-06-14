package com.react.Biometric;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.identy.Attempt;
import com.identy.IdentyError;
import com.identy.IdentyResponse;
import com.identy.IdentyResponseListener;
import com.identy.IdentySdk;
import com.identy.InitializationListener;
import com.identy.TemplateSize;
import com.identy.WSQCompression;
import com.identy.enums.Finger;
import com.identy.enums.FingerDetectionMode;
import com.identy.enums.Hand;
import com.identy.enums.Template;
import com.react.Biometric.interfaces.CustomCallback;
import com.react.Biometric.models.ResponseIdenty;
import com.react.Biometric.orquestador.Biometria;
import com.react.Biometric.orquestador.OrqResponse;
import com.react.Biometric.orquestador.Peticion;
import com.react.Biometric.utilidades.BaseActivity;
import com.react.Biometric.utilidades.HttpsPostRequest;
import com.react.Biometric.utilidades.ResponseManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainHuella extends BaseActivity implements CustomCallback
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

    //ANTERIORES
    private FingerDetectionMode[] detectionModes;
    String mode = "commercial";
    WSQCompression compression = WSQCompression.WSQ_10_1;
    int base64encoding = Base64.DEFAULT;
    static String NET_KEY = "AIzaSyDFFGSONyHF0Aa7ikelLSyXw_CIa0PGVdk";
    //private Button btnIniciarCaptura;
    //private Button btnProcesar;
    //private ImageView photoImageIv;
    private ImageView imgIndice;
    private ImageView imgMedio;
    private ImageView imgAnular;
    private ImageView imgMenique;
    private String userName;
    private String userPassword;
    private String userModalidad;
    private String userNID;
    private String userNombres;
    private String userApellidos;
    private String formModalidad;
    private String Metodo;
    private boolean Captura;
    private String RostroCapturado;
    private AlertDialog loadingDialog;
    String HuellaPNG = "";
    String HuellaWSQ = "";
    String ManoIndice = "";
    String PNG_Indce = "";
    String PNG_Medio = "";
    String PNG_Anular = "";
    String PNG_Menique = "";
    String WSQ_Indce = "";
    String WSQ_Medio = "";
    String WSQ_Anular = "";
    String WSQ_Menique = "";

    private LinearLayout MainContent;
    private LinearLayout TabRespuesta;
    private ImageView img_resultado;
    private TextView txt_continuar;
    private TextView texto_principal;
    private Toolbar toolbar;

    private String ErrorCode = "";
    private String Message = "";
    private Boolean EstadoDocumento = false;
    private Boolean Capturando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_huella);

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
        imgIndice = findViewById(R.id.img_huella_1);
        imgMedio = findViewById(R.id.img_huella_2);
        imgAnular = findViewById(R.id.img_huella_3);
        imgMenique = findViewById(R.id.img_huella_4);

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

        Captura = false;
        Metodo = "99";
        toolbar = findViewById(R.id.toolbarMain);
        MainContent = findViewById(R.id.MainContent);
        TabRespuesta = findViewById(R.id.TabRespuesta);
        img_resultado = findViewById(R.id.img_resultado);
        txt_continuar = findViewById(R.id.txt_continuar);
        texto_principal = findViewById(R.id.texto_principal);
        if (userModalidad != null)
        {
            if (userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                Metodo = Constantes.ENROLL_HUELLA;
                toolbar.setTitle(getString(R.string.title_huellas));
            }
            else
            {
                Metodo = Constantes.VERI_HUELLA;
                toolbar.setTitle(getString(R.string.title_huellas_val));
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

        btnIniciarCaptura.setOnClickListener(view -> {
            if (!Capturando)
            {
                Captura = false;
                RostroCapturado = "";
                EstadoDocumento = false;
                ErrorCode = "";
                Message = "";
                imgIndice.setImageResource(R.drawable.finger);
                imgMedio.setImageResource(R.drawable.finger);
                imgAnular.setImageResource(R.drawable.finger);
                imgMenique.setImageResource(R.drawable.finger);
                Capturando = true;
                EjecutarCapturaHuella();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.finger_starting), Constantes.ToastDuration);
                toast.show();
            }
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
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.finger_error_face), Constantes.ToastDuration);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainHuella.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainHuella.this);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(Constantes.USER_NAME, userName);
        intent.putExtra(Constantes.USER_PASSWORD, userPassword);
        intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
        startActivity(intent);
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

    private void EjecutarCapturaHuella()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS}, 1);
            }
        }

        String licenseFile = "1944-com.react.Biometric-09-11-2022.lic";
        detectionModes = new FingerDetectionMode[]{FingerDetectionMode.L4F};

        try
        {
            final HashMap<Template, HashMap<Finger, ArrayList<TemplateSize>>> requiredtemplates =  new HashMap<>();
            HashMap<Finger, ArrayList<TemplateSize>> fingerToGetTemplatesFor = new HashMap<>();
            ArrayList<TemplateSize> templateSizes = new ArrayList<>();
            Collections.addAll(templateSizes, TemplateSize.DEFAULT);
            fingerToGetTemplatesFor.put(Finger.INDEX, templateSizes);
            fingerToGetTemplatesFor.put(Finger.MIDDLE, templateSizes);
            fingerToGetTemplatesFor.put(Finger.RING, templateSizes);
            fingerToGetTemplatesFor.put(Finger.LITTLE, templateSizes);
            requiredtemplates.put(Template.PNG, fingerToGetTemplatesFor);
            requiredtemplates.put(Template.WSQ, fingerToGetTemplatesFor);

            final boolean showprogressdialog = true;
            Captura = false;

            IdentySdk.newInstance(
                    MainHuella.this,
                    licenseFile,
                    new InitializationListener<IdentySdk>() {
                        @Override
                        public void onInit(IdentySdk identySdk) {
                            try
                            {
                                identySdk.setBase64EncodingFlag(base64encoding)
                                         .setDisplayImages(false)
                                         .setMode(mode)
                                         .setAS(false)
                                         .setDisplayBoxes(true)
                                         .setRequiredTemplates(requiredtemplates)
                                         .displayImages(false)
                                         .setWSQCompression(compression)
                                         .setDetectionMode(detectionModes)
                                         .capture();
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.e("ErrorControlado", ex.getMessage());
                            }
                        }
                    },
                    new IdentyResponseListener() {
                        @Override
                        public void onAttempt(Hand hand, int i, Map<Finger, Attempt> map) {
                            Capturando = false;
                            ManoIndice = hand.toString();
                        }

                        @Override
                        public void onResponse(IdentyResponse identyResponse, HashSet<String> hashSet) {
                            try
                            {
                                Gson gson = new Gson();
                                ResponseIdenty resp = gson.fromJson(identyResponse.toJson(MainHuella.this).toString(), ResponseIdenty.class);

                                if (ManoIndice.equals("right"))
                                {
                                    WSQ_Indce = resp.data.rightindex.templates.WSQ.DEFAULT;
                                    WSQ_Medio = resp.data.rightmiddle.templates.WSQ.DEFAULT;
                                    WSQ_Anular = resp.data.rightring.templates.WSQ.DEFAULT;
                                    WSQ_Menique = resp.data.rightlittle.templates.WSQ.DEFAULT;

                                    PNG_Indce = resp.data.rightindex.templates.PNG.DEFAULT;
                                    PNG_Medio = resp.data.rightmiddle.templates.PNG.DEFAULT;
                                    PNG_Anular = resp.data.rightring.templates.PNG.DEFAULT;
                                    PNG_Menique = resp.data.rightlittle.templates.PNG.DEFAULT;
                                }
                                else
                                {
                                    WSQ_Indce = resp.data.leftindex.templates.WSQ.DEFAULT;
                                    WSQ_Medio = resp.data.leftmiddle.templates.WSQ.DEFAULT;
                                    WSQ_Anular = resp.data.leftring.templates.WSQ.DEFAULT;
                                    WSQ_Menique = resp.data.leftlittle.templates.WSQ.DEFAULT;

                                    PNG_Indce = resp.data.leftindex.templates.PNG.DEFAULT;
                                    PNG_Medio = resp.data.leftmiddle.templates.PNG.DEFAULT;
                                    PNG_Anular = resp.data.leftring.templates.PNG.DEFAULT;
                                    PNG_Menique = resp.data.leftlittle.templates.PNG.DEFAULT;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try
                                        {
                                            imgIndice.setImageBitmap(convertBase64ToBitmap(PNG_Indce));
                                            imgMedio.setImageBitmap(convertBase64ToBitmap(PNG_Medio));
                                            imgAnular.setImageBitmap(convertBase64ToBitmap(PNG_Anular));
                                            imgMenique.setImageBitmap(convertBase64ToBitmap(PNG_Menique));
                                            Captura = true;
                                            MostrarTabInformacion();
                                        }
                                        catch (Exception ex)
                                        {
                                            String Error = ex.getMessage();
                                        }
                                    }
                                });

                            }
                            catch (Exception ex)
                            {
                                Log.e("ErrorControlado", ex.getMessage());
                            }
                        }

                        @Override
                        public void onErrorResponse(IdentyError identyError, HashSet<String> hashSet)
                        {
                            Log.e("ErrorControlado", identyError.getMessage());
                        }
                    },
                    showprogressdialog,
                    true
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("ErrorControlado", ex.getMessage());
        }
    }

    private Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
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

    private Boolean NoEsNuloOVacio(String Dato)
    {
        if (Dato != null)
        {
            if (Dato.length() > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
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

            if (Constantes.ESDESARROLLO)
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
            if (NoEsNuloOVacio(WSQ_Indce))
            {
                Biometria dedo = new Biometria();
                String NumIndice = ManoIndice.equals("right") ? "2" : "7";
                dedo.setBiometryName(NumIndice);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(WSQ_Indce);
                Biometrics.add(dedo);
            }
            if (NoEsNuloOVacio(WSQ_Medio))
            {
                Biometria dedo = new Biometria();
                String NumMedio = ManoIndice.equals("right") ? "3" : "8";
                dedo.setBiometryName(NumMedio);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(WSQ_Medio);
                Biometrics.add(dedo);
            }
            if (NoEsNuloOVacio(WSQ_Anular))
            {
                Biometria dedo = new Biometria();
                String NumAnular = ManoIndice.equals("right") ? "4" : "9";
                dedo.setBiometryName(NumAnular);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(WSQ_Anular);
                Biometrics.add(dedo);
            }
            if (NoEsNuloOVacio(WSQ_Menique))
            {
                Biometria dedo = new Biometria();
                String NumMenique = ManoIndice.equals("right") ? "5" : "10";
                dedo.setBiometryName(NumMenique);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(WSQ_Menique);
                Biometrics.add(dedo);
            }
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