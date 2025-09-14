package com.example.student.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.student.R;
import com.example.student.models.Student;
import com.example.student.services.AuthService;

public class ProfileFragment extends Fragment {
    private TextView tvName, tvStudentId, tvGrade, tvBusInfo;
    private Button btnEditProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        setupClickListeners();
        loadProfileData();
        
        return view;
    }

    private void initViews(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvStudentId = view.findViewById(R.id.tv_student_id);
        tvGrade = view.findViewById(R.id.tv_grade);
        tvBusInfo = view.findViewById(R.id.tv_bus_info);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            // Navigate to profile edit activity
        });
    }

    private void loadProfileData() {
        AuthService authService = AuthService.getInstance(requireContext());
        Student student = authService.getCurrentStudent();
        
        if (student != null) {
            tvName.setText(student.getName());
            tvStudentId.setText(student.getStudentId());
            tvGrade.setText(student.getGrade());
            tvBusInfo.setText("Bus: " + (student.getAssignedBusId() != null ? student.getAssignedBusId() : "Not assigned"));
        }
    }
}

