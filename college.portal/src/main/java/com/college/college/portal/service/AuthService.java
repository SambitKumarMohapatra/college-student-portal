package com.college.college.portal.service;

import com.college.college.portal.dto.*;
import com.college.college.portal.entity.Admin;
import com.college.college.portal.entity.Faculty;
import com.college.college.portal.entity.Role;
import com.college.college.portal.entity.Student;
import com.college.college.portal.exception.UserAlreadyExistsException;
import com.college.college.portal.repository.AdminRepository;
import com.college.college.portal.repository.FacultyRepository;
import com.college.college.portal.repository.StudentRepository;
import com.college.college.portal.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    //STUDENT REGISTRATION
    @Transactional
    public ApiResponseDTO registerStudent(StudentRegisterDTO dto){
        if(studentRepository.existsByEmail(dto.getEmail())){
            throw new UserAlreadyExistsException("Email already registered:"+dto.getEmail());
        }
        Student student=Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .branch(dto.getBranch())
                .enrollmentYear(dto.getEnrollmentYear())
                .semester(1)
                .role(Role.STUDENT)
                .build();
        student=studentRepository.save(student);
        return ApiResponseDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .role("STUDENT")
                .message("Registration successful. Please login.")
                .build();

    }
    //FACULTY REGISTRATION
    @Transactional
    public ApiResponseDTO registerFaculty(FacultyRegisterDTO dto) {
        if (facultyRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered: " + dto.getEmail());
        }
        Faculty faculty = Faculty.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .department(dto.getDepartment())
                .designation(dto.getDesignation())
                .role(Role.FACULTY)
                .build();
        faculty = facultyRepository.save(faculty);
        return ApiResponseDTO.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .email(faculty.getEmail())
                .role("FACULTY")
                .message("Registration successful. Please login.")
                .build();
    }

    //ADMIN REGISTRATION
    @Transactional
    public ApiResponseDTO registerAdmin(AdminRegisterDTO dto) {
        if (adminRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered: " + dto.getEmail());
        }
        Admin admin = Admin.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .adminRole(dto.getRole())
                .role(Role.ADMIN)
                .build();
        admin = adminRepository.save(admin);
        return ApiResponseDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .role("ADMIN")
                .message("Admin registered successfully.")
                .build();
    }

    //LOGIN

    public JwtResponseDTO login(LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        String token = jwtUtils.generateToken(auth);

        // Determine user details from correct table
        var studentOpt = studentRepository.findByEmail(dto.getEmail());
        if (studentOpt.isPresent()) {
            Student s = studentOpt.get();
            return JwtResponseDTO.builder()
                    .token(token).type("Bearer")
                    .userId(s.getId()).email(s.getEmail())
                    .name(s.getName()).role("STUDENT")
                    .build();
        }
        var facultyOpt = facultyRepository.findByEmail(dto.getEmail());
        if (facultyOpt.isPresent()) {
            Faculty f = facultyOpt.get();
            return JwtResponseDTO.builder()
                    .token(token).type("Bearer")
                    .userId(f.getId()).email(f.getEmail())
                    .name(f.getName()).role("FACULTY")
                    .build();
        }
        var adminOpt = adminRepository.findByEmail(dto.getEmail());
        if (adminOpt.isPresent()) {
            Admin a = adminOpt.get();
            return JwtResponseDTO.builder()
                    .token(token).type("Bearer")
                    .userId(a.getId()).email(a.getEmail())
                    .name(a.getName()).role("ADMIN")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
