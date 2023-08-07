package com.react.biometric.orquestador;

public class Biometria {
    private String biometryName;
    private String biometryRawDataType;
    private String rawData;

    public String getBiometryName() {
        return biometryName;
    }

    public void setBiometryName(String biometryNameParam) {
        biometryName = biometryNameParam;
    }

    public String getBiometryRawDataType() {
        return biometryRawDataType;
    }

    public void setBiometryRawDataType(String biometryRawDataTypeParam) {
        biometryRawDataType = biometryRawDataTypeParam;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawDataParam) {
        rawData = rawDataParam;
    }
}
