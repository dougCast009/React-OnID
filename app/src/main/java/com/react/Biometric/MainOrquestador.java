package com.react.Biometric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.react.Biometric.interfaces.CustomCallback;
import com.react.Biometric.orquestador.Biometria;
import com.react.Biometric.orquestador.OrqResponse;
import com.react.Biometric.orquestador.Peticion;
import com.react.Biometric.utilidades.HttpsPostRequest;
import com.react.Biometric.utilidades.ResponseManager;

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
            request.setCUSTOMERID("xpi");
            request.setPASS("$tr@!ght1928");
            request.setNID("201110111");
            request.setMethodAuth("30");
            request.setNombres("COPPER");
            request.setApellidos("BRAND");
            request.setBiometrics(biometrics);

            Gson gson = new Gson();
            JsonObject jsonRequest = JsonParser.parseString(gson.toJson(request)).getAsJsonObject();
            realizarPeticion(jsonRequest);
        }
        catch (Exception ex) {
            //MOSTRAR ERROR
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
            Log.d("realizarPeticion", "Request: " + ex.getMessage());
        }
    }

    @Override
    public void ObtenerRespuesta(Boolean success, String object) {
        runOnUiThread(() -> {
            /*findViewById(R.id.progress_overlay).setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            findViewById(R.id.toolbarMain).setVisibility(View.VISIBLE);*/
        });
        try {
            OrqResponse respuesta = ResponseManager.obtenerObjetoRespuesta(object);

            if (respuesta != null)
            {
                if (Integer.parseInt(respuesta.ObtenerInfoPersonaResult.ErrorCode) == 00)
                {

                }
                else
                {

                    runOnUiThread(() -> {
                        /*txtIdentificacion.setEnabled(true);
                        showToast(Mensaje);
                        if (requestMethod == Constantes.VeriDactilar
                                || requestMethod == Constantes.VeriDactilarCompleto
                                || requestMethod == Constantes.VeriDactilarDemo)
                        {
                            redirectToActivity(MainFinger.class);
                        }*/
                    });
                }
            }
            else
            {
                runOnUiThread(() -> {
                    /*txtIdentificacion.setEnabled(true);
                    showToast("Ha ocurrido un error al procesar la respuesta.");*/
                });
            }

        } catch (Exception e) {
            Log.d("Verificar", "Request: " + e.getMessage());
        }
    }
}