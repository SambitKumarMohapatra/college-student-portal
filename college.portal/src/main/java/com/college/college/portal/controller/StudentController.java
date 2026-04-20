package com.college.college.portal.controller;

import com.college.college.portal.dto.StudentProfileDTO;
import com.college.college.portal.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "Student", description = "Student profile and data APIs")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {
    private final StudentService studentService;

    // Student views their own profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get own profile (Student only)")
    public ResponseEntity<StudentProfileDTO> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(studentService.getStudentByEmail(auth.getName()));
    }

    // Student views another student's profile by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('FACULTY') or hasRole('ADMIN')")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<StudentProfileDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}
