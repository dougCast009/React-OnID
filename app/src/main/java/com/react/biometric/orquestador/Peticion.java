package com.react.biometric.orquestador;
import java.util.List;

public class Peticion {
    private String CUSTOMERID;
    private String PASS;
    private String MethodAuth;
    private String IMEI;
    private String NID;
    private String Nombres;
    private String Apellidos;
    private String Sexo;
    private String Nacionalidad;
    private String FechaNacimiento;
    private List<String> MethodSource;
    private List<Biometria> Biometrics;

    public String getCUSTOMERID() {
        return CUSTOMERID;
    }

    public void setCUSTOMERID(String CUSTOMERID) {
        this.CUSTOMERID = CUSTOMERID;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }

    public String getMethodAuth() {
        return MethodAuth;
    }

    public void setMethodAuth(String methodAuth) {
        MethodAuth = methodAuth;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public List<String> getMethodSource() {
        return MethodSource;
    }

    public void setMethodSource(List<String> methodSource) {
        MethodSource = methodSource;
    }

    public List<Biometria> getBiometrics() {
        return Biometrics;
    }

    public void setBiometrics(List<Biometria> biometrics) {
        Biometrics = biometrics;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getNacionalidad() {
        return Nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        Nacionalidad = nacionalidad;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }
}
