package com.react.Biometric.utilidades;

public class ConstantesRespuestas
{
    public enum IncludeLista {
        /*No extranjeros*/
        URLPERSONAL("URLPersonal"),
        FECHAVENCIMIENTO("vFechaVencimiento"),
        NOMBRE("vNombre"),
        MENSAJE("vMensaje"),
        NACIMIENTO("vFNacimiento"),
        PROVINCIA("vProvincia"),
        CANTON("vCanton"),
        SEXO("vSexo"),
        /**/
        APELLIDO("apellido1"),
        APELLIDO2("apellido2"),
        VAPELLIDO("vApellido1"),
        VAPELLIDO2("vApellido2")
        /*Extranjeros*/,
        NOMBREEXTRANJERO("NOMBRE"),
        SEXOEXTRANJERO("SEXO"),
        CATEGORIA("CATEGORIA"),
        NACIONALIDAD("NACIONALIDAD"),
        FEHA_EMISION("FECHA_EMISION"),
        FECHA_VENCIMIENTO("FECHA_VENCIMIENTO"),
        /**/
        /*Completo*/
        EDAD("edad"),
        EDADMADRE("EdadMadre"),
        EDADPADRE("EdadPadre"),
        FECHANACIMIENTO("Fecha_Nacimiento"),
        GENERO("Genero"),
        NACIONALIDADMADRE("Nacionalidad_Madre"),
        NACIONALIDADPADRE("Nacionalidad_Padre"),
        NOMBREAPELLIDOSMADRE("Nombre_Apellidos_Madre"),
        NOMBREAPELLIDOSPADRE("Nombre_Apellidos_Padre"),
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
        RESULTVIDCONSULTARCEDULA("resultVIDConsultarCedula"),
        RESULTVIDCONFIRMACIONHUELLA("resultVIDConfirmacionHuella"),
        RESULTCONSULTAPSI("resultConsultaPSI"),
        RESULTBUSCARXCEDULAINTERDATA("resultBuscarXCedulaInterdata"),
        OBTENERINFOPERSONARESULT("ObtenerInfoPersonaResult");

        private final String include;

        ResponsesToInclude(String stringToInclude) {
            this.include = stringToInclude;
        }

        public String getStringInclude() {
            return include;
        }
    }
}
