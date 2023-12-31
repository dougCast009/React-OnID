package com.react.biometric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.UnsupportedSchemeException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.react.biometric.interfaces.CustomCallback;
import com.react.biometric.orquestador.Biometria;
import com.react.biometric.orquestador.Peticion;
import com.react.biometric.utilidades.HttpsPostRequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainOrquestador extends AppCompatActivity implements CustomCallback {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_orquestador);

        Button btnIniciarCaptura = (Button) this.findViewById(R.id.btnIniciarEscaner);
        btnIniciarCaptura.setOnClickListener(view -> crearPeticion());

    }

    private void crearPeticion()
    {
        try
        {
            View vista = this.getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Biometria biometric = new Biometria();
            biometric.setBiometryName("Document");
            biometric.setBiometryRawDataType("Jpeg");
            biometric.setRawData("");
            //biodata
            List<Biometria> biometrics = new ArrayList<>();
            biometrics.add(biometric);

            Peticion request = new Peticion();
            request.setCustomerid("xpi");
            request.setPass("$tr@!ght1928");
            request.setNid("201110111");
            request.setMethodAuth("30");
            request.setNombres("COPPER");
            request.setApellidos("BRAND");
            request.setBiometrics(biometrics);

            Gson gson = new Gson();
            JsonObject jsonRequest = JsonParser.parseString(gson.toJson(request)).getAsJsonObject();
            realizarPeticion(jsonRequest);
        }
        catch (UnsupportedOperationException ex) {
            Toast.makeText(this, "No se logro realizar la peticion de forma correcta", Toast.LENGTH_LONG).show();
        }
    }

    private void realizarPeticion(JsonObject request)
    {
        try
        {
            InputStream privateCrt = getResources().openRawResource(R.raw.certificado_android_pfx);
            InputStream certChain = getResources().openRawResource(R.raw.certificado_android_jks);
            final HttpsPostRequest peticion = new HttpsPostRequest(request, this, privateCrt, certChain);
            peticion.execute(Constantes.URL_BASE);
        }
        catch (UnsupportedOperationException ex)
        {
            Log.d("realizarPeticion", "Request: " + ex.getMessage());
        }
    }


    @Override
    public void obtenerRespuesta(Boolean success, String object) {
        Log.d("ObtenerRespuesta", "No funciona este metodo");
    }
}