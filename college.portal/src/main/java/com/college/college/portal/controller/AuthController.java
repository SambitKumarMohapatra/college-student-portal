package com.college.college.portal.controller;

import com.college.college.portal.dto.*;
import com.college.college.portal.service.AuthService;
import com.college.college.portal.service.PasswordResetService;
import com.college.college.portal.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",description = "Registration, Login, Refresh Token, and Logout APIs")
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final StudentService studentService;
    //Student Registration
    @PostMapping("/student/register")
    @Operation( summary = "Register a new Student",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                {
                  "name": "Rahul Kumar",
                  "email": "rahul@college.edu",
                  "phone": "9876543210",
                  "password": "SecurePass@123",
                  "branch": "CSE",
                  "enrollmentYear": 2024
                }
            """))
            ))
    public ResponseEntity<ApiResponseDTO> registerStudent(
            @Valid@RequestBody StudentRegisterDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerStudent(dto));
    }

    //Faculty Registration
    @PostMapping("/faculty/register")
    @Operation(
            summary = "Register a new Faculty member",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                {
                  "name": "Dr. Priya Sharma",
                  "email": "priya@college.edu",
                  "phone": "9123456780",
                  "password": "Faculty@123",
                  "department": "CSE",
                  "designation": "Associate Prof"
                }
            """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Faculty registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<ApiResponseDTO> registerFaculty(@Valid @RequestBody FacultyRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerFaculty(dto));
    }

    //Admin Registration
    @PostMapping("/admin/register")
    @Operation(
            summary = "Register Admin (SuperAdmin use only)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                {
                  "name": "Super Admin",
                  "email": "admin@college.edu",
                  "phone": "9000000001",
                  "password": "Admin@Super1",
                  "role": "SuperAdmin"
                }
            """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin registered successfully"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<ApiResponseDTO> registerAdmin(@Valid @RequestBody AdminRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdmin(dto));
    }

    //Student Login
    @PostMapping("/student/login")
    @Operation(
            summary = "Student Login — returns JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                {
                  "email": "rahul@college.edu",
                  "password": "SecurePass@123"
                }
            """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<JwtResponseDTO> loginStudent(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    //Faculty Login
    @PostMapping("/faculty/login")
    @Operation(
            summary = "Faculty Login — returns JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                {
                  "email": "priya@college.edu",
                  "password": "Faculty@123"
                }
            """))
            )
    )
    public ResponseEntity<JwtResponseDTO> loginFaculty(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    //Admin Login
    @PostMapping("/admin/login")
    @Operation(
            summary = "Admin Login — returns JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                {
                  "email": "admin@college.edu",
                  "password": "Admin@Super1"
                }
            """))
            )
    )
    public ResponseEntity<JwtResponseDTO> loginAdmin(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    //Refresh Token
    @PostMapping("/refresh-token")
    @Operation(summary = "Get new access token using refresh token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                { "refreshToken": "your-refresh-token-uuid-here" }
            """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New access token issued"),
            @ApiResponse(responseCode = "401", description = "Refresh token invalid or expired")
    })
    public ResponseEntity<TokenRefreshResponseDTO> refreshToken(
            @Valid @RequestBody TokenRefreshRequestDTO dto) {
        return ResponseEntity.ok(authService.refreshToken(dto));
    }

    //Logout

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Logout — blacklists access token + deletes refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<ApiResponseDTO> logout(HttpServletRequest request,
                                                 Authentication authentication) {
        String token = parseToken(request);
        if (token != null && authentication != null) {
            authService.logout(token, authentication.getName());
        }
        return ResponseEntity.ok(ApiResponseDTO.builder()
                .message("Logged out successfully.")
                .build());
    }
    private String parseToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    // ─── Forgot Password ──────────────────────────────────────
    @PostMapping("/forgot-password")
    @Operation(summary = "Send password reset token to email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
            { "email": "rahul@college.edu" }
        """))))
    public ResponseEntity<ApiResponseDTO> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto) {
        passwordResetService.processForgotPassword(dto.getEmail());
        return ResponseEntity.ok(ApiResponseDTO.builder()
                .message("Reset link sent to email.")
                .build());
    }

    // ─── Reset Password ───────────────────────────────────────
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using token from email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
            { "token": "uuid-token-here", "newPassword": "NewPass@123" }
        """))))
    public ResponseEntity<ApiResponseDTO> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto) {
        passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok(ApiResponseDTO.builder()
                .message("Password reset successfully. Please login.")
                .build());
    }

    // ─── Change Password ──────────────────────────────────────
    @PostMapping("/change-password")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Change password (JWT protected)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
            { "oldPassword": "SecurePass@123", "newPassword": "NewPass@123" }
        """))))
    public ResponseEntity<ApiResponseDTO> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto,
            Authentication authentication) {
        studentService.changePassword(authentication.getName(), dto);
        return ResponseEntity.ok(ApiResponseDTO.builder()
                .message("Password changed successfully.")
                .build());
    }
}

