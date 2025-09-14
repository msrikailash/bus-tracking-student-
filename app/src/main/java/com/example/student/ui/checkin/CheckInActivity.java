package com.example.student.ui.checkin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.student.R;
import com.example.student.models.Trip;
import com.example.student.services.AuthService;
import com.example.student.services.LocationService;
import com.example.student.utils.Constants;
import com.example.student.ui.qr.QRScannerActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckInActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 1003;
    
    private TextView tvStatus, tvLocation, tvTime;
    private Button btnCheckIn, btnCheckOut, btnScanQR, btnManualCheckIn;
    private View progressBar;
    
    private AuthService authService;
    private LocationService locationService;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private boolean isCheckedIn = false;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        
        initViews();
        setupClickListeners();
        checkLocationPermission();
        
        authService = AuthService.getInstance(this);
        locationService = new LocationService(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        updateUI();
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tv_status);
        tvLocation = findViewById(R.id.tv_location);
        tvTime = findViewById(R.id.tv_time);
        btnCheckIn = findViewById(R.id.btn_checkin);
        btnCheckOut = findViewById(R.id.btn_checkout);
        btnScanQR = findViewById(R.id.btn_scan_qr);
        btnManualCheckIn = findViewById(R.id.btn_manual_checkin);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnCheckIn.setOnClickListener(v -> performCheckIn());
        btnCheckOut.setOnClickListener(v -> performCheckOut());
        btnScanQR.setOnClickListener(v -> startQRScanner());
        btnManualCheckIn.setOnClickListener(v -> showManualCheckInDialog());
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, getString(R.string.location_permission_required), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLocation = location;
                            updateLocationDisplay();
                        }
                    });
        }
    }

    private void updateLocationDisplay() {
        if (currentLocation != null) {
            tvLocation.setText(String.format(Locale.getDefault(), 
                    "Lat: %.6f, Lng: %.6f", 
                    currentLocation.getLatitude(), 
                    currentLocation.getLongitude()));
        } else {
            tvLocation.setText("Location not available");
        }
    }

    private void updateUI() {
        tvTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        if (isCheckedIn) {
            tvStatus.setText("Checked In");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.success));
            btnCheckIn.setEnabled(false);
            btnCheckOut.setEnabled(true);
            btnScanQR.setEnabled(false);
            btnManualCheckIn.setEnabled(false);
        } else {
            tvStatus.setText("Not Checked In");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            btnCheckIn.setEnabled(true);
            btnCheckOut.setEnabled(false);
            btnScanQR.setEnabled(true);
            btnManualCheckIn.setEnabled(true);
        }
    }

    private void performCheckIn() {
        if (currentLocation == null) {
            Toast.makeText(this, "Location not available. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        
        // Simulate check-in process
        // In real implementation, this would save to Firebase
        currentTrip = new Trip(
                "trip_" + System.currentTimeMillis(),
                authService.getCurrentStudent().getStudentId(),
                "bus001",
                "stop001"
        );
        currentTrip.setCheckInTime(new Date());
        currentTrip.setCheckInLocation(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
        
        // Simulate API call delay
        new android.os.Handler().postDelayed(() -> {
            isCheckedIn = true;
            updateUI();
            showProgress(false);
            Toast.makeText(this, getString(R.string.checkin_success), Toast.LENGTH_SHORT).show();
        }, 1000);
    }

    private void performCheckOut() {
        if (currentTrip == null) {
            Toast.makeText(this, "No active trip to check out", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        
        // Simulate check-out process
        currentTrip.setCheckOutTime(new Date());
        if (currentLocation != null) {
            currentTrip.setCheckOutLocation(currentLocation.getLatitude() + "," + currentLocation.getLongitude());
        }
        currentTrip.setCompleted(true);
        
        // Simulate API call delay
        new android.os.Handler().postDelayed(() -> {
            isCheckedIn = false;
            currentTrip = null;
            updateUI();
            showProgress(false);
            Toast.makeText(this, getString(R.string.checkout_success), Toast.LENGTH_SHORT).show();
        }, 1000);
    }

    private void startQRScanner() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    Constants.REQUEST_CODE_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, QRScannerActivity.class);
            startActivityForResult(intent, REQUEST_CODE_QR_SCAN);
        }
    }

    private void showManualCheckInDialog() {
        // Show dialog for manual check-in
        Toast.makeText(this, "Manual check-in feature coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (resultCode == RESULT_OK && data != null) {
                String qrResult = data.getStringExtra("qr_result");
                if (qrResult != null && qrResult.startsWith("bus_stop_")) {
                    // Valid QR code scanned
                    performCheckIn();
                } else {
                    Toast.makeText(this, getString(R.string.error_invalid_qr), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnCheckIn.setEnabled(!show && !isCheckedIn);
        btnCheckOut.setEnabled(!show && isCheckedIn);
        btnScanQR.setEnabled(!show && !isCheckedIn);
        btnManualCheckIn.setEnabled(!show && !isCheckedIn);
    }
}
