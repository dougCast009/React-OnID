package com.react.Biometric;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import com.react.Biometric.interfaces.CustomCallback;
import com.react.Biometric.orquestador.OrqResponse;
import com.react.Biometric.orquestador.Peticion;
import com.react.Biometric.utilidades.HttpsPostRequest;
import com.react.Biometric.utilidades.ResponseManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IniciarSesionActivity extends AppCompatActivity implements CustomCallback {

    EditText txtUsuario;
    EditText txtContrasenna;
    CheckBox cbxRemember;
    private Button btnIniciarSesion;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private String WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS = "";
    private String WARNING_NOT_ALL_GRANTED = "";
    private String MESSAGE_ALL_PERMISSIONS_GRANTED = "";
    private static final String TAG = IniciarSesionActivity.class.getSimpleName();
    private Map<String, Integer> mPermissions = new HashMap<String, Integer>();
    private SharedPreferences sharedPreferences;
    private int duration = Toast.LENGTH_SHORT;
    private boolean firstOpen;
    private int ERROR_NEGATIVE_BUTTON = 13;
    private String Metodo = "50";
    private String Usuario = "";
    private String Contrasenna = "";
    private Boolean CerroSesion = false;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        txtUsuario = findViewById(R.id.etxtUser);
        txtContrasenna = findViewById(R.id.etxtPassword);
        cbxRemember = findViewById(R.id.cbxRemember);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(view -> {
            IniciarSesion();
        });

        ValidarPermisos();

        WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS = getString(R.string.permissions_not_continue);
        WARNING_NOT_ALL_GRANTED = getString(R.string.permissions_some_warning);
        MESSAGE_ALL_PERMISSIONS_GRANTED = getString(R.string.permissions_all_granted);
        CerroSesion = getIntent().getBooleanExtra(Constantes.CERRO_SESION, false);

        CargarPreferenciasCompartidas();

        if (cbxRemember.isChecked() && !CerroSesion)
        {
            IniciarSesion();
        }
    }

    //EVENTO BOTON btnIniciarSesion
    public void IniciarSesion(View view) {
        IniciarSesion();
    }

    private void EstadoBoton(Boolean Estado)
    {
        btnIniciarSesion.setEnabled(Estado);
        btnIniciarSesion.setBackgroundColor(Estado ? Color.parseColor(getString(R.string.color_primary)) : Color.parseColor(getString(R.string.color_primary_dark)));
    }

    private void IniciarSesion()
    {
        EstadoBoton(false);
        showDialog("Validando...");
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
                    RedireccionarInicio(userName, userPassword);
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
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_toast_no_credenciales), Constantes.ToastDuration);
            toast.show();
        }
    }

    private void CrearPeticion(String userName, String userPassword)
    {
        try
        {
            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Peticion request = new Peticion();
            request.setMethodAuth(Metodo);
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

            Usuario = request.getCUSTOMERID();
            Contrasenna = request.getPASS();

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
            OrqResponse Respuesta = ResponseManager.obtenerObjetoRespuesta(object);

            if (Respuesta != null)
            {
                String ErrorCode = Respuesta.ObtenerInfoPersonaResult.ErrorCode;
                String Message = Respuesta.ObtenerInfoPersonaResult.Message;

                if (ErrorCode.equals("00"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            IrInicio();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EstadoBoton(true);
                            String Titulo = getString(R.string.not_matched);
                            AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this);
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

    private void RedireccionarInicio(String Usuario, String Contrasenna)
    {
        CrearPeticion(Usuario, Contrasenna);
    }

    private void IrInicio()
    {
        Intent intent = new Intent(this, SelectorActivity.class);
        intent.putExtra(Constantes.USER_NAME, Usuario);
        intent.putExtra(Constantes.USER_PASSWORD, Contrasenna);
        startActivity(intent);
    }

    private void initFingerPrint(final String userName, final String userPassword){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString)
            {
                super.onAuthenticationError(errorCode, errString);
                if (!(errorCode == ERROR_NEGATIVE_BUTTON))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fingerprint_error), Constantes.ToastDuration);
                    toast.show();
                }
                cbxRemember.setChecked(false);
            }

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

                    RedireccionarInicio(userName, userPassword);
                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_userdata_error), duration);
                    toast.show();
                }
            }

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

    private void CargarPreferenciasCompartidas()
    {
        try
        {
            sharedPreferences = getSharedPreferences("app_local", MODE_PRIVATE);
            firstOpen = sharedPreferences.getBoolean(Constantes.FIRST_OPEN, true);
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

    private void ValidarPermisos()
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
        List<String> neededPermissions = new ArrayList<String>();
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.CAMERA);
        }
        return neededPermissions.toArray(new String[neededPermissions.size()]);
    }
}