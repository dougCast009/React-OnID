package com.react.biometric;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.completions.IDocumentReaderCompletion;
import com.regula.documentreader.api.completions.IDocumentReaderInitCompletion;
import com.regula.documentreader.api.completions.IDocumentReaderPrepareCompletion;
import com.regula.documentreader.api.enums.DocReaderAction;
import com.regula.documentreader.api.errors.DocumentReaderException;
import com.regula.documentreader.api.results.DocumentReaderResults;
import com.regula.documentreader.api.results.DocumentReaderScenario;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public abstract class BaseActivity extends AppCompatActivity implements MainFragment.MainCallbacks {

    public static final int REQUEST_BROWSE_PICTURE = 11;
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 22;
    private static final String MY_SHARED_PREFS = "MySharedPrefs";
    public static final String DO_RFID = "doRfid";
    private static final String TAG_UI_FRAGMENT = "ui_fragment";

    protected SharedPreferences sharedPreferences;
    private boolean doRfid;
    private AlertDialog loadingDialog;
    protected MainFragment mainFragment;
    protected FrameLayout fragmentContainer;
    private static DocumentReaderResults documentReaderResults;


    protected abstract void initializeReader();
    protected abstract void onPrepareDbCompleted();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.fragmentContainer);

        mainFragment = (MainFragment) findFragmentByTag(TAG_UI_FRAGMENT);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            replaceFragment(mainFragment,false);
        }
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFS, MODE_PRIVATE);

        if (DocumentReader.Instance().isReady()) {
            successfulInit();
            return;
        }

        showDialog(getString(R.string.descarga_txt_preparing));

        //preparing database files, it will be downloaded from network only one time and stored on user device
        DocumentReader.Instance().prepareDatabase(BaseActivity.this,
                "Full",  // if you use 7310, replace to FullAuth
                new IDocumentReaderPrepareCompletion() {
                    @Override
                    public void onPrepareProgressChanged(int progress) {
                        String dialog = getString(R.string.descarga_texto);
                        setTitleDialog(dialog + progress + "%");
                    }

                    @Override
                    public void onPrepareCompleted(boolean status, DocumentReaderException error) {
                        if (status) {
                            onPrepareDbCompleted();
                        } else {
                            dismissDialog();
                            String dialog = getString(R.string.descarga_texto_fallo);
                            Toast.makeText(BaseActivity.this, dialog + error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void scenarioLv(String item) {
        if (!DocumentReader.Instance().isReady())
            return;

        //setting selected scenario to DocumentReader params
        DocumentReader.Instance().processParams().scenario = item;

    }

    @Override
    public void showScanner() {
        if (!DocumentReader.Instance().isReady())
            return;

        DocumentReader.Instance().processParams().dateFormat = "yyyy-MM-dd";
        DocumentReader.Instance().processParams().multipageProcessing = true;
        DocumentReader.Instance().processParams().checkHologram = true;

        DocumentReader.Instance().showScanner(BaseActivity.this, completion);
    }

    @Override
    public void recognizeImage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            //start image browsing
            createImageBrowsingRequest();
        }
    }

    @Override
    public void recognizePdf() {
        if (!DocumentReader.Instance().isReady())
            return;

        showDialog("Processing pdf");
        Executors.newSingleThreadExecutor().execute(() -> {
            InputStream is;
            byte[] buffer = null;
            try {
                is = getAssets().open("Regula/test.pdf");
                int size = is.available();
                buffer = new byte[size];
                is.read(buffer);
                is.close();
            } catch (IOException e) {
                Toast.makeText(this, "No se logro obtener el archivo solicitado", Toast.LENGTH_LONG).show();
            }

            byte[] finalBuffer = buffer;
            runOnUiThread(() -> DocumentReader.Instance().recognizeImage(finalBuffer, completion));
        });
    }

    @Override
    public void setDoRFID(boolean checked) {
        doRfid = checked;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainFragment.RFIDRESULT && documentReaderResults != null) {
            mainFragment.displayResults(documentReaderResults);
        }

        if (resultCode != RESULT_OK || requestCode != REQUEST_BROWSE_PICTURE || data.getData() == null) {
            return;
        }

        //Image browsing intent processed successfully



        showDialog("Processing image");


    }


    protected final IDocumentReaderInitCompletion initCompletion = (result, error) -> {
        dismissDialog();

        if (!result) { //Initialization was not successful
            mainFragment.disableUiElements();
            Toast.makeText(BaseActivity.this, "Init failed:" + error, Toast.LENGTH_LONG).show();
            return;
        }
        successfulInit();

    };

    protected void successfulInit() {
        setupCustomization();
        setupFunctionality();

        mainFragment.setDoRfid(DocumentReader.Instance().isRFIDAvailableForUse(), sharedPreferences);
        //getting current processing scenario and loading available scenarios to ListView
        if (!DocumentReader.Instance().availableScenarios.isEmpty())
            setScenarios();
        else {
            Toast.makeText(
                    this,
                    "Available scenarios list is empty",
                    Toast.LENGTH_SHORT
            ).show();
            mainFragment.disableUiElements();
        }
    }

    private void setupCustomization() {
        DocumentReader.Instance().customization().edit().setShowHelpAnimation(false).apply();
    }

    private void setupFunctionality() {
        DocumentReader.Instance().functionality().edit()
                .setShowCameraSwitchButton(true)
                .apply();
    }

    private final IDocumentReaderCompletion completion = (new IDocumentReaderCompletion() {

        @Override
        public void onCompleted(int action, DocumentReaderResults results, DocumentReaderException error) {
            //processing is finished, all results are ready
            if (action == DocReaderAction.COMPLETE || action == DocReaderAction.TIMEOUT) {
                dismissDialog();
                //Checking, if nfc chip reading should be performed
                if (doRfid && results != null && results.chipPage != 0) {
                    //starting chip reading
                    DocumentReader.Instance().startRFIDReader(BaseActivity.this, (rfidAction, results1, error1) -> {
                        if (rfidAction == DocReaderAction.COMPLETE || rfidAction == DocReaderAction.CANCEL) {
                            mainFragment.displayResults(results1);
                        }
                    });
                } else {
                    mainFragment.displayResults(results);
                }
            } else {
                //something happened before all results were ready
                if (action == DocReaderAction.CANCEL) {
                    Toast.makeText(BaseActivity.this, getString(R.string.alerta_cancelar_escaneo), Toast.LENGTH_LONG).show();
                } else if (action == DocReaderAction.ERROR) {
                    Toast.makeText(BaseActivity.this, "Error:" + error, Toast.LENGTH_LONG).show();
                }
            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //access to gallery is allowed
                createImageBrowsingRequest();
            } else {
                Toast.makeText(this, "Permission required, to browse images", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "The requested code is invalid.", Toast.LENGTH_LONG).show();
        }
    }

    // creates and starts image browsing intent
    // results will be handled in onActivityResult method
    private void createImageBrowsingRequest() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_BROWSE_PICTURE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }


    protected void setTitleDialog(String msg) {
        if (loadingDialog != null) {
            loadingDialog.setTitle(msg);
        } else {
            showDialog(msg);
        }
    }

    protected void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void showDialog(String msg) {
        dismissDialog();
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.simple_dialog, null);
        builderDialog.setTitle(msg);
        builderDialog.setView(dialogView);
        builderDialog.setCancelable(false);
        loadingDialog = builderDialog.show();
    }


    public Fragment findFragmentByTag(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentByTag(tag);
    }

    private void replaceFragment(Fragment fragment, boolean addFragmentInBackstack) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragmentContainer, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            if (addFragmentInBackstack)
                ft.addToBackStack(backStateName);

            ft.commit();
        }
    }

    public void setScenarios() {
        ArrayList<String> scenarios = new ArrayList<>();
        for (DocumentReaderScenario scenario : DocumentReader.Instance().availableScenarios) {
            scenarios.add(scenario.name);
        }
        if (!scenarios.isEmpty()) {
            //setting default scenario
            if (DocumentReader.Instance().processParams().scenario.isEmpty())
                DocumentReader.Instance().processParams().scenario = scenarios.get(0);

            int scenarioPosition = getScenarioPosition(scenarios, DocumentReader.Instance().processParams().scenario);
            scenarioLv(DocumentReader.Instance().processParams().scenario);
            final ScenarioAdapter adapter = new ScenarioAdapter(BaseActivity.this, android.R.layout.simple_list_item_1, scenarios);
            adapter.setSelectedPosition(scenarioPosition);
            mainFragment.setAdapter(adapter);
        }
    }

    private int getScenarioPosition(List<String> scenarios, String currentScenario) {
        int selectedPosition = 0;
        for (int i = 0; i < scenarios.size(); i++) {
            if (scenarios.get(i).equals(currentScenario)) {
                selectedPosition = i;
                break;
            }
        }
        return selectedPosition;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }
}