package com.react.Biometric.utilidades;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.react.Biometric.Constantes;
import com.react.Biometric.interfaces.CustomCallback;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = "https://" + params[0];
        String result = "";
        try {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .followSslRedirects(true)
                    .connectTimeout((long) CONNECTION_TIMEOUT, timeUnit)
                    .readTimeout((long) READ_TIMEOUT, timeUnit)
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
                callback.ObtenerRespuesta(true, result);
            } else if (statusCode == 400){
                callback.ObtenerRespuesta(false, "400");
            }

        } catch (Exception e) {
            callback.ObtenerRespuesta(false, result.toString());
            Log.d("doInBackground", "Excepcion: " + e.getMessage());
        }
        return null;
    }

    private Headers getHeaders(String host, String contentLength){
        Headers headers = new Headers.Builder()
                .add("Content-Type",CONTENT_TYPE)
                //.add("User-Agent", "Android Client")
                .add("Host", host)
                //.add("Connection", "Keep-Alive")
                .add("Content-Length", contentLength)
                //.add("Accept-Encoding", "gzip, deflate")
                //.add("Expect", "100-Continue")
                //.add("Accept-Language", "en")
                .build();
        return headers;
    }

    private KeyManager[] getKeyManager(String password){
        KeyManager[] keyManagers = null;
        try{
            //Certificado Privado
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(privateCert, password.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(keyStore, password.toCharArray());
            keyManagers = kmf.getKeyManagers();
        }catch (Exception e){
            Log.d("getKeyManager", "Excepcion: " + e.getMessage());
        }
        return keyManagers;
    }

    private TrustManager[] getTrustManager(){
        TrustManager[] trustManagers = null;
        try{
            //TrustStore con cadena de certificados
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(caCrt);

            String alias = cert.getSubjectX500Principal().getName();
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null);
            trustStore.setCertificateEntry(alias, cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(trustStore);
            trustManagers = tmf.getTrustManagers();
        } catch (Exception e) {
            Log.d("getTrustManager", "Excepcion: " + e.getMessage());
        }
        return trustManagers;
    }

    private X509TrustManager getx509Trust(TrustManager[] trustManagers){
        X509TrustManager customTm = null;
        try{
            X509TrustManager x509Tm = null;
            for (TrustManager tm : trustManagers) {
                if (tm instanceof X509TrustManager) {
                    x509Tm = (X509TrustManager) tm;
                    break;
                }
            }

            // Convierte TrustManager a X509TrustManager
            final X509TrustManager finalTm = x509Tm;
            customTm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return finalTm.getAcceptedIssuers();
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    finalTm.checkServerTrusted(chain, authType);
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    finalTm.checkClientTrusted(chain, authType);
                }
            };
        }catch (Exception e){
            Log.d("getx509Trust", "Excepcion: " + e.getMessage());
        }
        return customTm;
    }
}
