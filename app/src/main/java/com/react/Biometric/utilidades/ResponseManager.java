package com.react.Biometric.utilidades;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.react.Biometric.orquestador.OrqResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResponseManager
{
    public static OrqResponse ObtenerObjetoRespuesta(String Json) {
        Gson gson = new Gson();
        OrqResponse resp = gson.fromJson(Json, OrqResponse.class);
        return resp;
    }

    List<Map<String, String>> lista = new ArrayList<>();

    /**
     * Convierte la respuesta del servidor, a sea en lista o en Cedula/Dimex
     **/
    public List<Map<String, String>> jsonToList(String stringResultado, String error, int modalidad, boolean completo, boolean extranjero) {

        try {
            ArrayList<String> resultados = getInnerContent(stringResultado);
            if (resultados.isEmpty()) {
                return null;
            }else {
                for (String resultado: resultados) {
                    recursiveAddtoList(lista, resultado, "");
                }
            }

        } catch (Exception e) {
            Map<String, String> mapa = new HashMap<String, String>() {
            };
            lista.clear();
            mapa.put("error", error);
            lista.add(mapa);
        }

        return lista;
    }

    public static String extraeDigitalID(String object) {
        JsonElement element = JsonParser.parseString(object);
        JsonObject obj = element.getAsJsonObject(); //la respuesta del servidor es un json

        if (!obj.get("ObtenerInfoPersonaResult").isJsonNull()) {
            JsonObject objResult = obj.getAsJsonObject("ObtenerInfoPersonaResult");
            if (objResult.has("DigitalID")) {
                String digitalID = objResult.get("DigitalID").toString();
                if (!(digitalID.equalsIgnoreCase("") || digitalID.contains("Error"))){
                    return digitalID;
                }
            }
        }

        return "";
    }

    public static String getPersonalURL(String Response)
    {
        try {
            JsonElement element = JsonParser.parseString(Response);
            JsonObject obj = element.getAsJsonObject();

            if (!obj.get("ObtenerInfoPersonaResult").isJsonNull())
            {
                JsonObject objResult = obj.getAsJsonObject("ObtenerInfoPersonaResult");
                if (objResult.has("URLPersonal"))
                {
                    //DIRECCION ACTIVACION PERFIL PERSONAL
                    String BaseURL = objResult.get("URLPersonal").toString();
                    String Personal = BaseURL.replace("\"", "");
                    String PersonalURL = Personal;
                    if (!(PersonalURL.equalsIgnoreCase("")))
                    {
                        return PersonalURL;
                    }
                }
            }

            return "";
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getConsentimiento(String Response)
    {
        try {
            JsonElement element = JsonParser.parseString(Response);
            JsonObject obj = element.getAsJsonObject();

            if (!obj.get("ObtenerInfoPersonaResult").isJsonNull())
            {
                JsonObject objResult = obj.getAsJsonObject("ObtenerInfoPersonaResult");
                if (objResult.has("Consentimiento"))
                {
                    //CONSENTIMIENTO
                    String BaseURL = objResult.get("Consentimiento").toString();
                    String Personal = BaseURL.replace("\"", "");
                    if (!(Personal.equalsIgnoreCase("")))
                    {
                        return Personal;
                    }
                }
            }

            return "";
        } catch (Exception ex) {
            return null;
        }
    }

    private ArrayList<String> getInnerContent(String object) {

        ArrayList<String> innerDataResult = new ArrayList<String>();

        JsonElement element = JsonParser.parseString(object);
        JsonObject obj = element.getAsJsonObject(); //la respuesta del servidor es un json

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {
            if (entry.getKey().equalsIgnoreCase("ObtenerInfoPersonaResult")) {
                //Se repite el mismo proceso para la respuesta interna
                obj = entry.getValue().getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> innerEntries = obj.entrySet();

                for (Map.Entry<String, JsonElement> innerEntry : innerEntries) {
                    for (ConstantesRespuestas.ResponsesToInclude include : ConstantesRespuestas.ResponsesToInclude.values()) {
                        if (innerEntry.getKey().contains(include.getStringInclude())) {
                            JsonObject innerobj = innerEntry.getValue().getAsJsonObject();

                            innerDataResult.add(innerobj.get("response").toString());
                        }else if (innerEntry.getKey().toLowerCase().contains("Patronal")){
                            JsonObject innerobj = innerEntry.getValue().getAsJsonObject();

                            innerDataResult.add(innerobj.get("result").toString());
                        }
                    }
                }
            }
        }
        return innerDataResult;
    }


    //Recibe elemento Json y añade cada miembro a un mapa (para visualizar el resultado en pares en un listview)
    private void recursiveAddtoList(List<Map<String, String>> lista, String object, String titulo) {
        JsonElement element = JsonParser.parseString(object);

        JsonObject obj = element.getAsJsonObject();

        Map<String, String> mapa;

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object
        for (Map.Entry<String, JsonElement> entry : entries) {

            if (entry.getValue().isJsonObject()) {
                recursiveAddtoList(lista, entry.getValue().toString(), entry.getKey() + "-");

            } else {
                //Excluye de la lista a los elementos de 'Huella' y 'Error, Errordescription'
                for (ConstantesRespuestas.IncludeLista include : ConstantesRespuestas.IncludeLista.values()) {
                    if (entry.getKey().toLowerCase().equalsIgnoreCase(include.getStringInclude())) {
                        if (!(entry.getValue().toString().isEmpty() || entry.getValue().isJsonNull())) {
                            if (checkNull(entry.getValue().toString())){
                                mapa = new HashMap<String, String>();

                                String key = entry.getKey().replaceAll("^[viVI]", "").trim();
                                key = key.replaceAll("[_]", " ");
                                String value = entry.getValue().toString().trim();
                                value = value.replaceAll("[\"]", "");

                                mapa.put(titulo + key, value);

                                lista.add(mapa);
                            }
                        }
                    }
                }
                //Si se encuentra con error en este punto, es un "no-match" por lo que retorna lista vacía
                if (entry.getKey().equals("ErrorCode")) {
                    if (!entry.getValue().toString().contains("00")) {
                        lista.clear();
                        return;
                    }

                }

            }

        }

    }

    /**
     * Jose Acuña - 16/04/2020
     * Verifica si el valor es null o alguna otra representación del valor que utilice el servidor
     * @param value valor a verificar
     * @return verdadero si el valor NO es null, falso si es al contrario
     */
    private boolean checkNull(String value){
        return !(value.equals("") || value.equals("0") || value.contains("null"));
    }
}
