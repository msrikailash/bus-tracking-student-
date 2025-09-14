package com.example.student.ui.qr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.student.R;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeResult;

public class QRScannerActivity extends CaptureActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, scanner will work
            } else {
                Toast.makeText(this, getString(R.string.camera_permission_required), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    public void onScanResult(com.journeyapps.barcodescanner.BarcodeResult result) {
        String scannedText = result.getText();
        
        // Validate QR code format
        if (scannedText != null && scannedText.startsWith("bus_stop_")) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("qr_result", scannedText);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.error_invalid_qr), Toast.LENGTH_SHORT).show();
            // Continue scanning
        }
    }
}

