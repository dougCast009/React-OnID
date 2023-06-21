package com.react.biometric.orquestador;

public class OrqResponse {
    public OrqObtenerInfoPersonaResult ObtenerInfoPersonaResult;

    public OrqResponse (OrqObtenerInfoPersonaResult obtenerInfoPersonaResult) {
        this.ObtenerInfoPersonaResult = obtenerInfoPersonaResult;
    }
}
