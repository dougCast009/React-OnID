package com.react.Biometric;

import static com.react.Biometric.Constantes.OPTION_MODALIDAD;
import static com.react.Biometric.Constantes.USER_NAME;
import static com.react.Biometric.Constantes.USER_PASSWORD;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.react.Biometric.utilidades.HttpsPostRequest;
import com.react.Biometric.utilidades.ResponseManager;
import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.enums.Scenario;
import com.regula.documentreader.api.params.DocReaderConfig;
import com.react.Biometric.util.LicenseUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainDocumento extends BaseActivity implements CustomCallback {

    //NUEVOS
    private Button btnIniciarProceso;
    private TextView txt_estado;
    private TextView txt_identificacion;
    private TextView txt_nombres;
    private TextView txt_apellidos;
    private TextView txt_sexo;
    private TextView txt_cod_pais;
    private TextView txt_fecha_nacimiento;
    private ImageView img_foto;
    private ImageView img_firma;
    private ImageView img_documento_1;
    private ImageView img_documento_2;
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
    private String userName;
    private String userPassword;
    private String userModalidad;
    private String Metodo;
    private String formModalidad;
    private String userNID;
    private String userNombres;
    private String userApellidos;
    private String DocCapturado;
    private TextView nameTv;
    private TextView numberTv;
    protected FrameLayout fragmentContainer;
    private Button btnProcesar;
    private AlertDialog loadingDialog;
    private ImageView docImageIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LicenseUtil.getLicense(this) == null)
            showDialog(this);

        userName = getIntent().getStringExtra(USER_NAME);
        userPassword = getIntent().getStringExtra(USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(OPTION_MODALIDAD);
        formModalidad = getIntent().getStringExtra(Constantes.FORM_MODALIDAD);

        Metodo = "99";
        if (NoEsNuloOVacio(userModalidad))
        {
            if (userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                Metodo = Constantes.enroll_docmentos;
            }
            else
            {
                Metodo = Constantes.veri_docmentos;
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_modalidad), Constantes.ToastDuration);
            toast.show();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        //CARGAR ELEMENTOS DE LA VISTA
        fragmentContainer = findViewById(R.id.fragmentContainer);
        Toolbar toolbar = fragmentContainer.findViewById(R.id.toolbarMain);
        if (userModalidad.equals(Constantes.MODALIDADENROLA))
        {
            toolbar.setTitle(getString(R.string.title_documentos));
        }
        else
        {
            toolbar.setTitle(getString(R.string.title_documentos_val));
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

        txt_estado = fragmentContainer.findViewById(R.id.txt_estado);
        txt_identificacion = fragmentContainer.findViewById(R.id.txt_identificacion);
        txt_nombres = fragmentContainer.findViewById(R.id.txt_nombres);
        txt_apellidos = fragmentContainer.findViewById(R.id.txt_apellidos);
        txt_sexo = fragmentContainer.findViewById(R.id.txt_sexo);
        txt_fecha_nacimiento = fragmentContainer.findViewById(R.id.txt_fecha_nacimiento);
        txt_cod_pais = fragmentContainer.findViewById(R.id.txt_cod_pais);
        img_foto = fragmentContainer.findViewById(R.id.img_foto);
        img_firma = fragmentContainer.findViewById(R.id.img_firma);
        img_documento_1 = fragmentContainer.findViewById(R.id.img_documento_1);
        img_documento_2 = fragmentContainer.findViewById(R.id.img_documento_2);
        btnIniciarProceso = fragmentContainer.findViewById(R.id.btnIniciarProceso);

        if (formModalidad.equals(Constantes.FORMFACIAL))
        {
            btnIniciarProceso.setText(getString(R.string.face_titulo));
        }
        else if (formModalidad.equals(Constantes.FormHuella))
        {
            btnIniciarProceso.setText(getString(R.string.finger_titulo));
        }
        else if (formModalidad.equals(Constantes.FormDocumento))
        {
            if (userModalidad.equals(Constantes.MODALIDADENROLA))
            {
                btnIniciarProceso.setText(getString(R.string.enrolar));
            }
            else
            {
                btnIniciarProceso.setText(getString(R.string.verificar));
                if (Metodo.equals("20"))
                {
                    btnIniciarProceso.setVisibility(View.GONE);
                }
            }
        }

        btnIniciarProceso.setOnClickListener(view -> {
            String Estado = txt_estado.getText().toString();
            if (NoEsNuloOVacio(Estado))
            {
                if (Estado.equals(getString(R.string.result_valido)))
                {
                    Boolean Valido = true;
                    peticionNID = txt_identificacion.getText().toString();
                    peticionNombres = txt_nombres.getText().toString();
                    peticionApellidos = txt_apellidos.getText().toString();
                    peticionSexo = txt_sexo.getText().toString();
                    peticionNacimiento = txt_fecha_nacimiento.getText().toString();
                    peticionPais = txt_cod_pais.getText().toString();
                    Valido = NoEsNuloOVacio(peticionNID) && Valido ? true : false;
                    Valido = NoEsNuloOVacio(peticionNombres) && Valido ? true : false;
                    Valido = NoEsNuloOVacio(peticionApellidos) && Valido ? true : false;
                    Valido = NoEsNuloOVacio(peticionNacimiento) && Valido ? true : false;
                    Valido = NoEsNuloOVacio(peticionPais) && Valido ? true : false;

                    if (Valido)
                    {
                        if (formModalidad.equals(Constantes.FormFacial))
                        {
                            Intent intent = new Intent(this, MainFacial.class);
                            intent.putExtra(Constantes.USER_NAME, userName);
                            intent.putExtra(Constantes.USER_PASSWORD, userPassword);
                            intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
                            intent.putExtra(Constantes.REQUEST_NID, peticionNID);
                            intent.putExtra(Constantes.REQUEST_NAME, peticionNombres);
                            intent.putExtra(Constantes.REQUEST_LAST_NAME, peticionApellidos);
                            intent.putExtra(Constantes.REQUEST_SEX, peticionSexo);
                            intent.putExtra(Constantes.REQUEST_COUNTRY, peticionPais);
                            intent.putExtra(Constantes.REQUEST_BIRTH, peticionNacimiento);
                            startActivity(intent);
                        }
                        else if (formModalidad.equals(Constantes.FormHuella))
                        {
                            Intent intent = new Intent(this, MainHuella.class);
                            intent.putExtra(Constantes.USER_NAME, userName);
                            intent.putExtra(Constantes.USER_PASSWORD, userPassword);
                            intent.putExtra(Constantes.OPTION_MODALIDAD, userModalidad);
                            intent.putExtra(Constantes.REQUEST_NID, peticionNID);
                            intent.putExtra(Constantes.REQUEST_NAME, peticionNombres);
                            intent.putExtra(Constantes.REQUEST_LAST_NAME, peticionApellidos);
                            intent.putExtra(Constantes.REQUEST_SEX, peticionSexo);
                            intent.putExtra(Constantes.REQUEST_COUNTRY, peticionPais);
                            intent.putExtra(Constantes.REQUEST_BIRTH, peticionNacimiento);
                            startActivity(intent);
                        }
                        else if (formModalidad.equals(Constantes.FormDocumento))
                        {
                            peticionFoto = getStringImage(img_foto);
                            peticionFirma = getStringImage(img_firma);
                            peticionDoc = getStringImage(img_documento_1);
                            Valido = (NoEsNuloOVacio(peticionDoc)) || (NoEsNuloOVacio(peticionFoto) && NoEsNuloOVacio(peticionFirma)) && Valido ? true : false;

                            if (Valido)
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
                                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_datos), Constantes.ToastDuration);
                                toast.show();
                            }
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_modalidad), Constantes.ToastDuration);
                            toast.show();
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_datos), Constantes.ToastDuration);
                        toast.show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.invalid_doc), Constantes.ToastDuration);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.document_error_face), Constantes.ToastDuration);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(USER_PASSWORD, userPassword);
        intent.putExtra(OPTION_MODALIDAD, userModalidad);
        startActivity(intent);
    }

    protected void initializeReader() {
        showDialog("Initializing");
        byte[] license = LicenseUtil.getLicense( this);
        DocReaderConfig config = new DocReaderConfig(license);
        config.setLicenseUpdate(true);
        DocumentReader.Instance().initializeReader(MainDocumento.this, config, initCompletion);
    }

    @Override
    protected void onPrepareDbCompleted() {
        initializeReader();
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Error");
        builder.setMessage("license in assets is missed!");
        builder.setPositiveButton(getString(R.string.strAccessibilityCloseButton), (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    public String getStringImage(ImageView imageView) {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        Bitmap bit = bd.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private Boolean NoEsNuloOVacio(String Dato)
    {
        if (Dato != null)
        {
            if (Dato.length() > 0)
            {
                if (!Dato.equals(getString(R.string.document_pending)))
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
        else
        {
            return false;
        }
    }

    private void CrearPeticion()
    {
        try
        {
            showDialog("Procesando...");
            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
            if (NoEsNuloOVacio(peticionDoc))
            {
                Biometria documento = new Biometria();
                documento.setBiometryName("Document");
                documento.setBiometryRawDataType("Jpeg");
                documento.setRawData(peticionDoc);
                Biometrics.add(documento);
            }

            if (Metodo == "30")
            {
                if (NoEsNuloOVacio(peticionFoto))
                {
                    Biometria rostro = new Biometria();
                    rostro.setBiometryName("Face");
                    rostro.setBiometryRawDataType("Jpeg");
                    rostro.setRawData(peticionFoto);
                    Biometrics.add(rostro);

                    //LA FIRMA DA PROBLEMAS AL INTENTAR ENROLAR
                    /*if (NoEsNuloOVacio(peticionFirma))
                    {
                        Biometria firma = new Biometria();
                        firma.setBiometryName("Signature");
                        firma.setBiometryRawDataType("Jpeg");
                        firma.setRawData(peticionFirma);
                        Biometrics.add(firma);
                    }*/
                }
                request.setNombres(peticionNombres);
                request.setApellidos(peticionApellidos);
                if (peticionSexo.equals("M") || peticionSexo.equals("F"))
                {
                    request.setSexo(peticionSexo);
                }
                request.setNacionalidad(peticionPais);
                request.setFechaNacimiento(peticionNacimiento);

            }
            request.setBiometrics(Biometrics);

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
                String ErrorCode = Respuesta.ObtenerInfoPersonaResult.ErrorCode;
                String Message = Respuesta.ObtenerInfoPersonaResult.Message;

                if (ErrorCode.equals("00"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String Titulo = getString(R.string.matched);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainDocumento.this);
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
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String Titulo = getString(R.string.not_matched);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainDocumento.this);
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