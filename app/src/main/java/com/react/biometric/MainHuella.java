package com.react.biometric;

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
import com.identy.TemplateSize;
import com.identy.WSQCompression;
import com.identy.enums.Finger;
import com.identy.enums.FingerDetectionMode;
import com.identy.enums.Hand;
import com.identy.enums.Template;
import com.react.biometric.interfaces.CustomCallback;
import com.react.biometric.models.ResponseIdenty;
import com.react.biometric.orquestador.Biometria;
import com.react.biometric.orquestador.OrqResponse;
import com.react.biometric.orquestador.Peticion;
import com.react.biometric.utilidades.BaseActivity;
import com.react.biometric.utilidades.HttpsPostRequest;
import com.react.biometric.utilidades.ResponseManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

    //LAYOUTS
    private LinearLayout tabInformacion;
    private LinearLayout tabEscaner;
    //CAMPOS DE TEXTO

    //VARIABLES DE PETICION
    private String peticionNID;
    private String peticionNombres;
    private String peticionApellidos;
    private String peticionSexo;
    private String peticionPais;
    private String peticionNacimiento;

    //ANTERIORES

    String mode = "commercial";
    WSQCompression compression = WSQCompression.WSQ_10_1;
    int base64encoding = Base64.DEFAULT;
    static final String NET_KEY = "AIzaSyDFFGSONyHF0Aa7ikelLSyXw_CIa0PGVdk";
    private ImageView imgIndice;
    private ImageView imgMedio;
    private ImageView imgAnular;
    private ImageView imgMenique;
    private String userName;
    private String userPassword;
    private String userModalidad;

    private String metodo;
    private boolean captura;
    private AlertDialog loadingDialog;
    String huellaPNG = "";
    String huellaWSQ = "";
    String manoIndice = "";
    String pngIndce = "";
    String pngMedio = "";
    String pngAnular = "";
    String pngMenique = "";
    String wsqIndce = "";
    String wsqMedio = "";
    String wsqAnular = "";
    String wsqMenique = "";

    private LinearLayout mainContent;
    private LinearLayout tabRespuesta;
    private ImageView imgResultado;


    private Toolbar toolbar;

    private String errorCode = "";
    private String message = "";
    private Boolean estadoDocumento = false;
    private Boolean capturando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_huella);

        btnTabCapturar = findViewById(R.id.btnTabCapturar);
        btnTabInfo = findViewById(R.id.btnTabInfo);
        Button btnIniciarCaptura = findViewById(R.id.btnIniciarCaptura);
        Button btnIniciarProceso = findViewById(R.id.btnIniciarProceso);
        tabInformacion = findViewById(R.id.TabInformacion);
        tabEscaner = findViewById(R.id.TabEscaner);

        TextView txtIdentificacion = findViewById(R.id.txtIdentificacion);
        TextView txtNombres = findViewById(R.id.txtNombres);
        TextView txtApellidos = findViewById(R.id.txtApellidos);
        TextView txtSexo = findViewById(R.id.txtSexo);
        TextView txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);

        TextView txtCodPais = findViewById(R.id.txtCodPais);
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
        txtIdentificacion.setText(peticionNID);
        txtNombres.setText(peticionNombres);
        txtApellidos.setText(peticionApellidos);
        txtSexo.setText(peticionSexo);
        txtCodPais.setText(peticionPais);
        txtFechaNacimiento.setText(peticionNacimiento);

        mostrarTabCaptura();

        captura = false;
        metodo = "99";
        toolbar = findViewById(R.id.toolbarMain);
        mainContent = findViewById(R.id.MainContent);
        tabRespuesta = findViewById(R.id.TabRespuesta);
        imgResultado = findViewById(R.id.img_resultado);
        TextView txtContinuar = findViewById(R.id.txt_continuar);

        if (userModalidad != null)
        {
            if (userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                metodo = Constantes.ENROLL_HUELLA;
                toolbar.setTitle(getString(R.string.title_huellas));
            }
            else
            {
                metodo = Constantes.VERI_HUELLA;
                toolbar.setTitle(getString(R.string.title_huellas_val));
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

        btnIniciarCaptura.setOnClickListener(view -> {
            if (Boolean.FALSE.equals(capturando))
            {
                captura = false;
                estadoDocumento = false;
                errorCode = "";
                message = "";
                imgIndice.setImageResource(R.drawable.finger);
                imgMedio.setImageResource(R.drawable.finger);
                imgAnular.setImageResource(R.drawable.finger);
                imgMenique.setImageResource(R.drawable.finger);
                capturando = true;
                ejecutarCapturaHuella();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.finger_starting), Constantes.TOASTDURATION);
                toast.show();
            }
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
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.finger_error_face), Constantes.TOASTDURATION);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainHuella.this);
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
                            //dont use
                        });
                builder.create().show();
            }
            else
            {
                String titulo = getString(R.string.not_matched);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainHuella.this);
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
                            //don´t used
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

    private void mostrarTabCaptura() {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        tabInformacion.setVisibility(View.GONE);
        tabEscaner.setVisibility(View.VISIBLE);
    }

    private void mostrarTabInformacion() {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        tabInformacion.setVisibility(View.VISIBLE);
        tabEscaner.setVisibility(View.GONE);
    }

    private void ejecutarCapturaHuella()
    {
        FingerDetectionMode[] detectionModes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS}, 1);

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
            captura = false;

            IdentySdk.newInstance(
                    MainHuella.this,
                    licenseFile,
                    identySdk -> {
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
                            Log.e("Fail_OnInit", ex.getMessage());
                        }
                    },
                    new IdentyResponseListener() {
                        @Override
                        public void onAttempt(Hand hand, int i, Map<Finger, Attempt> map) {
                            capturando = false;
                            manoIndice = hand.toString();
                        }

                        @Override
                        public void onResponse(IdentyResponse identyResponse, HashSet<String> hashSet) {
                            try
                            {
                                Gson gson = new Gson();
                                ResponseIdenty resp = gson.fromJson(identyResponse.toJson(MainHuella.this).toString(), ResponseIdenty.class);

                                if (manoIndice.equals(R.string.mano))
                                {
                                    wsqIndce = resp.getData().rightindex.templates.WSQ.DEFAULT;
                                    wsqMedio = resp.getData().rightmiddle.templates.WSQ.DEFAULT;
                                    wsqAnular = resp.getData().rightring.templates.WSQ.DEFAULT;
                                    wsqMenique = resp.getData().rightlittle.templates.WSQ.DEFAULT;

                                    pngIndce = resp.getData().rightindex.templates.PNG.DEFAULT;
                                    pngMedio = resp.getData().rightmiddle.templates.PNG.DEFAULT;
                                    pngAnular = resp.getData().rightring.templates.PNG.DEFAULT;
                                    pngMenique = resp.getData().rightlittle.templates.PNG.DEFAULT;
                                }
                                else
                                {
                                    wsqIndce = resp.getData().leftindex.templates.WSQ.DEFAULT;
                                    wsqMedio = resp.getData().leftmiddle.templates.WSQ.DEFAULT;
                                    wsqAnular = resp.getData().leftring.templates.WSQ.DEFAULT;
                                    wsqMenique = resp.getData().leftlittle.templates.WSQ.DEFAULT;

                                    pngIndce = resp.getData().leftindex.templates.PNG.DEFAULT;
                                    pngMedio = resp.getData().leftmiddle.templates.PNG.DEFAULT;
                                    pngAnular = resp.getData().leftring.templates.PNG.DEFAULT;
                                    pngMenique = resp.getData().leftlittle.templates.PNG.DEFAULT;
                                }

                                runOnUiThread(() -> {
                                    try
                                    {
                                        imgIndice.setImageBitmap(convertBase64ToBitmap(pngIndce));
                                        imgMedio.setImageBitmap(convertBase64ToBitmap(pngMedio));
                                        imgAnular.setImageBitmap(convertBase64ToBitmap(pngAnular));
                                        imgMenique.setImageBitmap(convertBase64ToBitmap(pngMenique));
                                        captura = true;
                                        mostrarTabInformacion();
                                    }
                                    catch (Exception ex)
                                    {
                                        Log.e("Fail_run", ex.getMessage());

                                    }
                                });

                            }
                            catch (Exception ex)
                            {
                                Log.e("Fail_run", ex.getMessage());
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

    private Boolean noEsNuloOVacio(String dato)
    {
        if (dato != null)
        {
            return dato.length() > 0;
        }
        else
        {
            return false;
        }
    }

    private void crearPeticion()
    {
        try
        {
            showDialog(getString(R.string.procesando));

            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Peticion request = new Peticion();
            request.setMethodAuth(metodo);
            request.setNid(peticionNID);

            if (Boolean.TRUE.equals(Constantes.ESDESARROLLO))
            {
                request.setCustomerid("xpi");
                request.setPass("$tr@!ght1928");
            }
            else
            {
                request.setCustomerid(userName);
                request.setPass(userPassword);
            }

            List<Biometria> biometrics = new ArrayList<>();
            if (Boolean.TRUE.equals(noEsNuloOVacio(wsqIndce)))
            {
                Biometria dedo = new Biometria();
                String numIndice = manoIndice.equals(R.string.mano) ? "2" : "7";
                dedo.setBiometryName(numIndice);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(wsqIndce);
                biometrics.add(dedo);
            }
            if (Boolean.TRUE.equals(noEsNuloOVacio(wsqMedio)))
            {
                Biometria dedo = new Biometria();
                String numMedio = manoIndice.equals(R.string.mano) ? "3" : "8";
                dedo.setBiometryName(numMedio);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(wsqMedio);
                biometrics.add(dedo);
            }
            if (Boolean.TRUE.equals(Boolean.TRUE.equals(noEsNuloOVacio(wsqAnular))))
            {
                Biometria dedo = new Biometria();
                String numAnular = manoIndice.equals(R.string.mano) ? "4" : "9";
                dedo.setBiometryName(numAnular);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(wsqAnular);
                biometrics.add(dedo);
            }
            if (Boolean.TRUE.equals(noEsNuloOVacio(wsqMenique)))
            {
                Biometria dedo = new Biometria();
                String numMenique = manoIndice.equals(R.string.mano) ? "5" : "10";
                dedo.setBiometryName(numMenique);
                dedo.setBiometryRawDataType("Jpeg");
                dedo.setRawData(wsqMenique);
                biometrics.add(dedo);
            }
            request.setBiometrics(biometrics);

            if (metodo.equals("30") )
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
        TextView textoPrincipal = findViewById(R.id.texto_principal);
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