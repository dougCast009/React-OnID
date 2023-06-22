package com.react.Biometric.orquestador;
import java.util.List;

public class Peticion {
    private String customerid;
    private String pass;
    private String methodAuth;
    private String imei;
    private String nid;
    private String nombres;
    private String apellidos;
    private String sexo;
    private String nacionalidad;
    private String fechaNacimiento;
    private List<String> methodSource;
    private List<Biometria> biometrics;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getMethodAuth() {
        return methodAuth;
    }

    public void setMethodAuth(String methodAuth) {
        this.methodAuth = methodAuth;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public List<String> getMethodSource() {
        return methodSource;
    }

    public void setMethodSource(List<String> methodSource) {
        this.methodSource = methodSource;
    }

    public List<Biometria> getBiometrics() {
        return biometrics;
    }

    public void setBiometrics(List<Biometria> biometrics) {
        this.biometrics = biometrics;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
