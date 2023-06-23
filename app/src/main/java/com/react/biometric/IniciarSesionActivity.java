package com.react.biometric;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.biometric.BiometricPrompt;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.react.biometric.interfaces.CustomCallback;
import com.react.biometric.orquestador.OrqResponse;
import com.react.biometric.orquestador.Peticion;
import com.react.biometric.utilidades.HttpsPostRequest;
import com.react.biometric.utilidades.ResponseManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IniciarSesionActivity extends AppCompatActivity implements CustomCallback {

    EditText txtUsuario;
    EditText txtContrasenna;
    CheckBox cbxRemember;
    private Button btnIniciarSesion;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = IniciarSesionActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private int duration = Toast.LENGTH_SHORT;
    private static final int ERROR_NEGATIVE_BUTTON = 13;
    private static final String METODO = "50";
    private String usuario = "";
    private String contrasenna = "";
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        Boolean cerroSesion = false;

        txtUsuario = findViewById(R.id.etxtUser);
        txtContrasenna = findViewById(R.id.etxtPassword);
        cbxRemember = findViewById(R.id.cbxRemember);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(view -> iniciarSesion());

        validarPermisos();
        cerroSesion = getIntent().getBooleanExtra(Constantes.CERRO_SESION, false);

        cargarPreferenciasCompartidas();

        if (cbxRemember.isChecked() && Boolean.FALSE.equals(cerroSesion))
        {
            iniciarSesion();
        }
    }

    private void estadoBoton(Boolean estado)
    {
        btnIniciarSesion.setEnabled(estado);
        btnIniciarSesion.setBackgroundColor(Boolean.TRUE.equals(estado) ? Color.parseColor(getString(R.string.color_primary)) : Color.parseColor(getString(R.string.color_primary_dark)));
    }

    private void iniciarSesion()
    {
        estadoBoton(false);
        showDialog(getString(R.string.login));
        String userName = txtUsuario.getText().toString();
        String userPassword = txtContrasenna.getText().toString();

        if (userName.replace(" ","").length() > 0 && userPassword.replace(" ","").length() > 0)
        {
            if (cbxRemember.isChecked())
            {
                initFingerPrint(userName,userPassword);
            }
            else
            {
                try
                {
                    redireccionarInicio(userName, userPassword);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Constantes.USER_NAME);
                    editor.remove(Constantes.USER_PASSWORD);
                    editor.remove(Constantes.USER_CHECKED);
                    editor.apply();
                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_save_userdata_error), duration);
                    toast.show();
                }
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_toast_no_credenciales), Constantes.TOASTDURATION);
            toast.show();
        }
    }

    private void crearPeticion(String userName, String userPassword)
    {
        try
        {
            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Peticion request = new Peticion();
            request.setMethodAuth(METODO);
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

            usuario = request.getCustomerid();
            contrasenna = request.getPass();

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
                String errorCode = respuesta.ObtenerInfoPersonaResult.ErrorCode;
                String message = respuesta.ObtenerInfoPersonaResult.Message;

                if (errorCode.equals("00"))
                {
                    runOnUiThread(this::irInicio);
                }
                else
                {
                    runOnUiThread(() -> {
                        estadoBoton(true);
                        String titulo = getString(R.string.not_matched);
                        AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this);
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

    protected void showDialog(String msg) {
        dismissDialog();
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.simple_dialog, null);
        builderDialog.setTitle(msg);
        builderDialog.setView(dialogView);
        builderDialog.setCancelable(false);
        loadingDialog = builderDialog.show();
    }

    protected void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void redireccionarInicio(String usuario, String contrasenna)
    {
        crearPeticion(usuario, contrasenna);
    }

    private void irInicio()
    {
        Intent intent = new Intent(this, SelectorActivity.class);
        intent.putExtra(Constantes.USER_NAME, usuario);
        intent.putExtra(Constantes.USER_PASSWORD, contrasenna);
        startActivity(intent);
    }

    private void initFingerPrint(final String userName, final String userPassword){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString)
            {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode != ERROR_NEGATIVE_BUTTON)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fingerprint_error), Constantes.TOASTDURATION);
                    toast.show();
                }
                cbxRemember.setChecked(false);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result)
            {
                super.onAuthenticationSucceeded(result);
                try
                {
                    if (!userPassword.equals(""))
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constantes.USER_CHECKED, cbxRemember.isChecked());
                        editor.putString(Constantes.USER_NAME, userName);
                        editor.putString(Constantes.USER_PASSWORD,userPassword);
                        editor.apply();
                    }
                    else
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constantes.USER_NAME, userName);
                        editor.apply();
                    }

                    redireccionarInicio(userName, userPassword);
                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_userdata_error), duration);
                    toast.show();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

        });

        BiometricPrompt.PromptInfo.Builder promptInfo = new BiometricPrompt.PromptInfo.Builder();
        if (!userPassword.equals(""))
        {
            promptInfo.setTitle(getString(R.string.fingerprint_prompt))
                    .setDeviceCredentialAllowed(true);
        }
        else
        {
            promptInfo.setTitle(getString(R.string.fingerprint_prompt_basic))
                    .setNegativeButtonText(getString(R.string.fingerprint_cancel))
                    .setDeviceCredentialAllowed(true);
        }

        try
        {
            biometricPrompt.authenticate(promptInfo.build());
        }
        catch (Exception ex)
        {
            Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), duration);
            toast.show();
        }
    }

    private void cargarPreferenciasCompartidas()
    {
        try
        {
            sharedPreferences = getSharedPreferences("app_local", MODE_PRIVATE);
            txtUsuario.setText(sharedPreferences.getString(Constantes.USER_NAME, ""));
            txtContrasenna.setText(sharedPreferences.getString(Constantes.USER_PASSWORD,""));
            cbxRemember.setChecked(sharedPreferences.getBoolean(Constantes.USER_CHECKED, false));

        }
        catch (Exception e)
        {
            Log.d(TAG, "initComponents: " + e);
            txtUsuario.setText("");
            txtContrasenna.setText("");
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_userdata_error), duration);
            toast.show();
        }
    }

    private void validarPermisos()
    {
        String[] neededPermissions = getNotGrantedPermissions();

        if (neededPermissions.length != 0) {
            requestPermissions(neededPermissions);
        }
    }

    //SOLICITAR PERMISOS FALTANTES
    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS);
    }

    //PERMISOS NECESARIOS
    private String[] getNotGrantedPermissions() {
        List<String> neededPermissions = new ArrayList<>();
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.CAMERA);
        }
        return neededPermissions.toArray(new String[neededPermissions.size()]);
    }
}