package com.college.college.portal.controller;

import com.college.college.portal.dto.FacultyProfileDTO;
import com.college.college.portal.dto.StudentProfileDTO;
import com.college.college.portal.service.FacultyService;
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

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@Tag(name = "Faculty", description = "Faculty profile and student management APIs")
@SecurityRequirement(name = "bearerAuth")
public class FacultyController {
    private final FacultyService facultyService;
    private final StudentService studentService;

    // Faculty views own profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('FACULTY')")
    @Operation(summary = "Get own profile (Faculty only)")
    public ResponseEntity<FacultyProfileDTO> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(facultyService.getFacultyByEmail(auth.getName()));
    }

    // Faculty views all students
    @GetMapping("/students")
    @PreAuthorize("hasRole('FACULTY')")
    @Operation(summary = "Get all students (Faculty only)")
    public ResponseEntity<List<StudentProfileDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Faculty filters students by branch
    @GetMapping("/students/branch/{branch}")
    @PreAuthorize("hasRole('FACULTY')")
    @Operation(summary = "Get students by branch (Faculty only)")
    public ResponseEntity<List<StudentProfileDTO>> getStudentsByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(studentService.getStudentsByBranch(branch));
    }

    // Faculty filters students by semester
    @GetMapping("/students/semester/{semester}")
    @PreAuthorize("hasRole('FACULTY')")
    @Operation(summary = "Get students by semester (Faculty only)")
    public ResponseEntity<List<StudentProfileDTO>> getStudentsBySemester(@PathVariable Integer semester) {
        return ResponseEntity.ok(studentService.getStudentsBySemester(semester));
    }

    //Placeholder for reports
    @GetMapping("/reports")
    @PreAuthorize("hasRole('FACULTY')")
    @Operation(summary = "Faculty reports (FACULTY only)")
    public ResponseEntity<?> getReports(Authentication auth) {
        return ResponseEntity.ok(java.util.Map.of(
                "message", "Reports API coming in next module",
                "faculty", auth.getName()
        ));
    }
}
