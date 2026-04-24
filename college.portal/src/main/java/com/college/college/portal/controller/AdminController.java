package com.college.college.portal.controller;

import com.college.college.portal.dto.AdminProfileDTO;
import com.college.college.portal.dto.FacultyProfileDTO;
import com.college.college.portal.dto.StudentProfileDTO;
import com.college.college.portal.service.AdminService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AdminService adminService;
    private final StudentService studentService;
    private final FacultyService facultyService;

    // Admin views own profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get own admin profile")
    public ResponseEntity<AdminProfileDTO> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(adminService.getAdminByEmail(auth.getName()));
    }

    // Admin views all students
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all students (Admin only)")
    public ResponseEntity<List<StudentProfileDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Admin views all faculty
    @GetMapping("/faculty")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all faculty members (Admin only)")
    public ResponseEntity<List<FacultyProfileDTO>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }

    // Admin views all admins
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all admins (Admin only)")
    public ResponseEntity<List<AdminProfileDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // Admin views students by branch
    @GetMapping("/students/branch/{branch}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get students by branch (Admin only)")
    public ResponseEntity<List<StudentProfileDTO>> getStudentsByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(studentService.getStudentsByBranch(branch));
    }

    // Admin views faculty by department
    @GetMapping("/faculty/department/{department}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get faculty by department (Admin only)")
        public ResponseEntity<List<FacultyProfileDTO>> getFacultyByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(facultyService.getFacultyByDepartment(department));
    }

    // Dashboard stats (summary counts)
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin dashboard - total counts")
    public ResponseEntity<Map<String, Long>> getDashboard() {
        java.util.Map<String, Long> stats = new java.util.LinkedHashMap<>();
        stats.put("totalStudents", (long) studentService.getAllStudents().size());
        stats.put("totalFaculty", (long) facultyService.getAllFaculty().size());
        stats.put("totalAdmins", (long) adminService.getAllAdmins().size());
        return ResponseEntity.ok(stats);
    }

    //Reports endpoint (ADMIN only)
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin reports (ADMIN only)")
    public ResponseEntity<Map<String, Object>> getReports() {
        Map<String, Object> report = new java.util.LinkedHashMap<>();
        report.put("totalStudents", studentService.getAllStudents().size());
        report.put("totalFaculty",  facultyService.getAllFaculty().size());
        report.put("studentsByBranch", Map.of(
                "CSE", studentService.getStudentsByBranch("CSE").size(),
                "ECE", studentService.getStudentsByBranch("ECE").size(),
                "ME",  studentService.getStudentsByBranch("ME").size(),
                "CE",  studentService.getStudentsByBranch("CE").size(),
                "EE",  studentService.getStudentsByBranch("EE").size()
        ));
        return ResponseEntity.ok(report);
    }
}
