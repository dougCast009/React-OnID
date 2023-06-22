package com.react.Biometric.orquestador;

public class Biometria {
    private String BiometryName;
    private String BiometryRawDataType;
    private String RawData;

    public String getBiometryName() {
        return BiometryName;
    }

    public void setBiometryName(String biometryName) {
        BiometryName = biometryName;
    }

    public String getBiometryRawDataType() {
        return BiometryRawDataType;
    }

    public void setBiometryRawDataType(String biometryRawDataType) {
        BiometryRawDataType = biometryRawDataType;
    }

    public String getRawData() {
        return RawData;
    }

    public void setRawData(String rawData) {
        RawData = rawData;
    }
}
