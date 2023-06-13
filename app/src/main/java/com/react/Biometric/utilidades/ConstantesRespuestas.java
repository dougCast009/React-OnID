package com.react.Biometric.utilidades;

public class ConstantesRespuestas
{
    public enum IncludeLista {
        /*No extranjeros*/
        URLPersonal("URLPersonal"),
        FechaVencimiento("vFechaVencimiento"),
        Nombre("vNombre"),
        Mensaje("vMensaje"),
        Nacimiento("vFNacimiento"),
        Provincia("vProvincia"),
        Canton("vCanton"),
        Sexo("vSexo"),
        /**/
        Apellido("apellido1"),
        Apellido2("apellido2"),
        vApellido("vApellido1"),
        vApellido2("vApellido2")
        /*Extranjeros*/,
        NombreExtranjero("NOMBRE"),
        SexoExtranjero("SEXO"),
        Categoria("CATEGORIA"),
        Nacionalidad("NACIONALIDAD"),
        Fecha_Emision("FECHA_EMISION"),
        Fecha_Vencimiento("FECHA_VENCIMIENTO"),
        /**/
        /*Completo*/
        Edad("edad"),
        EdadMadre("EdadMadre"),
        EdadPadre("EdadPadre"),
        FechaNacimiento("Fecha_Nacimiento"),
        Genero("Genero"),
        NacionalidadMadre("Nacionalidad_Madre"),
        NacionalidadPadre("Nacionalidad_Padre"),
        NombreApellidosMadre("Nombre_Apellidos_Madre"),
        NombreApellidosPadre("Nombre_Apellidos_Padre"),
        /**/
        /*Orden Patronal*/
        OCUPACION("ocupacion"),
        NUMPATRONO("numPatrono"),
        VALIDEZ("validez"),
        AVISO_IMPORTANTE("avisoimportante"),
        AVISO_UNO("aviso1"),
        AVISO_DOS("aviso2"),
        AVISO_TRES("aviso3"),
        SUBSIDIO("subsidio");
        /**/

        private String include;

        IncludeLista(String stringToInclude) {
            this.include = stringToInclude;
        }

        public String getStringInclude() {
            return include;
        }
    }

    public enum ExcludeLista {
        FOTO("FOTO"),
        FIRMA("FIRMA");

        private String exclude;

        ExcludeLista(String stringToExclude) {
            this.exclude = stringToExclude;
        }

        public String getStringExclude() {
            return exclude;
        }
    }

    public enum ResponsesToInclude {
        resultVIDConsultarCedula("resultVIDConsultarCedula"),
        resultVIDConfirmacionHuella("resultVIDConfirmacionHuella"),
        resultConsultaPSI("resultConsultaPSI"),
        resultBuscarXCedulaInterdata("resultBuscarXCedulaInterdata"),
        ObtenerInfoPersonaResult("ObtenerInfoPersonaResult");

        private final String include;

        ResponsesToInclude(String stringToInclude) {
            this.include = stringToInclude;
        }

        public String getStringInclude() {
            return include;
        }
    }
}
