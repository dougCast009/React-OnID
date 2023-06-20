package com.react.Biometric.utilidades;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.react.Biometric.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Helper {
    private Helper(){
        throw new IllegalArgumentException("El dato que se pasa o se referencia no es correcto.");
    }
    private static final String TAG = "Helper";

    public static String obtenerConfigValue(Context context, String nombre) {
        Resources resources = context.getResources();

        try {
            InputStream archivoConfig = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(archivoConfig);
            return properties.getProperty(nombre);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "No se localizó el archivo de configuración: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Problemas al abrir el archivo de configuración");
        }

        return null;
    }
}
