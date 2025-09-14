package com.example.student.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.student.R;
import com.example.student.models.Student;
import com.example.student.services.AuthService;

import java.util.concurrent.CompletableFuture;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etGrade, etParentPhone, etEmergencyContact;
    private TextView tvStudentId, tvBusInfo;
    private ImageView ivProfile;
    private Button btnUpdateProfile, btnChangePassword;
    private ProgressBar progressBar;
    
    private AuthService authService;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        loadProfileData();
        
        authService = AuthService.getInstance(this);
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etGrade = findViewById(R.id.et_grade);
        etParentPhone = findViewById(R.id.et_parent_phone);
        etEmergencyContact = findViewById(R.id.et_emergency_contact);
        tvStudentId = findViewById(R.id.tv_student_id);
        tvBusInfo = findViewById(R.id.tv_bus_info);
        ivProfile = findViewById(R.id.iv_profile);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.profile));
        }
    }

    private void setupClickListeners() {
        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        ivProfile.setOnClickListener(v -> selectProfileImage());
    }

    private void loadProfileData() {
        currentStudent = authService.getCurrentStudent();
        if (currentStudent != null) {
            etName.setText(currentStudent.getName());
            etEmail.setText(currentStudent.getEmail());
            etGrade.setText(currentStudent.getGrade());
            etParentPhone.setText(currentStudent.getParentPhone());
            etEmergencyContact.setText(currentStudent.getEmergencyContact());
            tvStudentId.setText(currentStudent.getStudentId());
            tvBusInfo.setText("Bus: " + (currentStudent.getAssignedBusId() != null ? currentStudent.getAssignedBusId() : "Not assigned"));
        }
    }

    private void updateProfile() {
        if (validateInput()) {
            showProgress(true);
            
            // Update student object
            currentStudent.setName(etName.getText().toString().trim());
            currentStudent.setEmail(etEmail.getText().toString().trim());
            currentStudent.setGrade(etGrade.getText().toString().trim());
            currentStudent.setParentPhone(etParentPhone.getText().toString().trim());
            currentStudent.setEmergencyContact(etEmergencyContact.getText().toString().trim());
            
            authService.updateStudentProfile(currentStudent)
                    .thenAccept(success -> {
                        runOnUiThread(() -> {
                            showProgress(false);
                            if (success) {
                                Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        runOnUiThread(() -> {
                            showProgress(false);
                            Toast.makeText(this, getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
                        });
                        return null;
                    });
        }
    }

    private boolean validateInput() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (grade.isEmpty()) {
            etGrade.setError("Grade is required");
            etGrade.requestFocus();
            return false;
        }

        return true;
    }

    private void showChangePasswordDialog() {
        // Show change password dialog
        Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show();
    }

    private void selectProfileImage() {
        // Select profile image
        Toast.makeText(this, "Profile image selection coming soon", Toast.LENGTH_SHORT).show();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnUpdateProfile.setEnabled(!show);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

