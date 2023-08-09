package com.react.biometric.orquestador;

public class Biometria {
    private String BiometryName;
    private String BiometryRawDataType;
    private String RawData;

    public String getBiometryName() {
        return BiometryName;
    }

    public void setBiometryName(String BiometryNameParam) {
        BiometryName = BiometryNameParam;
    }

    public String getBiometryRawDataType() {
        return BiometryRawDataType;
    }

    public void setBiometryRawDataType(String biometryRawDataTypeParam) {
        BiometryRawDataType = biometryRawDataTypeParam;
    }

    public String getRawData() {
        return RawData;
    }

    public void setRawData(String rawDataParam) {
        RawData = rawDataParam;
    }
}
