package com.college.college.portal.controller;

import com.college.college.portal.dto.ApiResponseDTO;
import com.college.college.portal.dto.StudentRegisterDTO;
import com.college.college.portal.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Authentication",description = "Registration and Login APIs")
public class AuthController {
    private final AuthService authService;
    //Student Registration
    @PostMapping("/student/register")
    @Operation(summary = "Register a new Student")
    public ResponseEntity<ApiResponseDTO> registerStudent(
            @Valid@RequestBody StudentRegisterDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerStudent(dto));
    }

}
