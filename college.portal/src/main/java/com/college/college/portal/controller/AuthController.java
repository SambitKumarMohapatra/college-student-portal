package com.college.college.portal.controller;

import com.college.college.portal.dto.*;
import com.college.college.portal.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",description = "Registration and Login APIs for Student, Faculty, and Admin")
public class AuthController {
    private final AuthService authService;
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
}


}
