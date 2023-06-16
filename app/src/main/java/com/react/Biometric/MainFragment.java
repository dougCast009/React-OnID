package com.react.Biometric;

import static com.react.Biometric.BaseActivity.DO_RFID;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.enums.eCheckDiagnose;
import com.regula.documentreader.api.enums.eCheckResult;
import com.regula.documentreader.api.enums.eGraphicFieldType;
import com.regula.documentreader.api.enums.eRPRM_SecurityFeatureType;
import com.regula.documentreader.api.enums.eVisualFieldType;
import com.regula.documentreader.api.results.DocumentReaderResults;
import com.regula.documentreader.api.results.authenticity.DocumentReaderAuthenticityCheck;
import com.regula.documentreader.api.results.authenticity.DocumentReaderAuthenticityElement;
import com.regula.documentreader.api.results.authenticity.DocumentReaderIdentResult;
import com.regula.documentreader.api.enums.Scenario;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.enums.ImageType;
import com.regula.facesdk.enums.LivenessStatus;
import com.regula.facesdk.model.MatchFacesImage;
import com.regula.facesdk.model.results.matchfaces.MatchFacesComparedFacesPair;
import com.regula.facesdk.model.results.matchfaces.MatchFacesResponse;
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit;
import com.regula.facesdk.request.MatchFacesRequest;

public class MainFragment extends Fragment {

    //BOTONES
    private Button btnTabCapturar;
    private Button btnTabInfo;
    private Button btnIniciarCaptura;
    private Button btnIniciarProceso;
    //LAYOUTS
    private LinearLayout TabInformacion;
    private LinearLayout TabEscaner;
    //CAMPOS DE TEXTO
    private TextView txt_estado;
    private TextView txt_documento;
    private TextView txt_identificacion;
    private TextView txt_nombres;
    private TextView txt_apellidos;
    private TextView txt_sexo;
    private TextView txt_edad;
    private TextView txt_fecha_nacimiento;
    private TextView txt_lugar_nacimiento;
    private TextView txt_domicilio;
    private TextView txt_nombre_madre;
    private TextView txt_nombre_padre;
    private TextView txt_meses;
    private TextView txt_vencimiento;
    private TextView txt_cod_pais;
    private TextView txt_pais_origen;
    private TextView txt_dmx_expediente;
    private TextView txt_dmx_cod_nacionalidad;
    private TextView txt_dmx_nacionalidad;
    private TextView txt_dmx_emision;
    private LinearLayout lbl_documento;
    private LinearLayout lbl_lugar_nacimiento;
    private LinearLayout lbl_domicilio_electoral;
    private LinearLayout lbl_nombre_madre;
    private LinearLayout lbl_nombre_padre;
    private LinearLayout lbl_dmx_expediente;
    private LinearLayout lbl_dmx_cod_nacionalidad;
    private LinearLayout lbl_dmx_nacionalidad;
    private LinearLayout lbl_dmx_emision;
    //CAMPOS DE IMAGENES
    private ImageView img_foto;
    private ImageView img_firma;
    private ImageView img_documento_1;
    private ImageView img_documento_2;

    private LinearLayout MainContent;
    private LinearLayout TabRespuesta;
    private ImageView img_resultado;
    private TextView txt_continuar;
    private TextView texto_principal;
    private Toolbar toolbar;
    //ANTIGUOS
    private TextView nameTv;
    private TextView numberTv;
    private TextView showScanner;
    private TextView recognizeImage;
    private TextView recognizePdf;
    private ImageView portraitIv;
    private ImageView docImageIv;
    private ImageView sigImageIv;
    private RelativeLayout authenticityLayout;
    private ImageView authenticityResultImg;
    private volatile MainCallbacks mCallbacks;
    public static int RFID_RESULT = 100;
//    private Boolean Capturado;
    private String RostroCapturado;
    private Boolean EstadoDocumento = false;

    //Paleta Colores
    private String TXTCOLORPRINCIPAL = "#1A83C4";
    private String TXTCOLORSECUNDARIO = "#B33A3A";
    private String TXTCOLORNEGRO = "#000000";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        btnTabCapturar = root.findViewById(R.id.btnTabCapturar);
        btnTabInfo = root.findViewById(R.id.btnTabInfo);
        btnIniciarCaptura = root.findViewById(R.id.btnIniciarCaptura);
        btnIniciarProceso = root.findViewById(R.id.btnIniciarProceso);
        TabInformacion = root.findViewById(R.id.TabInformacion);
        TabEscaner = root.findViewById(R.id.TabEscaner);
        txt_estado = root.findViewById(R.id.txt_estado);
        txt_documento = root.findViewById(R.id.txt_documento);
        txt_identificacion = root.findViewById(R.id.txt_identificacion);
        txt_nombres = root.findViewById(R.id.txt_nombres);
        txt_apellidos = root.findViewById(R.id.txt_apellidos);
        txt_sexo = root.findViewById(R.id.txt_sexo);
        txt_edad = root.findViewById(R.id.txt_edad);
        txt_fecha_nacimiento = root.findViewById(R.id.txt_fecha_nacimiento);
        txt_lugar_nacimiento = root.findViewById(R.id.txt_lugar_nacimiento);
        txt_domicilio = root.findViewById(R.id.txt_domicilio);
        txt_nombre_madre = root.findViewById(R.id.txt_nombre_madre);
        txt_nombre_padre = root.findViewById(R.id.txt_nombre_padre);
        txt_meses = root.findViewById(R.id.txt_meses);
        txt_vencimiento = root.findViewById(R.id.txt_vencimiento);
        txt_cod_pais = root.findViewById(R.id.txt_cod_pais);
        txt_pais_origen = root.findViewById(R.id.txt_pais_origen);
        txt_dmx_expediente = root.findViewById(R.id.txt_dmx_expediente);
        txt_dmx_cod_nacionalidad = root.findViewById(R.id.txt_dmx_cod_nacionalidad);
        txt_dmx_nacionalidad = root.findViewById(R.id.txt_dmx_nacionalidad);
        txt_dmx_emision = root.findViewById(R.id.txt_dmx_emision);

        lbl_documento = root.findViewById(R.id.lbl_documento);
        lbl_lugar_nacimiento = root.findViewById(R.id.lbl_lugar_nacimiento);
        lbl_domicilio_electoral = root.findViewById(R.id.lbl_domicilio_electoral);
        lbl_nombre_madre = root.findViewById(R.id.lbl_nombre_madre);
        lbl_nombre_padre = root.findViewById(R.id.lbl_nombre_padre);
        lbl_dmx_expediente = root.findViewById(R.id.lbl_dmx_expediente);
        lbl_dmx_cod_nacionalidad = root.findViewById(R.id.lbl_dmx_cod_nacionalidad);
        lbl_dmx_nacionalidad = root.findViewById(R.id.lbl_dmx_nacionalidad);
        lbl_dmx_emision = root.findViewById(R.id.lbl_dmx_emision);

        img_foto = root.findViewById(R.id.img_foto);
        img_firma = root.findViewById(R.id.img_firma);
        img_documento_1 = root.findViewById(R.id.img_documento_1);
        img_documento_2 = root.findViewById(R.id.img_documento_2);
        MostrarTabCaptura();
        nameTv = root.findViewById(R.id.nameTv);
        numberTv = root.findViewById(R.id.numberTv);
        showScanner = root.findViewById(R.id.showScannerLink);
        recognizeImage = root.findViewById(R.id.recognizeImageLink);
        recognizePdf = root.findViewById(R.id.recognizePdfLink);
        portraitIv = root.findViewById(R.id.portraitIv);
        sigImageIv = root.findViewById(R.id.signatureImageIv);
        docImageIv = root.findViewById(R.id.documentImageIv);
        authenticityLayout = root.findViewById(R.id.authenticityLayout);
        authenticityResultImg = root.findViewById(R.id.authenticityResultImg);

//        Capturado = false;

        MainContent = root.findViewById(R.id.MainContent);
        TabRespuesta = root.findViewById(R.id.TabRespuesta);
        img_resultado = root.findViewById(R.id.img_resultado);
        txt_continuar = root.findViewById(R.id.txt_continuar);
        texto_principal = root.findViewById(R.id.texto_principal);
        toolbar = root.findViewById(R.id.toolbarMain);

        initView();
        return root;
    }

    @Override
    public void onResume() {//used to show scenarios after fragments transaction
        super.onResume();
        if (getActivity() != null && DocumentReader.Instance().isReady())

            if (DocumentReader.Instance().availableScenarios.size() > 0)
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

    private void MostrarTabCaptura()
    {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        TabInformacion.setVisibility(View.GONE);
        TabEscaner.setVisibility(View.VISIBLE);
    }

    private void MostrarTabInformacion()
    {
        btnTabCapturar.setBackgroundColor(Color.parseColor(getString(R.string.color_primary_dark)));
        btnTabInfo.setBackgroundColor(Color.parseColor(getString(R.string.color_primary)));
        btnTabCapturar.setTextColor(Color.parseColor(getString(R.string.color_font_primary_dark)));
        btnTabInfo.setTextColor(Color.parseColor(getString(R.string.color_font_primary)));
        TabInformacion.setVisibility(View.VISIBLE);
        TabEscaner.setVisibility(View.GONE);
    }

    private void initView() {
        txt_continuar.setOnClickListener(view -> {
            TabRespuesta.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            MainContent.setVisibility(View.VISIBLE);
        });

        btnTabCapturar.setOnClickListener(view -> {
            MostrarTabCaptura();
        });

        btnTabInfo.setOnClickListener(view -> {
            MostrarTabInformacion();
        });

        btnIniciarCaptura.setOnClickListener(view -> {
            mCallbacks.scenarioLv(Scenario.SCENARIO_FULL_AUTH);
            LimpiarCampos();
            mCallbacks.showScanner();
        });
    }

    public void displayResults(DocumentReaderResults results) {


        if (results != null)
        {
//            Capturado = true;
            //CARGAR CAMPOS DE TEXTO
            txt_cod_pais.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_ISSUING_STATE_CODE)));
            txt_documento.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_PERSONAL_NUMBER)));
            String Extra = ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_OPTIONAL_DATA));
            String Identificacion = Extra.equals(getString(R.string.document_pending)) ? ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DOCUMENT_NUMBER)) : ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DOCUMENT_NUMBER) + Extra);
            txt_identificacion.setText(Identificacion);
            txt_nombres.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_GIVEN_NAMES)));
            txt_apellidos.setText(ValidarNulosApellidos(results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME)) + " " + ValidarNulosApellidos(results.getTextFieldValueByType(eVisualFieldType.FT_SECOND_SURNAME)));
            txt_sexo.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_SEX)));
            txt_edad.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_AGE)));
            txt_fecha_nacimiento.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_BIRTH)));
            txt_lugar_nacimiento.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_PLACE_OF_BIRTH)));
            txt_domicilio.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_PLACE_OF_REGISTRATION)));
            txt_nombre_madre.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_MOTHER_GIVENNAME)));
            txt_nombre_padre.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_FATHER_GIVENNAME)));
            String Meses = ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_REMAINDER_TERM));
            txt_meses.setText(Meses);
            txt_vencimiento.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_EXPIRY)));
            txt_pais_origen.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_ISSUING_STATE_NAME)));
            txt_dmx_expediente.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_REG_CERT_REG_NUMBER)));
            txt_dmx_nacionalidad.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_NATIONALITY)));
            txt_dmx_emision.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_ISSUE)));
            txt_dmx_cod_nacionalidad.setText(ValidarNulos(results.getTextFieldValueByType(eVisualFieldType.FT_NATIONALITY_CODE)));

            if (!Meses.equals(getString(R.string.document_pending)))
            {
                if (Integer.parseInt(Meses) > 0)
                {
                    txt_meses.setTextColor(Color.parseColor(TXTCOLORPRINCIPAL));
                }
                else
                {
                    txt_meses.setTextColor(Color.parseColor(TXTCOLORSECUNDARIO));
                }
            }

            if (Identificacion.length() == 9)
            {
                lbl_dmx_expediente.setVisibility(View.GONE);
                lbl_dmx_cod_nacionalidad.setVisibility(View.GONE);
                lbl_dmx_nacionalidad.setVisibility(View.GONE);
                lbl_dmx_emision.setVisibility(View.GONE);
            }
            else if (Identificacion.length() == 12)
            {
                lbl_documento.setVisibility(View.GONE);
                lbl_lugar_nacimiento.setVisibility(View.GONE);
                lbl_domicilio_electoral.setVisibility(View.GONE);
                lbl_nombre_madre.setVisibility(View.GONE);
                lbl_nombre_padre.setVisibility(View.GONE);
            }

            Bitmap imgFoto = results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT);
            if (imgFoto != null) {
                img_foto.setImageBitmap(imgFoto);
            }
            Bitmap imgFirma = results.getGraphicFieldImageByType(eGraphicFieldType.GF_SIGNATURE);
            if (imgFirma != null) {
                img_firma.setImageBitmap(imgFirma);
            }
            Bitmap imgDoc1 = results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE);
            if (imgDoc1 != null) {
                img_documento_1.setImageBitmap(imgDoc1);
            }
            Bitmap imgDoc2 = results.getGraphicFieldImageByType(eGraphicFieldType.GF_OTHER);
            if (imgDoc2 != null) {
                img_documento_2.setImageBitmap(imgDoc2);
            }

            Boolean MostrarTabRespuesta = btnIniciarProceso.getText().equals(getString(R.string.verificar));

            if (ValidarFechaVencimiento(txt_vencimiento.getText().toString()))
            {
                EstadoDocumento = true;
                txt_estado.setText(getString(R.string.result_valido));
                txt_estado.setTextColor(Color.parseColor(TXTCOLORPRINCIPAL));
                txt_vencimiento.setTextColor(Color.parseColor(TXTCOLORPRINCIPAL));

                if (MostrarTabRespuesta)
                {
                    if (imgFoto != null)
                    {
                        FaceSDK.Instance().startLiveness(getActivity(), livenessResponse -> {
                            if (livenessResponse.getLiveness() == LivenessStatus.PASSED) {
                                RostroCapturado = this.getStringImage(livenessResponse.getBitmap());

                                List<MatchFacesImage> images = Arrays.asList(
                                        new MatchFacesImage(livenessResponse.getBitmap(), ImageType.LIVE),
                                        new MatchFacesImage(imgFoto, ImageType.PRINTED)
                                );
                                MatchFacesRequest request = new MatchFacesRequest(images);

                                FaceSDK.Instance().matchFaces(request, response -> {

                                    MatchFacesSimilarityThresholdSplit split = new MatchFacesSimilarityThresholdSplit(response.getResults(), 0.75);
                                    List<MatchFacesComparedFacesPair> matched = split.getMatchedFaces();
                                    List<MatchFacesComparedFacesPair> notmatched = split.getUnmatchedFaces();

                                    if (matched.size() > 0 && notmatched.size() == 0)
                                    {
                                        EstadoDocumento = true;
                                    }
                                    else
                                    {
                                        EstadoDocumento = false;
                                    }

                                });

                            }
                        });
                    }
                }
            }
            else
            {
                txt_estado.setText(getString(R.string.result_no_valido));
                txt_estado.setTextColor(Color.parseColor(TXTCOLORSECUNDARIO));
                txt_vencimiento.setTextColor(Color.parseColor(TXTCOLORSECUNDARIO));
                btnIniciarProceso.setVisibility(View.GONE);
            }

            if (MostrarTabRespuesta)
            {
                TabRespuesta.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                MainContent.setVisibility(View.GONE);
                texto_principal.setText(EstadoDocumento ? getString(R.string.identidad_confirmada) : getString(R.string.identidad_no_confirmada));
                img_resultado.setImageResource(EstadoDocumento ? R.drawable.document_check : R.drawable.document_cross);
            }

            MostrarTabInformacion();
        }
        else
        {
            MostrarTabCaptura();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            double aspectRatio = (double) bitmap.getWidth() / (double) bitmap.getHeight();
            return Bitmap.createScaledBitmap(bitmap, (int) (480 * aspectRatio), 480, false);
        }
        return null;
    }

    public void disableUiElements() {
        recognizePdf.setClickable(false);
        showScanner.setClickable(false);
        recognizeImage.setClickable(false);

        recognizePdf.setTextColor(Color.GRAY);
        showScanner.setTextColor(Color.GRAY);
        recognizeImage.setTextColor(Color.GRAY);
    }

    private String ValidarNulos(String Datos)
    {
        if (Datos != null)
        {
            return Datos;
        }
        else
        {
            return getString(R.string.document_pending);
        }
    }

    private String ValidarNulosApellidos(String Datos)
    {
        if (Datos != null)
        {
            return Datos;
        }
        else
        {
            return "";
        }
    }

    private Boolean ValidarFechaVencimiento(String Datos) {
        if (Datos != null)
        {
            try {
                Date FechaVence = new SimpleDateFormat("yyyy-MM-dd").parse(Datos);
                Date FechaActual = Calendar.getInstance().getTime();

                if (FechaActual.before(FechaVence) || FechaActual.equals(FechaVence))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch(Exception ex)
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private void LimpiarCampos()
    {
        txt_estado.setText(getString(R.string.document_pending));
        txt_documento.setText(getString(R.string.document_pending));
        txt_identificacion.setText(getString(R.string.document_pending));
        txt_nombres.setText(getString(R.string.document_pending));
        txt_apellidos.setText(getString(R.string.document_pending));
        txt_sexo.setText(getString(R.string.document_pending));
        txt_edad.setText(getString(R.string.document_pending));
        txt_fecha_nacimiento.setText(getString(R.string.document_pending));
        txt_lugar_nacimiento.setText(getString(R.string.document_pending));
        txt_nombre_madre.setText(getString(R.string.document_pending));
        txt_nombre_padre.setText(getString(R.string.document_pending));
        txt_meses.setText(getString(R.string.document_pending));
        txt_vencimiento.setText(getString(R.string.document_pending));
        txt_cod_pais.setText(getString(R.string.document_pending));
        txt_domicilio.setText(getString(R.string.document_pending));
        txt_pais_origen.setText(getString(R.string.document_pending));
        txt_dmx_expediente.setText(getString(R.string.document_pending));
        txt_dmx_nacionalidad.setText(getString(R.string.document_pending));
        txt_dmx_emision.setText(getString(R.string.document_pending));
        txt_dmx_cod_nacionalidad.setText(getString(R.string.document_pending));
        txt_estado.setTextColor(Color.parseColor(TXTCOLORNEGRO));
        txt_meses.setTextColor(Color.parseColor(TXTCOLORNEGRO));
        txt_vencimiento.setTextColor(Color.parseColor(TXTCOLORNEGRO));
//        Capturado = false;
        img_foto.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        img_firma.setImageDrawable(getResources().getDrawable(R.drawable.writing));
        img_documento_1.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        img_documento_2.setImageDrawable(getResources().getDrawable(R.drawable.membership));
        lbl_dmx_expediente.setVisibility(View.VISIBLE);
        lbl_dmx_cod_nacionalidad.setVisibility(View.VISIBLE);
        lbl_dmx_nacionalidad.setVisibility(View.VISIBLE);
        lbl_dmx_emision.setVisibility(View.VISIBLE);
        lbl_documento.setVisibility(View.VISIBLE);
        lbl_lugar_nacimiento.setVisibility(View.VISIBLE);
        lbl_domicilio_electoral.setVisibility(View.VISIBLE);
        lbl_nombre_madre.setVisibility(View.VISIBLE);
        lbl_nombre_padre.setVisibility(View.VISIBLE);
        btnIniciarProceso.setVisibility(View.VISIBLE);
        RostroCapturado = "";
    }

    public void setAdapter(ScenarioAdapter adapter) {
    }

    public void setDoRfid(boolean rfidAvailable, SharedPreferences sharedPreferences) {
    }

    interface MainCallbacks {

        void scenarioLv(String item);

        void showScanner();

        void recognizeImage();

        void recognizePdf();

        void setDoRFID(boolean checked);
    }
}