package com.example.student.ui.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.student.R;
import com.example.student.models.Bus;
import com.example.student.models.Route;
import com.example.student.services.LocationService;
import com.example.student.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationService locationService;
    
    private TextView tvBusInfo, tvEta, tvNextStop;
    private FloatingActionButton btnRefresh, btnCenterLocation;
    private View progressBar;
    
    private Marker busMarker, userMarker;
    private Polyline routePolyline;
    private Bus currentBus;
    private Route currentRoute;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        initViews();
        setupClickListeners();
        checkLocationPermission();
        
        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationService = new LocationService(this);
    }

    private void initViews() {
        tvBusInfo = findViewById(R.id.tv_bus_info);
        tvEta = findViewById(R.id.tv_eta);
        tvNextStop = findViewById(R.id.tv_next_stop);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnCenterLocation = findViewById(R.id.btn_center_location);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnRefresh.setOnClickListener(v -> refreshBusLocation());
        btnCenterLocation.setOnClickListener(v -> centerOnUserLocation());
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

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Enable user location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        
        // Set default location (San Francisco)
        LatLng defaultLocation = new LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, Constants.DEFAULT_ZOOM));
        
        // Load bus data
        loadBusData();
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            updateUserLocationMarker();
                        }
                    });
        }
    }

    private void updateUserLocationMarker() {
        if (googleMap != null && userLocation != null) {
            if (userMarker != null) {
                userMarker.remove();
            }
            userMarker = googleMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .title("Your Location"));
        }
    }

    private void loadBusData() {
        showProgress(true);
        
        // Simulate loading bus data
        // In real implementation, this would fetch from Firebase
        simulateBusData();
    }

    private void simulateBusData() {
        // Create mock bus data
        currentBus = new Bus("bus001", "Bus 12", "John Driver", "route001");
        currentBus.setCurrentLocation(new LatLng(37.7849, -122.4094));
        currentBus.setSpeed(25.0);
        currentBus.setMoving(true);
        
        // Create mock route data
        currentRoute = new Route("route001", "Route A", new ArrayList<>());
        List<LatLng> routePath = new ArrayList<>();
        routePath.add(new LatLng(37.7849, -122.4094));
        routePath.add(new LatLng(37.7849, -122.4194));
        routePath.add(new LatLng(37.7749, -122.4194));
        currentRoute.setPathCoordinates(routePath);
        
        updateMap();
        showProgress(false);
    }

    private void updateMap() {
        if (googleMap == null || currentBus == null) return;
        
        // Update bus marker
        if (busMarker != null) {
            busMarker.remove();
        }
        busMarker = googleMap.addMarker(new MarkerOptions()
                .position(currentBus.getCurrentLocation())
                .title("Bus " + currentBus.getBusNumber())
                .snippet("Driver: " + currentBus.getDriverName()));
        
        // Draw route
        if (currentRoute != null && currentRoute.getPathCoordinates() != null) {
            if (routePolyline != null) {
                routePolyline.remove();
            }
            routePolyline = googleMap.addPolyline(new PolylineOptions()
                    .addAll(currentRoute.getPathCoordinates())
                    .color(ContextCompat.getColor(this, R.color.primary))
                    .width(8));
        }
        
        // Update info display
        updateBusInfo();
        
        // Center camera on bus
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                currentBus.getCurrentLocation(), Constants.DEFAULT_ZOOM));
    }

    private void updateBusInfo() {
        if (currentBus != null) {
            tvBusInfo.setText("Bus " + currentBus.getBusNumber() + " - " + currentBus.getDriverName());
            tvEta.setText("ETA: 5 minutes");
            tvNextStop.setText("Next Stop: Main Street");
        }
    }

    private void refreshBusLocation() {
        showProgress(true);
        
        // Simulate refreshing bus location
        // In real implementation, this would fetch latest location from Firebase
        if (currentBus != null) {
            // Add some random movement for demo
            double lat = currentBus.getCurrentLocation().latitude + (Math.random() - 0.5) * 0.001;
            double lng = currentBus.getCurrentLocation().longitude + (Math.random() - 0.5) * 0.001;
            currentBus.setCurrentLocation(new LatLng(lat, lng));
            updateMap();
        }
        
        showProgress(false);
    }

    private void centerOnUserLocation() {
        if (userLocation != null && googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, Constants.DEFAULT_ZOOM));
        } else {
            getCurrentLocation();
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRefresh.setEnabled(!show);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationService != null) {
            locationService.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationService != null) {
            locationService.stopLocationUpdates();
        }
    }
}
