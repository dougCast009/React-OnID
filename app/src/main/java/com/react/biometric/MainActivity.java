package com.react.biometric;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.params.DocReaderConfig;
import com.react.biometric.util.LicenseUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LicenseUtil.getLicense(this) == null)
            showDialog(this);
    }

    protected void initializeReader() {
        showDialog(getString(R.string.alerta_Inicializando));

        byte[] license = LicenseUtil.getLicense( this);

        DocReaderConfig config = new DocReaderConfig(license);
        config.setLicenseUpdate(true);

        DocumentReader.Instance().initializeReader(MainActivity.this, config, initCompletion);
    }

    @Override
    protected void onPrepareDbCompleted() {
        initializeReader();
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Error");
        builder.setMessage("license in assets is missed!");
        builder.setPositiveButton(getString(R.string.strAccessibilityCloseButton), (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }
}
