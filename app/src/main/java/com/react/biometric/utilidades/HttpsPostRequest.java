package com.react.biometric.utilidades;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.react.biometric.Constantes;
import com.react.biometric.interfaces.CustomCallback;


public class HttpsPostRequest extends AsyncTask<String,Void,String>
{

    public static final int READ_TIMEOUT = 180000;
    public static final int CONNECTION_TIMEOUT = 180000;
    public static final String CONTENT_TYPE = "application/json";
    private CustomCallback callback;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private InputStream privateCert;
    private InputStream caCrt;
    JsonObject postData;

    public HttpsPostRequest(JsonObject postData, CustomCallback callback, InputStream privateCert, InputStream caCrt) {
        if (postData != null) {
            try {
                this.postData = postData;
                this.privateCert = privateCert;
                this.caCrt = caCrt;
            } catch (UnsupportedOperationException e) {
                Log.e("BadRequest", "Problemas al ejecutar la solicitud al servidor");
            }
        }
        this.callback = callback;
    }

    /*@Deprecated
    Change this code in phase 2 */
    @Override
    protected String doInBackground(String... params) {
        String stringUrl = "https://" + params[0];
        String result = "";
        try {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .followSslRedirects(true)
                    .connectTimeout(CONNECTION_TIMEOUT, timeUnit)
                    .readTimeout(READ_TIMEOUT, timeUnit)
                    //.sslSocketFactory(sslContext.getSocketFactory(),customTrust)
                    .build();

            RequestBody requestBody = RequestBody.create(JSON, postData.toString());
            Request request = new Request.Builder()
                    .url(stringUrl + Constantes.URL_PATH)
                    .post(requestBody)
                    .headers(getHeaders(params[0],String.valueOf(postData.toString().length())))
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            int statusCode = response.code();
            if (statusCode == 200) {
                result = response.body().string();
                callback.obtenerRespuesta(true, result);
            } else if (statusCode == 400){
                callback.obtenerRespuesta(false, "400");
            }

        } catch (Exception e) {
            callback.obtenerRespuesta(false, result);
            Log.d("doInBackground", "Error: " + e.getMessage());
        }
        return null;
    }

    private Headers getHeaders(String host, String contentLength){
        return new Headers.Builder()
                .add("Content-Type",CONTENT_TYPE)
                //.add("User-Agent", "Android Client")
                .add("Host", host)
                //.add("Connection", "Keep-Alive")
                .add("Content-Length", contentLength)
                //.add("Accept-Encoding", "gzip, deflate")
                //.add("Expect", "100-Continue")
                //.add("Accept-Language", "en")
                .build();
    }

}
