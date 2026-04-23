package com.college.college.portal.service;

import com.college.college.portal.dto.ChangePasswordDTO;
import com.college.college.portal.dto.StudentProfileDTO;
import com.college.college.portal.dto.UpdateProfileDTO;
import com.college.college.portal.entity.Student;
import com.college.college.portal.exception.ResourceNotFoundException;
import com.college.college.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentProfileDTO getStudentByEmail(String email) {
        Student s = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + email));
        return mapToDTO(s);
    }

    public StudentProfileDTO getStudentById(Long id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return mapToDTO(s);
    }

    public List<StudentProfileDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<StudentProfileDTO> getStudentsByBranch(String branch) {
        return studentRepository.findByBranch(branch)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<StudentProfileDTO> getStudentsBySemester(Integer semester) {
        return studentRepository.findBySemester(semester)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // ── Update Profile ────────────────────────────────────────
    @Transactional
    public StudentProfileDTO updateProfile(String email, UpdateProfileDTO dto) {
        Student s = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + email));

        // Only allowed fields — email/role/branch/semester cannot be changed by student
        if (dto.getPhone()   != null) s.setPhone(dto.getPhone());
        if (dto.getAddress() != null) s.setAddress(dto.getAddress());
        if (dto.getCity()    != null) s.setCity(dto.getCity());
        if (dto.getPincode() != null) s.setPincode(dto.getPincode());

        return mapToDTO(studentRepository.save(s));
    }

    // ── Change Password ───────────────────────────────────────
    @Transactional
    public void changePassword(String email, ChangePasswordDTO dto) {
        Student s = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + email));

        if (!passwordEncoder.matches(dto.getOldPassword(), s.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect.");
        }
        s.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        studentRepository.save(s);
    }

    private StudentProfileDTO mapToDTO(Student s) {
        return StudentProfileDTO.builder()
                .id(s.getId())
                .rollNumber(s.getRollNumber())
                .name(s.getName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .branch(s.getBranch())
                .semester(s.getSemester())
                .enrollmentYear(s.getEnrollmentYear())
                .dob(s.getDob())
                .address(s.getAddress())
                .city(s.getCity())
                .pincode(s.getPincode())
                .role(s.getRole().name())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
