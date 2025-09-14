package com.example.student.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.student.R;
import com.example.student.services.AuthService;
import com.example.student.ui.MainActivity;
import com.example.student.utils.Constants;

public class RegisterActivity extends AppCompatActivity {
    private EditText etStudentId, etName, etEmail, etPassword, etConfirmPassword, etGrade;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;
    
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
        setupClickListeners();
        
        authService = AuthService.getInstance(this);
    }

    private void initViews() {
        etStudentId = findViewById(R.id.et_student_id);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etGrade = findViewById(R.id.et_grade);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> {
            if (validateInput()) {
                performRegistration();
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInput() {
        String studentId = etStudentId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();

        // Validate Student ID
        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return false;
        }

        if (!studentId.matches(Constants.STUDENT_ID_PATTERN)) {
            etStudentId.setError("Invalid Student ID format (6-12 alphanumeric characters)");
            etStudentId.requestFocus();
            return false;
        }

        // Validate Name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (name.length() < 2) {
            etName.setError("Name must be at least 2 characters");
            etName.requestFocus();
            return false;
        }

        // Validate Email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            etEmail.requestFocus();
            return false;
        }

        // Validate Password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            etPassword.setError("Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() > Constants.MAX_PASSWORD_LENGTH) {
            etPassword.setError("Password must be no more than " + Constants.MAX_PASSWORD_LENGTH + " characters");
            etPassword.requestFocus();
            return false;
        }

        // Validate Confirm Password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        // Validate Grade
        if (TextUtils.isEmpty(grade)) {
            etGrade.setError("Grade is required");
            etGrade.requestFocus();
            return false;
        }

        return true;
    }

    private void performRegistration() {
        String studentId = etStudentId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String grade = etGrade.getText().toString().trim();

        showProgress(true);
        
        authService.register(studentId, name, email, password, grade)
                .thenAccept(success -> {
                    runOnUiThread(() -> {
                        showProgress(false);
                        if (success) {
                            Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
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

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
}

