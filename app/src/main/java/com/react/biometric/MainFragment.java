package com.react.biometric;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.enums.eGraphicFieldType;
import com.regula.documentreader.api.enums.eVisualFieldType;
import com.regula.documentreader.api.results.DocumentReaderResults;
import com.regula.documentreader.api.enums.Scenario;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.enums.ImageType;
import com.regula.facesdk.enums.LivenessStatus;
import com.regula.facesdk.model.MatchFacesImage;
import com.regula.facesdk.model.results.matchfaces.MatchFacesComparedFacesPair;
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit;
import com.regula.facesdk.request.MatchFacesRequest;

public class MainFragment extends Fragment {

    //BOTONES
    private Button btnTabCapturar;
    private Button btnTabInfo;
    private Button btnIniciarCaptura;
    private Button btnIniciarProceso;
    //LAYOUTS
    private LinearLayout tabInformacion;
    private LinearLayout tabEscaner;
    //CAMPOS DE TEXTO
    private TextView txtEstado;
    private TextView txtDcumento;
    private TextView txtIdentificacion;
    private TextView txtNombres;
    private TextView txtApellidos;
    private TextView txtSexo;
    private TextView txtEdad;
    private TextView txtFechaNacimiento;
    private TextView txtLugarNacimiento;
    private TextView txtDomicilio;
    private TextView txtNombreMadre;
    private TextView txtNombrePadre;
    private TextView txtMeses;
    private TextView txtVencimiento;
    private TextView txtCodPais;
    private TextView txtPaisOrigen;
    private TextView txtDmxExpediente;
    private TextView txtDmxCodNacionalidad;
    private TextView txtDmxNacionalidad;
    private TextView txtDmxEmision;
    private LinearLayout lblDocumento;
    private LinearLayout lblLugarNacimiento;
    private LinearLayout lblDomicilioElectoral;
    private LinearLayout lblNombreMadre;
    private LinearLayout lblNombrePadre;
    private LinearLayout lblDmxExpediente;
    private LinearLayout lblDmxCodNacionalidad;
    private LinearLayout lblDmxNacionalidad;
    private LinearLayout lblDmxEmision;
    //CAMPOS DE IMAGENES
    private ImageView imgFoto;
    private ImageView imgFirma;
    private ImageView imgDocumento1;
    private ImageView imgDocumento2;

    private LinearLayout mainContent;
    private LinearLayout tabRespuesta;
    private ImageView imgResultado;
    private TextView txtContinuar;
    private TextView textoPrincipal;
    private Toolbar toolbar;
    //ANTIGUOS
    private TextView showScanner;
    private TextView recognizeImage;
    private TextView recognizePdf;
    private MainCallbacks mCallbacks;
    public static final int RFIDRESULT = 100;
    private Boolean estadoDocumento = false;

    //Paleta Colores
    private static final String TXTCOLORPRINCIPAL = "#1A83C4";
    private static final String TXTCOLORSECUNDARIO = "#B33A3A";
    private static final String TXTCOLORNEGRO = "#000000";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        btnTabCapturar = root.findViewById(R.id.btnTabCapturar);
        btnTabInfo = root.findViewById(R.id.btnTabInfo);
        btnIniciarCaptura = root.findViewById(R.id.btnIniciarCaptura);
        btnIniciarProceso = root.findViewById(R.id.btnIniciarProceso);
        tabInformacion = root.findViewById(R.id.TabInformacion);
        tabEscaner = root.findViewById(R.id.TabEscaner);
        txtEstado = root.findViewById(R.id.txtEstado);
        txtDcumento = root.findViewById(R.id.txtDocumento);
        txtIdentificacion = root.findViewById(R.id.txtIdentificacion);
        txtNombres = root.findViewById(R.id.txtNombres);
        txtApellidos = root.findViewById(R.id.txtApellidos);
        txtSexo = root.findViewById(R.id.txtSexo);
        txtEdad = root.findViewById(R.id.txtEdad);
        txtFechaNacimiento = root.findViewById(R.id.txtFechaNacimiento);
        txtLugarNacimiento = root.findViewById(R.id.txtLugarNacimiento);
        txtDomicilio = root.findViewById(R.id.txtDomicilio);
        txtNombreMadre = root.findViewById(R.id.txtNombreMadre);
        txtNombrePadre = root.findViewById(R.id.txtNombrePadre);
        txtMeses = root.findViewById(R.id.txtMeses);
        txtVencimiento = root.findViewById(R.id.txtVencimiento);
        txtCodPais = root.findViewById(R.id.txtCodPais);
        txtPaisOrigen = root.findViewById(R.id.txtPaisOrigen);
        txtDmxExpediente = root.findViewById(R.id.txtDmxExpediente);
        txtDmxCodNacionalidad = root.findViewById(R.id.txt_dmx_cod_nacionalidad);
        txtDmxNacionalidad = root.findViewById(R.id.txt_dmx_nacionalidad);
        txtDmxEmision = root.findViewById(R.id.txt_dmx_emision);

        lblDocumento = root.findViewById(R.id.lbl_documento);
        lblLugarNacimiento = root.findViewById(R.id.lbl_lugar_nacimiento);
        lblDomicilioElectoral = root.findViewById(R.id.lbl_domicilio_electoral);
        lblNombreMadre = root.findViewById(R.id.lbl_nombre_madre);
        lblNombrePadre = root.findViewById(R.id.lbl_nombre_padre);
        lblDmxExpediente = root.findViewById(R.id.lbl_dmx_expediente);
        lblDmxCodNacionalidad = root.findViewById(R.id.lbl_dmx_cod_nacionalidad);
        lblDmxNacionalidad = root.findViewById(R.id.lbl_dmx_nacionalidad);
        lblDmxEmision = root.findViewById(R.id.lbl_dmx_emision);

        imgFoto = root.findViewById(R.id.img_foto);
        imgFirma = root.findViewById(R.id.img_firma);
        imgDocumento1 = root.findViewById(R.id.img_documento_1);
        imgDocumento2 = root.findViewById(R.id.img_documento_2);
        mostrarTabCaptura();
        showScanner = root.findViewById(R.id.showScannerLink);
        recognizeImage = root.findViewById(R.id.recognizeImageLink);
        recognizePdf = root.findViewById(R.id.recognizePdfLink);

        mainContent = root.findViewById(R.id.MainContent);
        tabRespuesta = root.findViewById(R.id.TabRespuesta);
        imgResultado = root.findViewById(R.id.img_resultado);
        txtContinuar = root.findViewById(R.id.txt_continuar);
        textoPrincipal = root.findViewById(R.id.texto_principal);
        toolbar = root.findViewById(R.id.toolbarMain);

        initView();
        return root;
    }

    @Override
    public void onResume() {//used to show scenarios after fragments transaction
        super.onResume();
        if (getActivity() != null && DocumentReader.Instance().isReady() && (!DocumentReader.Instance().availableScenarios.isEmpty()))
            ((BaseActivity) getActivity()).setScenarios();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (MainCallbacks) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void mostrarTabCaptura() {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        tabInformacion.setVisibility(View.GONE);
        tabEscaner.setVisibility(View.VISIBLE);
    }

    private void mostrarTabInformacion() {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        tabInformacion.setVisibility(View.VISIBLE);
        tabEscaner.setVisibility(View.GONE);
    }

    private void initView() {
        txtContinuar.setOnClickListener(view -> {
            tabRespuesta.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.VISIBLE);
        });

        btnTabCapturar.setOnClickListener(view -> mostrarTabCaptura());

        btnTabInfo.setOnClickListener(view -> mostrarTabInformacion());

        btnIniciarCaptura.setOnClickListener(view -> {
            mCallbacks.scenarioLv(Scenario.SCENARIO_FULL_AUTH);
            limpiarCampos();
            mCallbacks.showScanner();
        });
    }

    private void setImgFoto(DocumentReaderResults results) {
        Bitmap portrait = results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT);
        if (portrait != null) {
            this.imgFoto.setImageBitmap(portrait);
        }
    }

    private void setImgFirma(DocumentReaderResults results) {
        Bitmap firmaImg = results.getGraphicFieldImageByType(eGraphicFieldType.GF_SIGNATURE);
        if (imgFirma != null) {
            imgFirma.setImageBitmap(firmaImg);
        }
    }

    private void setImgDocumento1(DocumentReaderResults results) {
        Bitmap imgDoc1 = results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE);
        if (imgDoc1 != null) {
            imgDocumento1.setImageBitmap(imgDoc1);
        }
    }

    private void setImgDocumento2(DocumentReaderResults results) {
        Bitmap imgDoc2 = results.getGraphicFieldImageByType(eGraphicFieldType.GF_OTHER);
        if (imgDoc2 != null) {
            imgDocumento2.setImageBitmap(imgDoc2);
        }
    }

    private void chargeTextFields(DocumentReaderResults results) {
        //CARGAR CAMPOS DE TEXTO
        txtCodPais.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_ISSUING_STATE_CODE)));
        txtDcumento.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_PERSONAL_NUMBER)));
        String extra = validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_OPTIONAL_DATA));
        String identificacion = extra.equals(getString(R.string.document_pending)) ? validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DOCUMENT_NUMBER)) : validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DOCUMENT_NUMBER) + extra);
        txtIdentificacion.setText(identificacion);
        txtNombres.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_GIVEN_NAMES)));
        txtApellidos.setText(validarNulosApellidos(results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME)) + " " + validarNulosApellidos(results.getTextFieldValueByType(eVisualFieldType.FT_SECOND_SURNAME)));
        txtSexo.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_SEX)));
        txtEdad.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_AGE)));
        txtFechaNacimiento.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_BIRTH)));
        txtLugarNacimiento.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_PLACE_OF_BIRTH)));
        txtDomicilio.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_PLACE_OF_REGISTRATION)));
        txtNombreMadre.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_MOTHER_GIVENNAME)));
        txtNombrePadre.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_FATHER_GIVENNAME)));
        String meses = validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_REMAINDER_TERM));
        txtMeses.setText(meses);
        txtVencimiento.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_EXPIRY)));
        txtPaisOrigen.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_ISSUING_STATE_NAME)));
        txtDmxExpediente.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_REG_CERT_REG_NUMBER)));
        txtDmxNacionalidad.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_NATIONALITY)));
        txtDmxEmision.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_ISSUE)));
        txtDmxCodNacionalidad.setText(validarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_NATIONALITY_CODE)));
        if (!meses.equals(getString(R.string.document_pending))) {
            if (Integer.parseInt(meses) > 0) {
                txtMeses.setTextColor(Color.parseColor(TXTCOLORPRINCIPAL));
            } else {
                txtMeses.setTextColor(Color.parseColor(TXTCOLORSECUNDARIO));
            }
        }

        if (identificacion.length() == 9) {
            lblDmxExpediente.setVisibility(View.GONE);
            lblDmxCodNacionalidad.setVisibility(View.GONE);
            lblDmxNacionalidad.setVisibility(View.GONE);
            lblDmxEmision.setVisibility(View.GONE);
        } else if (identificacion.length() == 12) {
            lblDocumento.setVisibility(View.GONE);
            lblLugarNacimiento.setVisibility(View.GONE);
            lblDomicilioElectoral.setVisibility(View.GONE);
            lblNombreMadre.setVisibility(View.GONE);
            lblNombrePadre.setVisibility(View.GONE);
        }
    }
    private void displayIdEstade(boolean mostrarTabRespuesta){
        if (Boolean.TRUE.equals(mostrarTabRespuesta)) {
            tabRespuesta.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            mainContent.setVisibility(View.GONE);
            textoPrincipal.setText(Boolean.TRUE.equals(estadoDocumento) ? getString(R.string.identidad_confirmada) : getString(R.string.identidad_no_confirmada));
            imgResultado.setImageResource(Boolean.TRUE.equals(estadoDocumento) ? R.drawable.document_check : R.drawable.document_cross);
        }

    }
    private void vitalTest(DocumentReaderResults results){
        FaceSDK.Instance().startLiveness(getActivity(), livenessResponse -> {
            if (livenessResponse.getLiveness() == LivenessStatus.PASSED) {

                List<MatchFacesImage> images = Arrays.asList(
                        new MatchFacesImage(livenessResponse.getBitmap(), ImageType.LIVE),
                        new MatchFacesImage(results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT), ImageType.PRINTED)
                );
                MatchFacesRequest request = new MatchFacesRequest(images);

                FaceSDK.Instance().matchFaces(request, response -> {

                    MatchFacesSimilarityThresholdSplit split = new MatchFacesSimilarityThresholdSplit(response.getResults(), 0.75);
                    List<MatchFacesComparedFacesPair> matched = split.getMatchedFaces();
                    List<MatchFacesComparedFacesPair> notmatched = split.getUnmatchedFaces();

                    if (!matched.isEmpty() && notmatched.isEmpty()) {
                        estadoDocumento = true;
                    } else {
                        estadoDocumento = false;
                    }

                });

            }
        });

    }
    private void displayMatchInfo(DocumentReaderResults results,boolean mostrarTabRespuesta){
        if (Boolean.TRUE.equals(validarFechaVencimiento(txtVencimiento.getText().toString()))) {
            estadoDocumento = true;
            txtEstado.setText(getString(R.string.result_valido));
            txtEstado.setTextColor(Color.parseColor(TXTCOLORPRINCIPAL));
            txtVencimiento.setTextColor(Color.parseColor(TXTCOLORPRINCIPAL));

            if (Boolean.TRUE.equals(mostrarTabRespuesta) && (imgFoto != null)) {
                vitalTest(results);
            }
        } else {
            txtEstado.setText(getString(R.string.result_no_valido));
            txtEstado.setTextColor(Color.parseColor(TXTCOLORSECUNDARIO));
            txtVencimiento.setTextColor(Color.parseColor(TXTCOLORSECUNDARIO));
            btnIniciarProceso.setVisibility(View.GONE);
        }
    }
    public void displayResults(DocumentReaderResults results) {

        if (results != null) {
            chargeTextFields(results);
            setImgFoto(results);
            setImgFirma(results);
            setImgDocumento1(results);
            setImgDocumento2(results);

            Boolean mostrarTabRespuesta = btnIniciarProceso.getText().equals(getString(R.string.verificar));

            displayMatchInfo(results,mostrarTabRespuesta);


            displayIdEstade(mostrarTabRespuesta);
            mostrarTabInformacion();
        } else {
            mostrarTabCaptura();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void disableUiElements() {
        recognizePdf.setClickable(false);
        showScanner.setClickable(false);
        recognizeImage.setClickable(false);

        recognizePdf.setTextColor(Color.GRAY);
        showScanner.setTextColor(Color.GRAY);
        recognizeImage.setTextColor(Color.GRAY);
    }

    private String validarNulos(String datos) {
        if (datos != null) {
            return datos;
        } else {
            return getString(R.string.document_pending);
        }
    }

    private String validarNulosApellidos(String datos) {
        if (datos != null) {
            return datos;
        } else {
            return "";
        }
    }

    private Boolean validarFechaVencimiento(String datos) {
        if (datos != null) {
            try {
                Date fechaVence = new SimpleDateFormat("yyyy-MM-dd").parse(datos);
                Date fechaActual = Calendar.getInstance().getTime();

                return fechaActual.before(fechaVence) || fechaActual.equals(fechaVence);
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    private void limpiarCampos() {
        txtEstado.setText(getString(R.string.document_pending));
        txtDcumento.setText(getString(R.string.document_pending));
        txtIdentificacion.setText(getString(R.string.document_pending));
        txtNombres.setText(getString(R.string.document_pending));
        txtApellidos.setText(getString(R.string.document_pending));
        txtSexo.setText(getString(R.string.document_pending));
        txtEdad.setText(getString(R.string.document_pending));
        txtFechaNacimiento.setText(getString(R.string.document_pending));
        txtLugarNacimiento.setText(getString(R.string.document_pending));
        txtNombreMadre.setText(getString(R.string.document_pending));
        txtNombrePadre.setText(getString(R.string.document_pending));
        txtMeses.setText(getString(R.string.document_pending));
        txtVencimiento.setText(getString(R.string.document_pending));
        txtCodPais.setText(getString(R.string.document_pending));
        txtDomicilio.setText(getString(R.string.document_pending));
        txtPaisOrigen.setText(getString(R.string.document_pending));
        txtDmxExpediente.setText(getString(R.string.document_pending));
        txtDmxNacionalidad.setText(getString(R.string.document_pending));
        txtDmxEmision.setText(getString(R.string.document_pending));
        txtDmxCodNacionalidad.setText(getString(R.string.document_pending));
        txtEstado.setTextColor(Color.parseColor(TXTCOLORNEGRO));
        txtMeses.setTextColor(Color.parseColor(TXTCOLORNEGRO));
        txtVencimiento.setTextColor(Color.parseColor(TXTCOLORNEGRO));
        imgFoto.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        imgFirma.setImageDrawable(getResources().getDrawable(R.drawable.writing));
        imgDocumento1.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        imgDocumento2.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        lblDmxExpediente.setVisibility(View.VISIBLE);
        lblDmxCodNacionalidad.setVisibility(View.VISIBLE);
        lblDmxNacionalidad.setVisibility(View.VISIBLE);
        lblDmxEmision.setVisibility(View.VISIBLE);
        lblDocumento.setVisibility(View.VISIBLE);
        lblLugarNacimiento.setVisibility(View.VISIBLE);
        lblDomicilioElectoral.setVisibility(View.VISIBLE);
        lblNombreMadre.setVisibility(View.VISIBLE);
        lblNombrePadre.setVisibility(View.VISIBLE);
        btnIniciarProceso.setVisibility(View.VISIBLE);
    }

    public void setAdapter(ScenarioAdapter adapter) {
        // TODO this will be completed later in phase 2
    }

    public void setDoRfid(boolean rfidAvailable, SharedPreferences sharedPreferences) {
        // TODO this will be completed later in phase 2
    }

    interface MainCallbacks {

        void scenarioLv(String item);

        void showScanner();

        void recognizeImage();

        void recognizePdf();

        void setDoRFID(boolean checked);
    }
}