package com.react.biometric.utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.react.biometric.R;
import com.react.biometric.Constantes;
import com.react.biometric.util.ProgressHelper;

//Clase de utilidades para el manejo de interfaz, incluida en el SDK de neurotechnology, modificada con temas de aplicación
public class BaseActivity extends AppCompatActivity {
    // ===========================================================
    // Private fields
    // ===========================================================

    private ProgressHelper mProgressDialog;
    // ===========================================================
    // Protected methods
    // ===========================================================

    protected void showProgress(int messageId,  Context context) {
        showProgress(getString(messageId), context);
    }

    protected void showProgress(final String message, final Context contexto) {
        hideProgress();
        runOnUiThread(() -> ProgressHelper.showProgress(contexto,message));
    }

    protected void hideProgress() {
        runOnUiThread(() -> {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                ProgressHelper.hideProgress();
            }
        });
    }

    protected void showToast(int messageId) {
        showToast(getString(messageId));
    }

    protected void showToast(final String message) {
        runOnUiThread(() -> {
            Toast toast = Toast.makeText(getApplicationContext(), message, Constantes.TOASTDURATION);
            toast.show();
        });
    }

    protected void showMessage(int messageId) {
        showMessage(getString(messageId));
    }

    protected void showMessage(final String message) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null).show();

        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }


}
