package com.react.Biometric.utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.react.Biometric.R;
import com.react.Biometric.Constantes;

//Clase de utilidades para el manejo de interfaz, incluida en el SDK de neurotechnology, modificada con temas de aplicación
public class BaseActivity extends AppCompatActivity {
    // ===========================================================
    // Private fields
    // ===========================================================

    private ProgressDialog mProgressDialog;
    // ===========================================================
    // Protected methods
    // ===========================================================

    protected void showProgress(int messageId, int titleId, Context context) {
        showProgress(getString(messageId), getString(titleId), context);
    }

    protected void showProgress(final String message, final String title, final Context contexto) {
        hideProgress();
        runOnUiThread(() -> {
            mProgressDialog = new ProgressDialog(contexto, R.style.AlertDialogProgressStyle);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        });
    }

    protected void hideProgress() {
        runOnUiThread(() -> {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        });
    }

    protected void showToast(int messageId) {
        showToast(getString(messageId));
    }

    protected void showToast(final String message) {
        runOnUiThread(() -> {
            Toast toast = Toast.makeText(getApplicationContext(), message, Constantes.ToastDuration);
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

            //AlertDialog dialog = builder.create();
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }


}
