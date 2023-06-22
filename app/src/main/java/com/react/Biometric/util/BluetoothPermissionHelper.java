package com.react.Biometric.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import com.regula.common.utils.PermissionUtil;

public class BluetoothPermissionHelper {
    private BluetoothPermissionHelper() {
        throw new IllegalStateException("Utility class");
    }
    public static final int INTENT_REQUEST_ENABLE_LOCATION = 196;
    public static final int INTENT_REQUEST_ENABLE_BLUETOOTH = 197;

    public static boolean isBluetoothEnabled(Activity activity) {
        if (activity == null)
            return false;

        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null)
            return false;
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    public static boolean isLocationServiceEnabled(Activity activity) {
        if (activity == null)
            return false;

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        if (locationManager == null)
            return true;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.d("cant_gps_enabled", "Request: " + ex.getMessage());
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.d("cant_network_enabled", "Request: " + ex.getMessage());
        }

        return gpsEnabled || networkEnabled;

    }

    public static boolean isBlePermissionDenied(Activity activity) {
        if (activity == null)
            return false;

        return PermissionUtil.isPermissionsDenied(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                || !PermissionUtil.isPermissionGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static void requestEnableBluetooth(Activity activity) {
        if (activity == null)
            return;

        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableIntent, INTENT_REQUEST_ENABLE_BLUETOOTH);
    }

    public static void requestEnableLocationService(Activity activity) {
        if (activity == null)
            return;

        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(myIntent, INTENT_REQUEST_ENABLE_LOCATION);
    }

    public static boolean isBluetoothSupported(Activity activity) {
        if (activity == null)
            return false;

        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}

