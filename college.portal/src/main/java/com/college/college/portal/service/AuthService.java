package com.college.college.portal.service;

import com.college.college.portal.dto.*;
import com.college.college.portal.entity.*;
import com.college.college.portal.exception.InvalidCredentialsException;
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

import java.util.UUID;

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
        String rollNumber = generateRollNumber(dto.getBranch(), dto.getEnrollmentYear());
        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .branch(dto.getBranch())
                .enrollmentYear(dto.getEnrollmentYear())
                .rollNumber(rollNumber)
                .semester(1)
                .build();

        Student saved = studentRepository.save(student);

        return ApiResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role("STUDENT")
                .message("Registration successful. Please login.")
                .build();

    }

    public JwtResponseDTO loginStudent(LoginDTO dto) {
        Student student = studentRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), student.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        Authentication auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword())
        );

        String token = jwtUtils.generateToken(auth);

        return JwtResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .role("STUDENT")
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
                .qualification(dto.getQualification())
                .experienceYears(dto.getExperienceYears())
                .build();

        Faculty saved = facultyRepository.save(faculty);

        return ApiResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role("FACULTY")
                .message("Registration successful. Please login.")
                .build();
    }

    public JwtResponseDTO loginFaculty(LoginDTO dto) {
        Faculty faculty = facultyRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), faculty.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        Authentication auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword())
        );

        String token = jwtUtils.generateToken(auth);

        return JwtResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(faculty.getId())
                .email(faculty.getEmail())
                .name(faculty.getName())
                .role("FACULTY")
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

    public JwtResponseDTO loginAdmin(LoginDTO dto) {
        Admin admin = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        Authentication auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword())
        );

        String token = jwtUtils.generateToken(auth);

        return JwtResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(admin.getId())
                .email(admin.getEmail())
                .name(admin.getName())
                .role("ADMIN").build();
    }

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

    //Refresh Token
    @Transactional
    public TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO dto) {
        RefreshToken refreshToken = refreshTokenService.findByToken(dto.getRefreshToken());
        refreshTokenService.verifyExpiration(refreshToken);

        String newAccessToken = jwtUtils.generateTokenFromEmail(refreshToken.getUserEmail());
        return new TokenRefreshResponseDTO(newAccessToken, "Bearer", refreshToken.getToken());
    }
    //Logout

    @Transactional
    public void logout(String accessToken, String email) {
        // Blacklist the current access token
        TokenBlacklistService.blacklistToken(
                accessToken,
                jwtUtils.getExpiryFromToken(accessToken)
        );
        // Delete refresh token for this user
        refreshTokenService.deleteByEmail(email);
    }

    // ============ HELPERS ============

    private String generateRollNumber(String branch, int year) {
        String shortYear = String.valueOf(year).substring(2);
        String unique = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return shortYear + branch + unique;
    }
}
