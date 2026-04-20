package com.college.college.portal.service;

import com.college.college.portal.dto.FacultyProfileDTO;
import com.college.college.portal.entity.Faculty;
import com.college.college.portal.exception.ResourceNotFoundException;
import com.college.college.portal.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyProfileDTO getFacultyByEmail(String email) {
        Faculty f = facultyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + email));
        return mapToDTO(f);
    }

    public FacultyProfileDTO getFacultyById(Long id) {
        Faculty f = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
        return mapToDTO(f);
    }

    public List<FacultyProfileDTO> getAllFaculty() {
        return facultyRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<FacultyProfileDTO> getFacultyByDepartment(String department) {
        return facultyRepository.findByDepartment(department)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FacultyProfileDTO mapToDTO(Faculty f) {
        return FacultyProfileDTO.builder()
                .id(f.getId())
                .name(f.getName())
                .email(f.getEmail())
                .phone(f.getPhone())
                .designation(f.getDesignation())
                .department(f.getDepartment())
                .qualification(f.getQualification())
                .experienceYears(f.getExperienceYears())
                .role(f.getRole().name())
                .createdAt(f.getCreatedAt())
                .build();
    }

}
