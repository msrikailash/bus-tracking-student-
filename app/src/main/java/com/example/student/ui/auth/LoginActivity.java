package com.example.student.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.student.R;
import com.example.student.services.AuthService;
import com.example.student.ui.MainActivity;
import com.example.student.utils.Constants;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private EditText etStudentId, etPassword;
    private Button btnLogin, btnBiometric;
    private TextView tvRegister, tvForgotPassword;
    private CheckBox cbRememberMe;
    private ProgressBar progressBar;
    
    private AuthService authService;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initViews();
        initBiometric();
        setupClickListeners();
        
        authService = AuthService.getInstance(this);
        
        // Check if user was previously logged in
        if (authService.wasLoggedIn()) {
            etStudentId.setText(authService.getLastStudentId());
        }
    }

    private void initViews() {
        etStudentId = findViewById(R.id.et_student_id);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnBiometric = findViewById(R.id.btn_biometric);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initBiometric() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Auto-login with saved credentials
                performLogin();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Biometric authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use your fingerprint or face to login")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            if (validateInput()) {
                performLogin();
            }
        });

        btnBiometric.setOnClickListener(v -> {
            if (authService.wasLoggedIn()) {
                biometricPrompt.authenticate(promptInfo);
            } else {
                Toast.makeText(this, "Please login first to enable biometric authentication", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            String studentId = etStudentId.getText().toString().trim();
            if (TextUtils.isEmpty(studentId)) {
                Toast.makeText(this, "Please enter your Student ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String email = studentId + "@student.school.edu";
            resetPassword(email);
        });
    }

    private boolean validateInput() {
        String studentId = etStudentId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return false;
        }

        if (!studentId.matches(Constants.STUDENT_ID_PATTERN)) {
            etStudentId.setError("Invalid Student ID format");
            etStudentId.requestFocus();
            return false;
        }

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

        return true;
    }

    private void performLogin() {
        String studentId = etStudentId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        showProgress(true);
        
        authService.login(studentId, password)
                .thenAccept(success -> {
                    runOnUiThread(() -> {
                        showProgress(false);
                        if (success) {
                            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
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

    private void resetPassword(String email) {
        showProgress(true);
        
        authService.resetPassword(email)
                .thenAccept(success -> {
                    runOnUiThread(() -> {
                        showProgress(false);
                        if (success) {
                            Toast.makeText(this, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
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
        btnLogin.setEnabled(!show);
        btnBiometric.setEnabled(!show);
    }
}

