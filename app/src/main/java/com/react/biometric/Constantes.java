package com.react.biometric;

import android.widget.Toast;

import com.react.Biometric.BuildConfig;

public final class Constantes
{
    private Constantes(){
        throw new IllegalArgumentException("El dato que se pasa o se referencia no es correcto.");
    }
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    public static final String OPTION_MODALIDAD = "modalidad";
    public static final String FORM_MODALIDAD = "form";
    public static final String USER_CHECKED = "user_checked";
    public static final String USER_ID = "user_id";
    public static final String USER_PNAMES = "user_name";
    public static final String USER_PLNAMES = "user_lname";
    public static final String CERRO_SESION = "cerro_sesion";

    public static final String REQUEST_NID = "req_nid";
    public static final String REQUEST_NAME = "req_name";
    public static final String REQUEST_LAST_NAME = "req_last_name";
    public static final String REQUEST_SEX = "req_sex";
    public static final String REQUEST_COUNTRY = "req_country";
    public static final String REQUEST_BIRTH = "req_birth";

    public static final String DESDE_PRINCIPAL = "principal";
    public static final String USER_SOCIAL_ID = "user_social_id";
    public static final String RESPONSE = "response";
    public static final String MODALIDAD_RESPUESTA = "modalidad_respuesta";
    public static final String SUCCESS = "success";
    public static final String IMEI = "IMEI";
    public static final String FIRST_OPEN = "FIRST_OPEN";
    public static final String DEMO = "DEMO";
    public static final String METHOD_SOURCE = "method_source";
    public static final String INTENTOS = "INTENTOS";
    public static final String TXT_EMAIL = "txt_email";
    public static final String CONSENTIMIENTO = "txt";
    public static final String PERSONAL_URL = "link";
    public static final String NOM_COMPLETO = "nom";
    public  static  final String URL_BASE = BuildConfig.URL_BASE;
    public  static  final String URL_PATH = BuildConfig.URL_PATH;

    public static final String DEMO_USER = "REACT001";
    public static final String PASSWORD_USER = "$tr@!ght1928";

    public static final String CONTRASENNA_CERT = "1Pass1q2w3e4r";

    public static final int VERIDACTILAR = 0;
    public static final int VERIDACTILARCOMPLETO = 3;
    public static final int VERIDACTILARDEMO = 0;
    public static final int VERIFACIAL = 1;
    public static final int VERIFACIALCOMPLETO = 4;
    public static final int VERIFACIALDEMO = 1;
    public static final int VERICONSENTIMIENTO = 16;

    public static final int TOASTDURATION = Toast.LENGTH_SHORT;
    public static final String MODALIDADENROLA = "ENROLA";
    public static final String MODALIDADVALIDA = "VALIDA";

    public static final String FORMFACIAL = "ROSTRO";
    public static final String FORMHUELLA = "HUELLA";
    public static final String FORMDOCUMENTO = "DOCUMENRTO";

    public static final String VERI_DOCMENTOS = "20";
    public static final String VERI_FACIAL = "20";
    public static final String VERI_HUELLA = "20";

    public static final String ENROLL_DOCMENTOS = "30";
    public static final String ENROLL_FACIAL = "30";
    public static final String ENROLL_HUELLA = "30";

    public static final Boolean ESDESARROLLO = true;

}