package com.react.Biometric;

import static com.react.Biometric.Constantes.OPTION_MODALIDAD;
import static com.react.Biometric.Constantes.USER_NAME;
import static com.react.Biometric.Constantes.USER_PASSWORD;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
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
import com.regula.documentreader.api.params.DocReaderConfig;
import com.react.Biometric.util.LicenseUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainDocumento extends BaseActivity implements CustomCallback {

    //NUEVOS
    private String peticionNID;
    private String peticionNombres;
    private String peticionApellidos;
    private String peticionSexo;
    private String peticionPais;
    private String peticionNacimiento;
    private String peticionDoc;

    private String peticionFoto;

    private Button btnIniciarProceso;
    //ANTERIORES
    private String userName;
    private String userPassword;
    private String userModalidad;
    private String metodo;
    private String formModalidad;
    protected FrameLayout fragmentContainerLayout;
    //Variables
    private TextView txtNombres;
    private TextView txtSexo;
    private TextView txtIdentificacion;
    private TextView txtFechaNacimiento;
    private TextView txtEstado;
    private TextView txtCodPais;
    private TextView txtApellidos;
    private ImageView imgFoto;
    private ImageView imgFirma;

    private ImageView imgDocumento1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LicenseUtil.getLicense(this) == null)
            showDialog(this);

        userName = getIntent().getStringExtra(USER_NAME);
        userPassword = getIntent().getStringExtra(USER_PASSWORD);
        userModalidad = getIntent().getStringExtra(OPTION_MODALIDAD);
        formModalidad = getIntent().getStringExtra(Constantes.FORM_MODALIDAD);

        metodo = "99";
        if (Boolean.TRUE.equals(noEsNuloOVacio(userModalidad))) {
            if (userModalidad.equals(Constantes.MODALIDADENROLA)) {
                metodo = Constantes.ENROLL_DOCMENTOS;
            } else {
                metodo = Constantes.VERI_DOCMENTOS;
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_modalidad), Constantes.TOASTDURATION);
            toast.show();
        }
    }

    private void changeBtnIniciarProceso() {
        if (formModalidad.equals(Constantes.FORMFACIAL)) {
            btnIniciarProceso.setText(getString(R.string.face_titulo));
        } else if (formModalidad.equals(Constantes.FORMHUELLA)) {
            btnIniciarProceso.setText(getString(R.string.finger_titulo));
        } else if (formModalidad.equals(Constantes.FORMDOCUMENTO)) {
            if (userModalidad.equals(Constantes.MODALIDADENROLA)) {
                btnIniciarProceso.setText(getString(R.string.enrolar));
            } else {
                btnIniciarProceso.setText(getString(R.string.verificar));
                if (metodo.equals("20")) {
                    btnIniciarProceso.setVisibility(View.GONE);
                }
            }
        }
    }

    private void changeToolsBar() {
        //CARGAR ELEMENTOS DE LA VISTA
        fragmentContainerLayout = findViewById(R.id.fragmentContainer);
        Toolbar toolbar = fragmentContainerLayout.findViewById(R.id.toolbarMain);
        if (userModalidad.equals(Constantes.MODALIDADENROLA)) {
            toolbar.setTitle(getString(R.string.title_documentos));
        } else {
            toolbar.setTitle(getString(R.string.title_documentos_val));
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void changeTextFields() {
        txtEstado = fragmentContainerLayout.findViewById(R.id.txtEstado);
        txtIdentificacion = fragmentContainerLayout.findViewById(R.id.txtIdentificacion);
        txtNombres = fragmentContainerLayout.findViewById(R.id.txtNombres);
        txtApellidos = fragmentContainerLayout.findViewById(R.id.txtApellidos);
        txtSexo = fragmentContainerLayout.findViewById(R.id.txtSexo);
        txtFechaNacimiento = fragmentContainerLayout.findViewById(R.id.txtFechaNacimiento);
        txtCodPais = fragmentContainerLayout.findViewById(R.id.txtCodPais);
        imgFoto = fragmentContainerLayout.findViewById(R.id.img_foto);
        imgFirma = fragmentContainerLayout.findViewById(R.id.img_firma);
        imgDocumento1 = fragmentContainerLayout.findViewById(R.id.img_documento_1);
        btnIniciarProceso = fragmentContainerLayout.findViewById(R.id.btnIniciarProceso);
    }
    private void changeByType( Boolean valido){
        Intent intent ;
        String peticionFirma;
        switch(formModalidad) {
            case Constantes.FORMFACIAL:
                intent = new Intent(this, MainFacial.class);
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
                break;
            case Constantes.FORMHUELLA:
                intent = new Intent(this, MainHuella.class);
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
                break;
            case Constantes.FORMDOCUMENTO:
                peticionFoto = getStringImage(imgFoto);
                peticionFirma = getStringImage(imgFirma);
                peticionDoc = getStringImage(imgDocumento1);
                valido = (Boolean.TRUE.equals(noEsNuloOVacio(peticionDoc))) || Boolean.TRUE.equals((noEsNuloOVacio(peticionFoto) && noEsNuloOVacio(peticionFirma)) && valido);

                if (Boolean.TRUE.equals(valido)) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setTitle(getString(R.string.consentimiento_title));
                    dlgAlert.setMessage(getString(R.string.consentimiento));
                    dlgAlert.setCancelable(false);
                    dlgAlert.setNegativeButton(getString(R.string.rechazar), null);
                    dlgAlert.setPositiveButton(getString(R.string.aceptar), (dialog, whichButton) -> crearPeticion());
                    dlgAlert.create().show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_datos), Constantes.TOASTDURATION);
                    toast.show();
                }
                break;
            default:
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_modalidad), Constantes.TOASTDURATION);
                toast.show();
                // code block
        }

    }

    private boolean validateFilds(){
        Boolean valido = true;
        peticionNID = txtIdentificacion.getText().toString();
        peticionNombres = txtNombres.getText().toString();
        peticionApellidos = txtApellidos.getText().toString();
        peticionSexo = txtSexo.getText().toString();
        peticionNacimiento = txtFechaNacimiento.getText().toString();
        peticionPais = txtCodPais.getText().toString();
        valido = noEsNuloOVacio(peticionNID) && valido;
        valido = noEsNuloOVacio(peticionNombres) && valido;
        valido = noEsNuloOVacio(peticionApellidos) && valido;
        valido = noEsNuloOVacio(peticionNacimiento) && valido;
        valido = noEsNuloOVacio(peticionPais) && valido;
        return valido;
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        this.changeToolsBar();

        this.changeTextFields();

        this.changeBtnIniciarProceso();

        btnIniciarProceso.setOnClickListener(view -> {
            String estado = txtEstado.getText().toString();
            if (Boolean.TRUE.equals(noEsNuloOVacio(estado))) {
                if (estado.equals(getString(R.string.result_valido))) {
                    Boolean valido = validateFilds();

                    if (Boolean.TRUE.equals(valido)) {
                        this.changeByType(valido);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_datos), Constantes.TOASTDURATION);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.invalid_doc), Constantes.TOASTDURATION);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.document_error_face), Constantes.TOASTDURATION);
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
        showDialog(getString(R.string.alerta_Inicializando));
        byte[] license = LicenseUtil.getLicense(this);
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
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private Boolean noEsNuloOVacio(String dato) {
        return dato != null && dato.length() > 0;
    }

    private void crearPeticion() {
        try {
            showDialog(getString(R.string.procesando));
            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Peticion request = new Peticion();
            request.setMethodAuth(metodo);
            request.setNid(peticionNID);
            if (Boolean.TRUE.equals(Constantes.ESDESARROLLO)) {
                request.setCustomerid("xpi");
                request.setPass("$tr@!ght1928");
            } else {
                request.setCustomerid(userName);
                request.setPass(userPassword);
            }
            List<Biometria> biometrics = new ArrayList<>();
            if (Boolean.TRUE.equals(noEsNuloOVacio(peticionDoc))) {
                Biometria documento = new Biometria();
                documento.setBiometryName("Document");
                documento.setBiometryRawDataType("Jpeg");
                documento.setRawData(peticionDoc);
                biometrics.add(documento);
            }

            if (metodo.equals("30")) {
                if (Boolean.TRUE.equals(noEsNuloOVacio(peticionFoto))) {
                    Biometria rostro = new Biometria();
                    rostro.setBiometryName("Face");
                    rostro.setBiometryRawDataType("Jpeg");
                    rostro.setRawData(peticionFoto);
                    biometrics.add(rostro);
                }
                request.setNombres(peticionNombres);
                request.setApellidos(peticionApellidos);
                if (peticionSexo.equals("M") || peticionSexo.equals("F")) {
                    request.setSexo(peticionSexo);
                }
                request.setNacionalidad(peticionPais);
                request.setFechaNacimiento(peticionNacimiento);

            }
            request.setBiometrics(biometrics);

            Gson gson = new Gson();
            JsonObject jsonRequest = JsonParser.parseString(gson.toJson(request)).getAsJsonObject();
            realizarPeticion(jsonRequest);
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_peticion), Constantes.TOASTDURATION);
            toast.show();
        }
    }

    private void realizarPeticion(JsonObject request) {
        try {
            InputStream privateCrt = getResources().openRawResource(R.raw.certificado_android_pfx);
            InputStream certChain = getResources().openRawResource(R.raw.certificado_android_pem);
            final HttpsPostRequest peticion = new HttpsPostRequest(request, this, privateCrt, certChain);
            peticion.execute(Constantes.URL_BASE);
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.face_error_realiza_peticion), Constantes.TOASTDURATION);
            toast.show();
        }
    }

    @Override
    public void obtenerRespuesta(Boolean success, String object) {
        runOnUiThread(() -> dismissDialog());

        try {
            OrqResponse respuesta = ResponseManager.obtenerObjetoRespuesta(object);

            if (respuesta != null) {
                String errorCode = respuesta.ObtenerInfoPersonaResult.ErrorCode;
                String message = respuesta.ObtenerInfoPersonaResult.Message;

                if (errorCode.equals("00")) {
                    runOnUiThread(() -> {
                        String titulo = getString(R.string.matched);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainDocumento.this);
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
                    });
                } else {
                    runOnUiThread(() -> {
                        String titulo = getString(R.string.not_matched);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainDocumento.this);
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
                    });
                }
            } else {
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