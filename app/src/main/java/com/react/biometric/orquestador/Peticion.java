package com.react.biometric.orquestador;
import java.util.List;

public class Peticion {
    private String Customerid;
    private String Pass;
    private String MethodAuth;
    private String Imei;
    private String Nid;
    private String Nombres;
    private String Apellidos;
    private String Sexo;
    private String Nacionalidad;
    private String FechaNacimiento;
    private List<String> MethodSource;
    private List<Biometria> Biometrics;

    public String getCustomerid() {
        return Customerid;
    }

    public void setCustomerid(String customerid) {
        this.Customerid = customerid;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        this.Pass = pass;
    }

    public String getNid() {
        return Nid;
    }

    public void setNid(String Nid) {
        this.Nid = Nid;
    }

    public String getMethodAuth() {
        return MethodAuth;
    }

    public void setMethodAuth(String MethodAuth) {
        this.MethodAuth = MethodAuth;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String Imei) {
        this.Imei = Imei;
    }

    public List<String> getMethodSource() {
        return MethodSource;
    }

    public void setMethodSource(List<String> MethodSource) {
        this.MethodSource = MethodSource;
    }

    public List<Biometria> getBiometrics() {
        return Biometrics;
    }

    public void setBiometrics(List<Biometria> Biometrics) {
        this.Biometrics = Biometrics;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public String getNacionalidad() {
        return Nacionalidad;
    }

    public void setNacionalidad(String Nacionalidad) {
        this.Nacionalidad = Nacionalidad;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }
}
